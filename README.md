<p align="center">
<img src="https://static.naturalsmp.net/imgs/naturalstacker-logo.png" />
<h2 align="center">The first ever multi-threaded stacking solution!</h2>
</p>
<br>
<p align="center">
<a href="https://naturalsmp.net/discord/"><img src="https://img.shields.io/discord/293212540723396608?color=7289DA&label=Discord&logo=discord&logoColor=7289DA&link=https://naturalsmp.net/discord/"></a>
<a href="https://naturalsmp.net/patreon/"><img src="https://img.shields.io/badge/-Support_on_Patreon-F96854.svg?logo=patreon&style=flat&logoColor=white&link=https://naturalsmp.net/patreon/"></a><br>
<a href=""><img src="https://img.shields.io/maintenance/yes/2026"></a>
</p>

## Compiling

You can compile the project using gradlew.<br>
Run `./gradlew shadowJar` in console to build the project.<br>
You can find already compiled jars on our [GitHub Releases](https://github.com/Natural-Minecraft/NaturalStacker/releases) page!<br>

## API

The plugin is packed with a rich API for interacting with entities, items and more. When hooking into the plugin, it's highly recommended to only use the API and not the compiled plugin, as the API methods are not only commented, but also will not get removed or changed unless they are marked as deprecated. This means that by using the API, you won't have to do any additional changes to your code between updates.

##### Maven
```xml
<repositories>
    <repository>
        <id>natural-repo</id>
        <url>https://repo.naturalsmp.net/repository/api/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>id.naturalsmp</groupId>
        <artifactId>NaturalStackerAPI</artifactId>
        <version>latest</version>
    </dependency>
</dependencies>
```
##### Gradle
```gradle
repositories {
    maven { url 'https://repo.naturalsmp.net/repository/api/' }
}

dependencies {
    compileOnly 'id.naturalsmp:NaturalStackerAPI:latest'
}
```
## Updates

This plugin is provided "as is", which means no updates or new features are guaranteed. We will do our best to keep 
updating and pushing new updates, and you are more than welcome to contribute your time as well and make pull requests
for bug fixes. 

## License

This plugin is licensed under GNU GPL v3.0

This plugin uses HikariCP which you can find [here](https://github.com/brettwooldridge/HikariCP).
