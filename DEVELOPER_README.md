# Documentation for developers

# How to build the plugin
- Install Gradle 7.6
- Clone this repository
- Run the command `gradle build`

The build will generate the following jar `build/libs/easy-plugin-x.x.x-SNAPSHOT.jar`

# How to test the plugin
In this example, we will create a new extension called `helloworld`:
- `mkdir helloworld`
- `cd helloworld`
- Run the command `gradle init --type basic --project-name helloworld`
- Create a `lib` folder and add `easy-plugin-x.x.x-SNAPSHOT.jar`
- Copy the following content into `build.gradle`
```
buildscript {
    repositories {
        flatDir dirs: "lib"
    }
    dependencies {
        classpath "io.github.yannickrobin:easy-plugin:x.x.x-SNAPSHOT"
        classpath group: 'org.codehaus.groovy.modules.http-builder', name: 'http-builder', version: '0.7.1'   
    }
}

plugins {
    id 'groovy'
}

repositories {
    mavenCentral()
}

apply plugin: 'io.github.yannickrobin.easyplugin'
```

# How to publish

- Update the version of the plugin to fit with the version you want to pubish into `build.gradle`:
```
group 'io.github.yannickrobin'
version '0.0.2'
```

> The version should not contain `-SNAPSHOT`.
> Once the plugin is published, you cannot override or remove the artifact so be very careful before publishing.

- Publish the plugin by using the `publishPlugin` task: `gradle publishPlugins -Pgradle.publish.key=<key> -Pgradle.publish.secret=<secret>`
