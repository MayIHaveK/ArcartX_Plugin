/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.config

import com.google.gson.annotations.SerializedName
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.annotation.Comment
import priv.seventeen.artist.arcartx.commons.config.annotation.Comments
import priv.seventeen.artist.arcartx.commons.config.annotation.Implicit
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendException
import java.io.BufferedReader
import java.io.StringReader
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*
import kotlin.math.max

/** 配置节反射加载器 */
open class ArcartXConfigSection {

    @Transient
    private var tabLevel = 0

    @Transient
    private var _id = "root"

    fun setParentTabLevel(level: Int) {
        this.tabLevel = level + 1
    }

    protected open fun load(plugin: JavaPlugin, filePath: String, configurationSection: ConfigurationSection?) {
        if (configurationSection == null) {
            return
        }
        for (field in getAllFields(this.javaClass)) {
            field.isAccessible = true
            try {
                if (field.isAnnotationPresent(SerializedName::class.java) && !field.isAnnotationPresent(Implicit::class.java)) {
                    if(Map::class.java.isAssignableFrom(field.type)){
                        if (field.genericType is ParameterizedType) {
                            val actualTypeArguments: Array<Type> = (field.genericType as ParameterizedType).actualTypeArguments
                            if (actualTypeArguments.size == 2) {
                                if (actualTypeArguments[0] == String::class.java) {
                                    if (actualTypeArguments[1] is Class<*>) {
                                        val valueType = actualTypeArguments[1] as Class<*>
                                        if(ArcartXConfigSection::class.java.isAssignableFrom(valueType)){
                                            loadConfigSectionMapField(plugin, filePath, configurationSection, field)
                                        } else {
                                            loadConfigSectionPairsField(configurationSection, field)
                                        }
                                    } else {
                                        loadConfigSectionPairsField(configurationSection, field)
                                    }
                                } else {
                                    plugin.sendException(
                                        L(AXLanguageKey.EXCEPTION_CONFIG_FIELD_LOAD, filePath, configurationSection.currentPath + "." + field.name), Exception(
                                            L(AXLanguageKey.EXCEPTION_MAP_KEY_NOT_STRING)
                                        ))
                                }
                            } else {
                                plugin.sendException(
                                    L(AXLanguageKey.EXCEPTION_CONFIG_FIELD_LOAD, filePath, configurationSection.currentPath + "." + field.name), Exception(
                                        L(AXLanguageKey.EXCEPTION_MAP_TWO_GENERICS)
                                    ))
                            }
                        } else {
                            plugin.sendException(
                                L(AXLanguageKey.EXCEPTION_CONFIG_FIELD_LOAD, filePath, configurationSection.currentPath + "." + field.name), Exception(
                                    L(AXLanguageKey.EXCEPTION_MAP_PARAMETERIZED)
                                ))
                        }
                    }
                    else if(ArcartXConfigSection::class.java.isAssignableFrom(field.type)){
                        loadConfigurationSectionField(plugin, filePath, configurationSection, field)
                    } else {
                        loadConfigurationField(configurationSection, field)
                    }
                }
            } catch (e: Exception) {
                plugin.sendException(L(AXLanguageKey.EXCEPTION_CONFIG_FIELD_LOAD, filePath, configurationSection.currentPath + "." + field.name), e)
            }
        }
        // 所有字段加载完成后执行子类自定义校验
        try {
            validate(plugin, filePath)
        } catch (e: Exception) {
            plugin.sendException(L(AXLanguageKey.EXCEPTION_CONFIG_FIELD_LOAD, filePath, configurationSection.currentPath ?: ""), e)
        }
    }

    /**
     * 配置加载后的校验钩子。子类可重写以校验/修正字段值（如范围钳制、必填校验）。
     * 抛出的异常会被记录但不会中断其它配置的加载。默认不做任何校验。
     */
    protected open fun validate(plugin: JavaPlugin, filePath: String) {}

