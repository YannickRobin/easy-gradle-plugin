# easy-gradle-plugin

This is a Gradle plugin for Easy Extension Framework for SAP Commerce.

The objective of this plugin is to:
- Create an Easy extension by scaffolding
- Install/update/unstall extension use Easy REST API
- Run Groovy Tests
- Generate Easy Type models

# How to test the plugin
  Below are the examples to test the plugin

## Preparation
Execute the following pre-requisites.
- Install [Gradle](https://gradle.org/install/)
- Create your global gradle configuration properties file:
  - Unix/MacOS - `~/.gradle/gradle.properties`
  - Windows - `<WINDOWS_DRIVE>:\Users\<USER_NAME>\.gradle\gradle.properties`
- Configure you gradle environment for `easy-gradle-plugin` by adding the following properties to global `gradle.properties`
  ```properties
  # The base url of the easy rest for your SAP Commerce Cloud. Below if for a local SAP Commerce Cloud Server
  sap.commerce.easy.rest.base.url = https://localhost:9002/easyrest

  # The base url of the easy api for your SAP Commerce Cloud. Below if for a local SAP Commerce Cloud Server
  sap.commerce.easy.api.base.url = https://localhost:9002/easyrest/easyapi

  # Value of easy.apiKey property configured in SAP Commerce Cloud (by default, this is 123456)
  sap.commerce.easy.api.key = 123456
  
  # Path to the hybris home directory on your computer to add SAP Commerce Cloud libraries to your extension
  commercePlatformHome=<SAP COMMERCE HOME>
  ```

## Configure your local Easy Repository
In this example, we will create an easy local repository `easy-sample-repo` that represents a by a local directory in easy extension framework:
- `mkdir easy-sample-repo`
- `cd easy-sample-repo`
- Configure the easy repository in SAP Commerce Cloud backoffice as per instructions in [Configuring a Local Repository Documentation](https://sap.github.io/easy-extension-framework/configuring-an-easy-repository-in-backoffice/#configuring-a-local-repository)
- Run the command `gradle init --type basic --dsl groovy --no-incubating --project-name easy-sample-repo`
- Add the following content in `settings.gradle` file as the **_first line_**
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
        id "io.github.yannickrobin.easy-gradle-plugin" version "0.0.4"
    }
    ```
- Create file `gradle.properties` in `easy-sample-repo` directory with following content
    ```properties
  
    # The easy version deployed on your SAP Commerce Cloud instance
    sap.commerce.easy.extension.easy.version = 0.2
    
    # Base package for your easy extension
    sap.commerce.easy.extension.base.package=com.sap.cx.boosters.easy.extension
    
    # The value of code attribute of the easy repository configured in your SAP Commerce Cloud instance
    sap.commerce.easy.repository.code=easy-sample-repo
    ```

## Generate an Easy Extension
In this example, we will generate an easy extension `helloworld` in local repository `easy-sample-repo`:
- Run the command `./gradlew easy-ext-gen -PeasyExtensionId=helloworld`
  ```
  > Task :easy-ext-gen
  Generating new easy extension in current directory

  BUILD SUCCESSFUL in 1s
  1 actionable task: 1 executed
  ```

> You can  now implement the extension as per the instructions available at [Easy Extension Documentation](https://sap.github.io/easy-extension-framework/easy-extension) 

## Generate Model and Enum classes for Easy Types
### Prerequisites
  - Define the Easy type definitions in `easytype.json` file, for example:
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
  - Go to the extension directory
    ```shell
      ./gradlew easy-class-gen
    ```
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
