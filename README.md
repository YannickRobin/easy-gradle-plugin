# easy-gradle-plugin

This is a Gradle plugin for Easy Extensibility Framework for SAP Commerce.

The objective of this plugin is to:
- Create an Easy extension by scaffolding
- Install/update/unstall extension use Easy REST API
- Run Groovy Tests
- Generate Easy Type models

# How to test the plugin
In this example, we will create a new extension called `helloworld`:
- `mkdir helloworld`
- `cd helloworld`
- Run the command `gradle init --type basic --project-name helloworld`
- Copy the following content into `build.gradle`
```
plugins {
    id 'groovy'
    id "io.github.yannickrobin.easyplugin" version "0.0.2"
}

repositories {
    mavenCentral()
}
```

- Set the following environment variables

| Environment Variable | Description |
| ------------- | ------------- |
| EASY_BASE_URL  | The base URL of your SAP Commerce Cloud server |
| EASY_REPOSITORY  | The Easy Repository configured in SAP Commerce Cloud |
| EASY_API_KEY  | `easy.apiKey` property configured in SAP Commerce Cloud (by default, this is 123456) |

Below is an example.
```
export EASY_BASE_URL=http://localhost:9002
export EASY_REPOSITORY=easy-extension-samples
export EASY_API_KEY=123456
```

- Run the command `./gradlew listEasyExtensions` to list all extension available in the repository

To add SAP Commerce libraries of a local installation you need to specify `commercePlatformHome` property in `gradle.properties` file:

commercePlatformHome=~/SAPDevelop/hybris/hybris-2211-easy/hybris


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