    @Throws(IllegalAccessException::class)
    private fun loadConfigurationField(configurationSection: ConfigurationSection, field: Field) {
        val valueName: String = field.getAnnotation(SerializedName::class.java).value;
        val defaultValue = field[this]
        val clazz: Class<*> = defaultValue::class.java
        val value = configurationSection[valueName, defaultValue]
        if (defaultValue is Number) {
            // 类型不符时给出清晰提示（由外层 catch 带字段路径上报），而非晦涩的 ClassCastException
            if (value !is Number) {
                throw IllegalArgumentException(L(AXLanguageKey.EXCEPTION_VALUE_NOT_NUMBER, "$value"))
            }
            field[this] = when (defaultValue) {
                is Long -> value.toLong()
                is Int -> value.toInt()
                is Double -> value.toDouble()
                is Float -> value.toFloat()
                else -> value
            }
        } else if (defaultValue is List<*>) {
            if(configurationSection.getList(valueName) == null){
                field[this] = configurationSection.getString(valueName) ?.let { ArrayList(listOf(it)) } ?: defaultValue
            } else {
                field[this] =  configurationSection.getList(valueName, defaultValue)
            }
        } else if (clazz == String::class.java) {
            field[this] = configurationSection.getString(valueName, defaultValue.toString())!!
                .replace("</n/>", "\n")
        } else {
            field[this] = (value to clazz ).first
        }
    }


    @Throws(Exception::class)
    private fun loadConfigurationSectionField(
        plugin: JavaPlugin,
        filePath: String,
        configurationSection: ConfigurationSection,
        field: Field
    ) {
        val valueName: String = field.getAnnotation(SerializedName::class.java).value
        val sub = configurationSection.getConfigurationSection(valueName)
        if (sub != null) {
            val subObj = field.type.getConstructor().newInstance() as ArcartXConfigSection
            subObj.load(plugin, filePath, sub)
            subObj._id = valueName
            field[this] = subObj
        }
    }

    @Throws(Exception::class)
    private fun loadConfigSectionPairsField(
        configurationSection: ConfigurationSection,
        field: Field
    ) {
        val valueName: String = field.getAnnotation(SerializedName::class.java).value
        val root: Boolean = valueName == "root"
        val sub = if (root) configurationSection else configurationSection.getConfigurationSection(valueName)
        if (sub != null) {
            val pairs: MutableMap<String, Any?> = HashMap()
            for (key in sub.getKeys(false)) {
                if(sub.isConfigurationSection(key)){
                    val map: MutableMap<String, Any?> = HashMap()
                    val subObj = sub.getConfigurationSection(key)
                    subObj?.getKeys(false)?.forEach {
                        map[it] = subObj.getString(it)
                    }
                    pairs[key] = map
                } else if(sub.isList(key)){
                    val listValue = sub.getList(key)
                    pairs[key] = listValue
                } else {
                    val stringValue = sub.getString(key, "")
                    pairs[key] = stringValue!!.replace("</n/>", "\n")
                }
            }
            field[this] = pairs
        }
    }

    @Throws(Exception::class)
    private fun loadConfigSectionMapField(
        plugin: JavaPlugin,
        filePath: String,
        configurationSection: ConfigurationSection,
        field: Field
    ) {
        val valueName: String = field.getAnnotation(SerializedName::class.java).value
        val root: Boolean = valueName == "root"
        val mapGenericType = field.genericType as ParameterizedType
        val mapActualTypeArguments = mapGenericType.actualTypeArguments
        val value = mapActualTypeArguments[1] as Class<*>

        val sub = if (root) configurationSection else configurationSection.getConfigurationSection(valueName)
        if (sub != null) {
            val map = field[this] as MutableMap<Any,Any>
            map.clear()

            for (subKey in sub.getKeys(false)) {
                val subSubSection = sub.getConfigurationSection(subKey)
                if (subSubSection != null) {
                    val subObj = value.getConstructor().newInstance() as ArcartXConfigSection
                    subObj.load(plugin, filePath, subSubSection)
                    subObj._id = valueName
                    map[subKey] = subObj
                }
            }
        }
    }

