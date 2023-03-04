import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import groovyx.net.http.RESTClient

interface EasyPluginExtension {
    Property<String> getBaseUrl()
    Property<String> getRepository()
    Property<String> getExtension()
}

class EasyPlugin implements Plugin<Project> {
    void apply(Project project) {
        def easyConfig = project.extensions.create('easyConfig', EasyPluginExtension)
        easyConfig.baseUrl.convention('https://localhost:9002')
        easyConfig.repository.convention('easy-extension-samples')        
        easyConfig.extension.convention('helloWorld')

        println "Welcome to Easy Gradle Plugin\n"
        println "SAP Commerce Base URL: ${easyConfig.baseUrl.get()}"
        println "Repository: ${easyConfig.repository.get()}"
        println "Extension: ${easyConfig.extension.get()}\n"

        project.task('help2') {
            doLast {
                println "The following tasks are available:"
                println "create - Create a new extension"
                println "list - List existing extensions"
                println "update - Update the repository"                
                println "install - Install the extension"
                println "uninstall - Uninstall the extension"                        
            }
        }

        project.task('createExtension') {
            doLast {
                println "Create extension '${easyConfig.extension.get()}' ..."
                println "Not implemented yet..."
            }
        }
        project.task('list') {
            doLast {
                println "List extensions..."

                def restClient = new RESTClient(easyConfig.baseUrl.get())
                restClient.ignoreSSLIssues()
                restClient.handler.failure = {def response, def data ->
                    println "API call failed. HTTP status: $response.status";
                    println "Error is $data";
                }
                restClient.handler.success = {def response, def data ->
                    println "API call successfull. HTTP status: $response.status"
                    println "$data"
                }        

                restClient.get(path: '/easyrest/easyapi/repository/' + easyConfig.repository.get() + '/extensions')   
            }
        }        
        project.task('update') {
            doLast {
                println "Update repository..."
            }
        }        
    }
}