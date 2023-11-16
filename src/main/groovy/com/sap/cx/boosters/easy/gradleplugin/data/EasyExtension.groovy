package com.sap.cx.boosters.easy.gradleplugin.data

import org.apache.commons.lang.StringUtils

class EasyExtension {
    String groupId = StringUtils.EMPTY
    String id = StringUtils.EMPTY
    String name = StringUtils.EMPTY
    String version = '0.0.1'
    String easyVersion = StringUtils.EMPTY
    String description = StringUtils.EMPTY
    String thumbnail = StringUtils.EMPTY
    String documentation = StringUtils.EMPTY
    String groovyMainPath = 'src/main/groovy'
    String groovyTestPath = 'src/test/groovy'
    String init = 'Init.groovy'
    Set<String> authors = new HashSet<>()
    EasyExtensionRequires requires = new EasyExtensionRequires()
    Set<String> tags = new HashSet<>()
    String vendor = 'SAP'

    String getGroupId() {
        return groupId
    }

    void setGroupId(String groupId) {
        this.groupId = groupId
    }

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    String getVersion() {
        return version
    }

    void setVersion(String version) {
        this.version = version
    }

    String getEasyVersion() {
        return easyVersion
    }

    void setEasyVersion(String easyVersion) {
        this.easyVersion = easyVersion
    }

    String getDescription() {
        return description
    }

    void setDescription(String description) {
        this.description = description
    }

    String getThumbnail() {
        return thumbnail
    }

    void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail
    }

    String getDocumentation() {
        return documentation
    }

    void setDocumentation(String documentation) {
        this.documentation = documentation
    }

    String getGroovyMainPath() {
        return groovyMainPath
    }

    void setGroovyMainPath(String groovyMainPath) {
        this.groovyMainPath = groovyMainPath
    }

    String getGroovyTestPath() {
        return groovyTestPath
    }

    void setGroovyTestPath(String groovyTestPath) {
        this.groovyTestPath = groovyTestPath
    }

    String getInit() {
        return init
    }

    void setInit(String init) {
        this.init = init
    }

    Set<String> getAuthors() {
        return authors
    }

    void setAuthors(Set<String> authors) {
        this.authors = authors
    }

    EasyExtensionRequires getRequires() {
        return requires
    }

    void setRequires(EasyExtensionRequires requires) {
        this.requires = requires
    }

    Set<String> getTags() {
        return tags
    }

    void setTags(Set<String> tags) {
        this.tags = tags
    }

    String getVendor() {
        return vendor
    }

    void setVendor(String vendor) {
        this.vendor = vendor
    }
}
