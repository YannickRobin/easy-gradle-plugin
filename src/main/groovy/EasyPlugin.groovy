import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property

interface EasyPluginExtension {
    Property<String> getExtensionName()
}

class EasyPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.task('create') {
            def extension = project.extensions.create('greeting', EasyPluginExtension)
            extension.extensionName.convention('myEasyExtension')
            doLast {
                println "Create extension '${extension.extensionName.get()}' ..."
            }
        }
    }
}