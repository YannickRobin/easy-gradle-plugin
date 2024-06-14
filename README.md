# easy-gradle-plugin
This is a Gradle plugin for Easy Extension Framework for SAP Commerce.

The objective of this plugin is to:
- Create a new Easy extension by scaffolding
- Install/update/unstall extension use Easy REST API
- Run Groovy Tests
- Generate Easy Type models

# Getting started

Below are the instructions for a quick setup of your development environment.

## Preparation
Execute the following pre-requisites:
- Easy Gradle plugin requires Java JDK version 17.0 or higher. To check, run `java -version`
  ```
  % java -version
  openjdk version "17.0.9" 2023-10-17
  OpenJDK Runtime Environment Homebrew (build 17.0.9+0)
  OpenJDK 64-Bit Server VM Homebrew (build 17.0.9+0, mixed mode, sharing)
  ```
- Install SAP Commerce Cloud 22.11.0 or higher
- Setup [Easy Extension Framework 0.3](https://sap.github.io/easy-extension-framework/getting-started/) 
- Install [Gradle](https://gradle.org/install/) version 8.8 or higher
- Create your global gradle configuration properties file:
  - Unix/MacOS - `~/.gradle/gradle.properties`
  - Windows - `<WINDOWS_DRIVE>:\Users\<USER_NAME>\.gradle\gradle.properties`
- Configure you gradle environment for `easy-gradle-plugin` by adding the following properties to global `gradle.properties`
  ```properties
  # The base url of the easy rest for your SAP Commerce Cloud. Below if for a local SAP Commerce Cloud Server
  sap.commerce.easy.rest.base.url=https://localhost:9002/easyrest
  # The base url of the easy api for your SAP Commerce Cloud. Below if for a local SAP Commerce Cloud Server
  sap.commerce.easy.api.base.url=https://localhost:9002/easyrest/easyapi
  # Value of easy.apiKey property configured in SAP Commerce Cloud (by default, this is 123456)
  sap.commerce.easy.api.key=123456
  # Path to the hybris home directory on your computer to add SAP Commerce Cloud libraries to your extension
  sap.commerce.easy.platform.home=<SAP COMMERCE HOME>
  ```

## Configure your local Easy Repository
In this example, we will create an Easy local repository `easy-repo-local` that represents your development folder for Easy extensions:
- `mkdir easy-repo-local`
- `cd easy-repo-local`
- In SAP Commerce Cloud backoffice, configure the Easy repository that points to your local folder as per instructions in [Configuring a Local Repository Documentation](https://sap.github.io/easy-extension-framework/configuring-an-easy-repository-in-backoffice/#configuring-a-local-repository)
- Run the command `gradle init --type basic --dsl groovy --no-incubating --project-name easy-repo-local`
- If you are running the local setup of the plugin and not the published one. Add the following content in `settings.gradle` file as the **_first line_**
  ```  
   pluginManagement {
     repositories {
       mavenLocal()
       gradlePluginPortal()
       mavenCentral()
     }
   }
  ```  
- Copy the following content into `build.gradle`
    ```groovy
    plugins {
        id "io.github.yannickrobin.easy-gradle-plugin" version "0.0.5"
    }
    ```
- Create file `gradle.properties` in `easy-repo-local` directory with following content
    ```properties  
    # Base package for your easy extension
    sap.commerce.easy.extension.base.package=com.mycompany.commerce.easy.extension
    
    # The value of code attribute of the easy repository configured in your SAP Commerce Cloud instance
    sap.commerce.easy.repository.code=easy-repo-local
    ```
## Generate an Easy Extension
In this example, we will generate an easy extension `helloworld` in local repository `easy-repo-local`:
- Run the command `./gradlew easy-ext-gen`

```
> Configure project :
searching extensions in path: /Users/xxxx/hybris_2211/hybris/bin

What is the extension id?  (default: helloworld): helloworld         


What is the extension description?  (default: Lorem ipsum dollar): The helloworld Extension


Who is/are the author(s) of this extension?  (default: XYZ, ABC): Snowman


> Task :easy-ext-gen
*** Configuration for extension generation ***
Easy version: '0.3'
Extension Id: 'helloworld'
Extension Base Package: 'com.sap.cx.boosters.easy.extension'
Extension Package Name: 'com.sap.cx.boosters.easy.extension.helloworld'
Extension Package Folder: 'com/sap/cx/boosters/easy/extension/helloworld'
Extension Description: 'The helloworld Extension'
Extension Authors: 'Snowman'
********************************************** 
Generating new easy extension in current directory
Generated new easy extension [helloworld] in current directory
```

- Alternatively, you can run the same in non-interactive mode 
    ```
    ./gradlew easy-ext-gen --extensionId helloworld --noninteractive
    ```
- You can also provide all properties as parameters to avoid the user prompts as:
    ```
    ./gradlew easy-ext-gen --extensionId helloworld --basePackage io.github.me.easy --extensionDescription "My helloworld extension" --authors "Me"
    ```
- Change the current directory to point to your new extension `cd helloworld`
- Run the command `gradle wrapper` to generate Gradle wrapper files
- Execute tests with `./gradlew test` to check your extension is correctly setup (please note e2e test will not work until you have installed the extension as described below)
- You can now implement the extension as per the instructions available at [Easy Extension Documentation](https://sap.github.io/easy-extension-framework/easy-extension) 

## Update Repository
- Run the command `./gradlew easy-update-repo` to list all available extensions in the repository
```
  > Task :easy-update-repo
  API executed successfully. HTTP status: 200
  {
  "eventId": "00000001",
  "message": "Update request for repository easy-repo-local submitted"
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
- Update the repository as described above
- Run the command `./gradlew :helloworld:easy-ext-install` to install `helloworld` extension of the repository from the repository directory
- Alternatively, you can execute `./gradlew easy-ext-install` from the easy extension directory
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
- Run the command `./gradlew :helloworld:easy-ext-reinstall` to reinstall/reload/update `helloworld` extension of the repository
- Alternatively, you can execute `./gradlew easy-ext-reinstall` from the easy extension directory
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
- Run the command `./gradlew :helloworld:easy-ext-uninstall` to uninstall `helloworld` extension of the repository
- Alternatively, you can execute `./gradlew easy-ext-uninstall` from the easy extension directory
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

## Generate Model and Enum classes for Easy Types
### Prerequisites
  - Define the Easy type definitions in `easytypes.json` file, for example:
    ```json
      {
        "itemtypes" : [
          {
            "code" : "Product",
            "autocreate": "false",
            "generate": "false",
            "modelClassName" : "HelloWorldProductModel",
            "superTypeModelClass" : "de.hybris.platform.core.model.product.ProductModel",
            "attributes" : [
              {
                "qualifier" : "user",
                "type" : "User",
                "typeClass" : "de.hybris.platform.core.model.user.UserModel",
                "name" : [
                  {
                    "lang" : "en",
                    "value" : "User"
                  }
                ],
                "persistence" : {
                  "type" : "property",
                  "column" : "p_user"
                },
                "modifiers" : {
                  "unique" : "false",
                  "initial" : "false",
                  "optional" : "true",
                  "write" : "true",
                  "partof" : "false"
                }
              }
            ]
          }
        ]
      }
    ```
  - Go to the extension directory `./gradlew :helloworld:easy-class-gen`
  - Alternatively, you can execute `./gradlew easy-class-gen` from the easy extension directory
  - You can see find the generated classes inside generated source directory: `gensrc/main/groovy`
  - For example: `HelloWorldProductModel` class as:
    ```groovy
      /*
       * Copyright (c) 2023 SAP SE or an SAP affiliate company. All rights reserved.
       * ----------------------------------------------------------------
       * --- WARNING: THIS FILE IS GENERATED BY EASY GRADLE PLUGIN AND WILL BE
       * --- OVERWRITTEN IF REGENERATED AGAIN USING easy-class-gen GRADLE TASK!
       * --- Generated at Tue Nov 21 21:55:40 GMT 2023
       * ----------------------------------------------------------------
       */

       package com.sap.cx.boosters.easy.extension.helloworld.models

       import de.hybris.platform.core.model.ItemModel
       import de.hybris.platform.core.model.user.UserModel
       import de.hybris.platform.core.model.product.ProductModel
       import java.lang.String
       import java.util.Locale
       import de.hybris.bootstrap.annotations.Accessor
       import de.hybris.platform.servicelayer.model.ItemModelContext

       class HelloWorldProductModel extends ProductModel {

           static final String _TYPECODE = "Product"

           static final String USER = "user"

           HelloWorldProductModel() {
               super()
           }

           HelloWorldProductModel(final ItemModelContext ctx) {
               super(ctx)
           }

           @Accessor(qualifier = USER, type = Accessor.Type.GETTER)
           UserModel getUser() {
               return getPersistenceContext().getPropertyValue(USER)
           }

           @Accessor(qualifier = USER, type = Accessor.Type.SETTER)
           void setUser()(final UserModel value) {
               getPersistenceContext().setPropertyValue(USER, value)
           }

       }

    ```

## All Tasks
- Run the command `./gradlew tasks --group easy` to see all the task available with this plugin

  ```
    Easy tasks
    ----------
    easy-class-gen - Generates an easy extension in current directory
    easy-dump-classpath - Dumps the classpath
    easy-ext-gen - Generates an easy extension in current directory
    easy-ext-install - Installs an easy extension
    easy-ext-list - Lists the easy extensions of a repository
    easy-ext-reinstall - Re-installs/reloads an easy extension
    easy-ext-uninstall - Uninstalls an easy extension
    easy-update-repo - Updates the repository from remote location

  ```
