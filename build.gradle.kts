plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version "1.9.0"
    id("com.gradle.plugin-publish") version "1.1.0"
}

group = "com.sap.cx.boosters.easy"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
}

dependencies {
    implementation(gradleApi())
    implementation("org.jsonschema2pojo:jsonschema2pojo-core:1.2.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
    implementation("org.codehaus.groovy.modules.http-builder:http-builder:0.7.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

gradlePlugin {
    
    plugins {
        create("easy-gradle-plugin") {
            id = "${group}.${rootProject.name}"
            displayName = "Easy Gradle Plugin"
            description = "This plugin is used to manage easy extensions as part of Easy Extension Framework for SAP Commerce Cloud"
            tags = listOf("SAP Commerce", "SAP Commerce Cloud", "Easy", "Easy Extension Framework")
            implementationClass = "com.sap.cx.boosters.easy.plugin.SAPCommerceEasyPlugin"
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    from("templates")
}
