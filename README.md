# ArcartX

> 面向 Spigot / Paper 的服务端插件，配合 **ArcartX 客户端 Mod** 通过自定义网络协议通信，
> 为服务器带来模型、相机、HUD/UI、路标、区域、伤害显示、天空盒、着色器等客户端级表现能力。

---

## 环境要求

| 项目        | 要求                                                          |
|-----------|-------------------------------------------------------------|
| JDK       | 17+                                                         |
| Minecraft | 1.18+                                                       |
| 服务端       | Spigot / Paper 及其分支（支持 Folia 调度）                            |
| 客户端       | 需安装 **ArcartX Mod**（下载见 [arcartx.com](https://arcartx.com)） |


## 从源码构建

```bash
# 需要 JDK 17（注意：默认 JAVA_HOME 若为 Java 8 会失败）
./gradlew build
```

产物在 `build/libs/ArcartX-<version>.jar`。

> **关于依赖**：MythicMobs、ModelEngine、PlaceholderAPI、PlayerPoints、Vault 等第三方插件 API
> 均通过公共 Maven 仓库以 `compileOnly` 拉取（见 [build.gradle.kts](build.gradle.kts)）。
> 少数无公共仓库的中文社区闭源插件（AttributePlus、AstraX）以本地 jar 形式置于 `libs/`，
> 它们仅用于编译期，不会被打包进产物。

## 开发者 API

ArcartX 通过 `ArcartXAPI` 为附属插件提供受控访问入口（UI / 实体 / 区域 / 音效 / 网络发送等）。

```kotlin
repositories {
    maven("https://repo.arcartx.com/repository/maven-releases/")
}
dependencies {
    compileOnly("priv.seventeen.artist.arcartx:ArcartX:{VERSION}")
}
```

```kotlin
val ui = ArcartXAPI.getUIRegistry()
val player = ArcartXAPI.getEntityManager().getPlayer(bukkitPlayer)
```


## 安全

协议层威胁模型与部署安全建议见 [SECURITY.md](SECURITY.md)。

## 许可证

[ArcartX 源码可见许可协议 1.0](LICENSE) · Copyright © 2026 17Artist

源码公开，但**非** OSI 开源协议；使用本软件需接受 [ArcartX EULA](https://arcartx.com/resources/eula/view)。
