# easy-gradle-plugin

This is a Gradle plugin for Easy Extensibility Framework for SAP Commerce.

The objective of this plugin is to:
- Create an Easy extension by scaffolding
- Install/unstall extension use Easy REST API
- Run Groovy Tests
- Generate Easy Type models

!!! In development, not ready yet

# How to build the plugin
- Install Gradle 7.6
- Clone this repository
- Run the command `gradle build`

The build will generate the following jar `build/libs/easy-plugin-0.0.1.jar`

# How to test the plugin
- Create a new folder
- Create a `lib` folder inside and add `easy-plugin-0.0.1.jar`
- Create a file called `build.gradle` with the following content
```
buildscript {
    repositories {
        flatDir dirs: "lib"
    }
    dependencies {
        classpath "com.sap.cx.boosters:easy-plugin:0.0.1"
    }
}

apply plugin: 'com.sap.cx.boosters.easy-plugin'
```
- Run the command `gradle create`

```
> Task :create
Create extension 'myEasyExtension' ...

BUILD SUCCESSFUL in 418ms
1 actionable task: 1 executed
```