    protected fun write(plugin: JavaPlugin, filePath: String, context: StringBuilder) {
        val line = System.lineSeparator()
        val tabHead = "  ".repeat(max(0.0, tabLevel.toDouble()).toInt())
        try {
            writeAnnotations(context, line, tabHead, javaClass.annotations)
            for (field in getAllFields(this.javaClass)) {
                field.isAccessible = true
                writeAnnotations(context, line, tabHead, field.annotations)
                if (field.isAnnotationPresent(SerializedName::class.java) && !field.isAnnotationPresent(Implicit::class.java)) {
                    if(Map::class.java.isAssignableFrom(field.type)){
                        if (field.genericType is ParameterizedType) {
                            val actualTypeArguments: Array<Type> = (field.genericType as ParameterizedType).actualTypeArguments
                            if (actualTypeArguments.size == 2) {
                                if (actualTypeArguments[0] == String::class.java) {
                                    if (actualTypeArguments[1] is Class<*>) {
                                        val valueType = actualTypeArguments[1] as Class<*>
                                        if(ArcartXConfigSection::class.java.isAssignableFrom(valueType)){
                                            writeConfigSectionMapField(plugin, filePath, context, line, tabHead, field)
                                        } else {
                                            writeConfigSectionPairsField(context, line, tabHead, field)
                                        }
                                    } else {
                                        writeConfigSectionPairsField(context, line, tabHead, field)
                                    }
                                } else {
                                    plugin.sendException(
                                        L(AXLanguageKey.EXCEPTION_CONFIG_FIELD_WRITE, filePath, field.name), Exception(
                                            L(AXLanguageKey.EXCEPTION_MAP_KEY_NOT_STRING)
                                        ))
                                }
                            } else {
                                plugin.sendException(
                                    L(AXLanguageKey.EXCEPTION_CONFIG_FIELD_WRITE, filePath, field.name), Exception(
                                        L(AXLanguageKey.EXCEPTION_MAP_TWO_GENERICS)
                                    ))
                            }
                        } else {
                            plugin.sendException(
                                L(AXLanguageKey.EXCEPTION_CONFIG_FIELD_WRITE, filePath, field.name), Exception(
                                    L(AXLanguageKey.EXCEPTION_MAP_PARAMETERIZED)
                                ))
                        }
                    }
                    else if(ArcartXConfigSection::class.java.isAssignableFrom(field.type)){
                        writeConfigurationSectionField(plugin, filePath, context, line, tabHead, field)
                    } else {
                        writeConfigurationField(plugin, context, line, tabHead, field)
                    }
                }
            }
        } catch (e: Exception) {
            plugin.sendException(L(AXLanguageKey.EXCEPTION_CONFIG_WRITE, filePath), e)
        }
    }

    private fun writeAnnotations(
        context: StringBuilder,
        line: String,
        tabHead: String,
        annotations: Array<Annotation>
    ) {
        for (annotation in annotations) {
            if (annotation is Comments) {
                for (comment in annotation.value) {
                    context.append(tabHead).append("# ").append(comment.value).append(line)
                }
            } else if (annotation is Comment) {
                context.append(tabHead).append("# ").append(annotation.value).append(line)
            }
        }
    }

