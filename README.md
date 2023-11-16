# easy-gradle-plugin

This is a Gradle plugin for Easy Extension Framework for SAP Commerce.

The objective of this plugin is to:
- Create an Easy extension by scaffolding
- Install/update/unstall extension use Easy REST API
- Run Groovy Tests
- Generate Easy Type models

# How to test the plugin
  Below are the examples to test the plugin
## Generate an Easy Extension
In this example, we will create a new extension called `helloworld` in a directory `easy-sample-repo` that represents a local repository for easy extension framework:
- `mkdir easy-sample-repo`
- `cd easy-sample-repo`
- Configure the easy repository in SAP Commerce Cloud backoffice as per instructions in [Configuring a Local Repository Documentation](https://sap.github.io/easy-extension-framework/configuring-an-easy-repository-in-backoffice/#configuring-a-local-repository)
- Run the command `gradle init --type basic --project-name easy-sample-repo`
- Copy the following content into `build.gradle`
    ```groovy
    plugins {
        id 'groovy'
        id "io.github.yannickrobin.easy-gradle-plugin" version "0.0.3"
    }
    
    repositories {
        mavenCentral()
    }
    ```
- Create file `gradle.properties` in `easy-sample-repo` directory with following content
    ```properties
    # The base url of the easy api for your SAP Commerce Cloud. Below if for a local SAP Commerce Cloud Server
    sap.commerce.easy.api.base.url = https://localhost:9002/easyrest/easyapi
    
    # Value of easy.apiKey property configured in SAP Commerce Cloud (by default, this is 123456)
    sap.commerce.easy.api.key = 123456
    
    # The easy version deployed on your SAP Commerce Cloud instance
    sap.commerce.easy.extension.easy.version = 0.2
    
    # Base package for your easy extension
    sap.commerce.easy.extension.base.package=com.sap.cx.boosters.easy.extension
    
    # The value of code attribute of the easy repository configured in your SAP Commerce Cloud instance
    sap.commerce.easy.repository.code=easy-sample-repo
    
    # Path to the hybris home directory on your computer to add SAP Commerce Cloud libraries to your extension
    commercePlatformHome=/opt/hybris
    ```
- Run the command `./gradlew easy-ext-gen -PeasyExtensionId=helloworld`
  ```
  > Task :easy-ext-gen
  Generating new easy extension in current directory

  BUILD SUCCESSFUL in 1s
  1 actionable task: 1 executed
  ```

> Implement the extension as per the instructions available at [Easy Extension Documentation](https://sap.github.io/easy-extension-framework/easy-extension) 

## Update Repository
- Run the command `./gradlew easy-update-repo` to list all available extensions in the repository
  ```
  > Task :easy-update-repo
  API executed successfully. HTTP status: 200
  {
  "eventId": "00000001",
  "message": "Update request for repository easy-sample-repo submitted"
  }
  
  BUILD SUCCESSFUL in 3s
  1 actionable task: 1 executed
  ```

## List Easy Extensions
- Run the command `./gradlew easy-ext-list` to list all available extensions in the repository

    ```
    > Task :easy-ext-list
  API executed successfully. HTTP status: 200
  [
    ...
  ]
    
    BUILD SUCCESSFUL in 1s
    1 actionable task: 1 executed
    ```

## Install Extension
- Run the command `./gradlew easy-ext-install -PextensionId=helloworld` to install `helloworld` extension of the repository
  ```
  > Task :easy-ext-install
  API executed successfully. HTTP status: 200
  {
  "eventId": "00000002",
  "message": "Installation request for extension helloworld submitted."
  }
  
  BUILD SUCCESSFUL in 3s
  1 actionable task: 1 executed
  ```

## Update Extension
- Run the command `./gradlew easy-ext-reinstall -PextensionId=helloworld` to reinstall/reload/update `helloworld` extension of the repository
  ```
  > Task :easy-ext-reinstall
  API executed successfully. HTTP status: 200
  {
  "eventId": "00000003",
  "message": "Re-installation/reload request for extension helloworld submitted."
  }
  
  BUILD SUCCESSFUL in 2s
  1 actionable task: 1 executed
  ```

## Uninstall Extension
- Run the command `./gradlew easy-ext-uninstall -PextensionId=helloworld` to uninstall `helloworld` extension of the repository
  ```
  > Task :easy-ext-uninstall
  API executed successfully. HTTP status: 200
  {
  "eventId": "00000004",
  "message": "Uninstallation request for extension helloworld submitted."
  }
  
  BUILD SUCCESSFUL in 1s
  1 actionable task: 1 executed
  ```

## All Tasks
- Run the command `./gradlew tasks` to see all the task available with this plugin

  ```
  Easy tasks
  ----------
  easy-dump-classpath - Dumps the classpath
  easy-ext-gen - Generates an easy extension in current directory
  easy-ext-install - Installs an easy extension
  easy-ext-list - Lists the easy extensions of a repository
  easy-ext-reinstall - Re-installs/reloads an easy extension
  easy-ext-uninstall - Uninstalls an easy extension
  easy-update-repo - Updates the repository from remote location
  ```
