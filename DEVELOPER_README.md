# Documentation for developers

# How to build the plugin
- Install latest gradle version
- Clone this repository
- Run the command `gradle clean build `

The build will generate the following jar `build/libs/easy-plugin-x.x.x-SNAPSHOT.jar`

# How to test the plugin

In this example, we will create a new extension called `helloworld`:
- `mkdir helloworld`
- `cd helloworld`
- Run the command `gradle init --type basic --project-name helloworld`
- Create a `lib` folder and add `easy-plugin-x.x.x-SNAPSHOT.jar`
- Copy the following content into `build.gradle`

```
plugins {
  id 'io.github.yannickrobin.easyplugin' version 'x.x.x-SNAPSHOT'
}
```

To list available gradle tasks

```
./gradlew -q --group easy
```

# How to publish

- Update the version of the plugin to fit with the version you want to publish into `build.gradle`:

```
group 'io.github.yannickrobin'
version '0.0.4'
```

- Before publish in gradle portal, for testing purpose you can publish the plugin in your local maven repository or in internal SAP artifactory repository.

## publish to local maven repository

To publish on your local maven repository just run following gradle task:

```
./gradlew publishToMavenLocal
```

and check the artifacts are created under 

```
ls -lR ~/.m2/repository/io/github/yannickrobin/
```

To resolve the plugin in your gradle project you need to add mavenlocal to the repository list that is used to resolve the plugins, so in your project you need to add on top of your `settings.gradle` file:

```groovy
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
}
```

## Publish to internal SAP artifactory repository

You can also publish and resolve your plugin using SAP artifactory repository. There are two repositories you can use:
- internal repository under SAP VPN: https://int.repositories.cloud.sap/artifactory
- public repository: https://common.repositories.cloud.sap/artifactory

To publish the plugin 

```groovy
artifactory {
    publish {
        // Define the Artifactory URL for publishing artifacts
        contextUrl = 'https://int.repositories.cloud.sap/artifactory'
        // Define the project repository to which the artifacts will be published
        repository {
            // Set the Artifactory repository key
            repoKey = System.getenv('USER').uncapitalize()
            // Specify the publisher username
            username = System.getenv('USER')
            // Provide the publisher password
            password = sapIntRepositoryPassword
        }
        // Include all configured publications for all the modules
        defaults {
            publications('ALL_PUBLICATIONS')
        }
    }
}
```

Note that:
- you can only use your uncapitalized i-number as repo key
- to be checked how a shared repository can be set up
- `sapIntRepositoryPassword` is a token that is generated on [artifactory](https://common.repositories.cloud.sap/ui/user_profile) side an stored on you gradle home (ex: `~/.gradle/gradle.properties`)

Also in this case you need to add the repository on the project side on top of your `settings.gradle` file:

```groovy
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            url 'https://int.repositories.cloud.sap/artifactory/i309827/'
            credentials {
                username System.getenv('USER')
                password sapIntRepositoryPassword
            }
        }
    }
}
```

## Publish to Gradle plugin portal

> The version should not contain `-SNAPSHOT`.
> Once the plugin is published, you cannot override or remove the artifact so be very careful before publishing.

- Publish the plugin by using the `publishPlugin` task: `gradle publishPlugins -Pgradle.publish.key=<key> -Pgradle.publish.secret=<secret>`