    @Throws(IllegalAccessException::class)
    private fun writeConfigurationField(
        plugin: JavaPlugin,
        context: StringBuilder,
        line: String,
        tabHead: String,
        field: Field
    ) {
        val valueName: String = field.getAnnotation(SerializedName::class.java).value
        val value = field[this]
        if (value is List<*>) {
            context.append(tabHead).append(valueName).append(":").append(line)
            for (item in value) {
                context.append(tabHead).append("  - ").append(if (item is String) "\"" + item + "\"" else item)
                    .append(line)
            }
        } else if (value is String) {
            if (value.contains("\n")) {
                context.append(tabHead).append(valueName).append(": |").append(line)
                for (l in value.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                    context.append(tabHead).append("  ").append(l).append(line)
                }
            } else {
                context.append(tabHead).append(valueName).append(": \"").append(value).append("\"").append(line)
            }
        } else if (value is ItemStack) {
            context.append(tabHead).append(valueName).append(":").append(line)
            val yamlConfiguration = YamlConfiguration()
            yamlConfiguration["item"] = value
            try {
                BufferedReader(StringReader(yamlConfiguration.saveToString())).use { reader ->
                    var itemLine: String?
                    var start = false
                    while ((reader.readLine().also { itemLine = it }) != null) {
                        if (start) {
                            context.append(tabHead).append(itemLine).append(line)
                        } else {
                            start = true
                        }
                    }
                }
            } catch (exception: Exception) {
                plugin.sendException(L(AXLanguageKey.EXCEPTION_ITEM_WRITE), exception)
            }
        } else {
            context.append(tabHead).append(valueName).append(": ").append(value).append(line)
        }
    }

    @Throws(IllegalAccessException::class)
    private fun writeConfigSectionPairsField(context: StringBuilder, line: String, tabHead: String, field: Field) {
        val valueName: String = field.getAnnotation(SerializedName::class.java).value
        val root: Boolean = valueName == "root"
        val pairs = field[this] as Map<*, *>
        if (!root) {
            context.append(tabHead).append(valueName).append(":").append(line)
            pairs.forEach { (k: Any?, v: Any?) -> appendKeyValue(context, line, "$tabHead  ", k!!, v!!) }
        } else {
            pairs.forEach { (k: Any?, v: Any?) -> appendKeyValue(context, line, tabHead, k!!, v!!) }
        }
    }

    private fun appendKeyValue(context: StringBuilder, line: String, tabHead: String, key: Any, value: Any) {
        val text = value.toString()
        if (text.contains("\n")) {
            context.append(tabHead).append(key).append(": |").append(line)
            for (l in text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                context.append(tabHead).append("  ").append(l).append(line)
            }
        } else {
            context.append(tabHead).append(key).append(": \"").append(value).append("\"").append(line)
        }
    }

    @Throws(IllegalAccessException::class)
    private fun writeConfigurationSectionField(
        plugin: JavaPlugin,
        filePath: String,
        context: StringBuilder,
        line: String,
        tabHead: String,
        field: Field
    ) {
        val valueName: String = field.getAnnotation(SerializedName::class.java).value
        context.append(tabHead).append(valueName).append(":").append(line)
        val subSection = field[this] as ArcartXConfigSection
        subSection.setParentTabLevel(tabLevel)
        subSection.write(plugin, filePath, context)
    }

    @Throws(IllegalAccessException::class)
    private fun writeConfigSectionMapField(
        plugin: JavaPlugin,
        filePath: String,
        context: StringBuilder,
        line: String,
        tabHead: String,
        field: Field
    ) {
        val valueName: String = field.getAnnotation(SerializedName::class.java).value
        val root: Boolean = valueName == "root"
        val map = field[this] as Map<*, *>
        if (!root) {
            context.append(tabHead).append(valueName).append(":").append(line)
            map.forEach { (k: Any?, v: Any?) ->
                context.append(tabHead).append("  ").append(k).append(":").append(line)
                if (v is ArcartXConfigSection) {
                    v.setParentTabLevel(tabLevel + 1)
                    v.write(plugin, filePath, context)
                }
            }
        } else {
            map.forEach { (k: Any?, v: Any?) ->
                context.append(tabHead).append(k).append(":").append(line)
                if (v is ArcartXConfigSection) {
                    v.setParentTabLevel(tabLevel)
                    v.write(plugin, filePath, context)
                }
            }
        }
    }



    private fun getAllFields(clazz: Class<*>?): List<Field> {
        var clazz2 = clazz
        val fieldList: MutableList<Field> = ArrayList()
        while (clazz2 != null) {
            fieldList.addAll(listOf(*clazz2.declaredFields))
            clazz2 = clazz2.superclass
        }
        return fieldList
    }
}