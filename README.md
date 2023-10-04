# easy-gradle-plugin

This is a Gradle plugin for Easy Extensibility Framework for SAP Commerce.

The objective of this plugin is to:
- Create an Easy extension by scaffolding
- Install/update/unstall extension use Easy REST API
- Run Groovy Tests
- Generate Easy Type models

# How to build the plugin
- Install Gradle 7.6
- Clone this repository
- Run the command `gradle build`

The build will generate the following jar `build/libs/easy-plugin-0.0.1.jar`

# How to test the plugin
In this example, we will create a new extension called `helloworld`:
- `mkdir helloworld`
- `cd helloworld`
- Run the command `gradle init --type basic --project-name helloworld`
- Create a `lib` folder and add `easy-plugin-0.0.1.jar`
- Copy the following content into `build.gradle`
```
buildscript {
    repositories {
        flatDir dirs: "lib"
    }
    dependencies {
        classpath "io.github.yannickrobin:easy-plugin:0.0.1"
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

- Set the following environment variables

| Environment Variable | Description |
| ------------- | ------------- |
| EASY_BASE_URL  | The base URL of your SAP Commerce Cloud server |
| EASY_REPOSITORY  | The Easy Repository configured in SAP Commerce Cloud |

Below is an example.
```
export EASY_BASE_URL=http://localhost:9002
export EASY_REPOSITORY=easy-extension-samples
```

- Run the command `./gradlew listEasyExtensions` to list all extension available in the repository

``` 

> Task :listEasyExtensions
Welcome to Easy Gradle Plugin

SAP Commerce Base URL: https://localhost:9002
Repository: easy-extension-samples
Extension: my-easy-extension

List extensions...
API call successfull. HTTP status: 200
[extensions:[[

...

BUILD SUCCESSFUL in 608ms
1 actionable task: 1 executed
```

# Other tasks
- Run the command `./gradlew tasks` to see all the task available with this plugin

```
Easy tasks
----------
initEasyExtension - Init extension
installEasyExtension - Install extension
listEasyExtensions - List extensions
uninstallEasyExtension - Uninstall extension
updateEasyRepository - Update repository
```
