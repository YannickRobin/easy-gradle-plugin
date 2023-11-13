package com.sap.cx.boosters.easy.gradleplugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input

import groovyx.net.http.RESTClient

import com.sap.cx.boosters.easy.gradleplugin.EasyPluginExtension
import com.sap.cx.boosters.easy.gradleplugin.EasyPluginUtil

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory

import java.nio.file.Files
import java.nio.file.Path;

class InitExtensionTask extends AbstractEasyTask {

    @Input
    EasyPluginExtension easyConfig

    @TaskAction
    void initExtension() {

        init('Init extension...')

        def templateResourceRoot = 'easy-extension-template'

        def parameters = new HashMap<String, Object>()
        parameters.put('projectName', project.name)

        def templateURL = getClass().classLoader.getResource(templateResourceRoot)

        def templateDir

        if (templateURL.protocol == 'jar') {
            // REVIEWME
            def tokens = new URL(templateURL.file).file.split('!')
            def jarFile = new File(tokens[0])
            def dirInJar = tokens[1]
            println "jar: ${jarFile.absolutePath} dir: ${dirInJar}"
            def tmpDir = Files.createTempDirectory('easy-plugin').toFile()
            // def tmpDir = new File(System.getProperty('java.io.tmpdir','easy-plugin'))
            EasyPluginUtil.unzip(jarFile,tmpDir)
            templateDir = new File(tmpDir,dirInJar)
        } else if (templateURL.protocol == 'file') {
            println "file: ${templateURL.file}"
            templateDir = new File(templateURL.file)
        } else {
            throw new RuntimeException("Unexpected protocol ${templateURL.protocol} for resource ${templateResourceRoot}")
        }

        println "templateDir: ${templateDir}"

        // initBuildGradleFile(templateDir, project.projectDir, parameters)
        // initImpexFiles(templateDir, project.projectDir, parameters)
        // initEasyTypes(templateDir, project.projectDir, parameters)

        // Files.copy(Path.of(templateDir.toURI()),Path.of(project.projectDir.toURI()))

        templateDir.traverse() {
            def type = it.isDirectory() ? 'd' : 'f'
            File.separator
            println(type + ' ' + (it.absolutePath - (templateDir.absolutePath + File.separator)))
        }

    }

    // REVIEWME: groovy has it's own template engine no need to use this com.github.mustachejava probably

    /*

    void initBuildGradleFile(File templateDir, File targetDir, HashMap<String, Object> parameters) {    
        def targetFile = new File(targetDir, 'build.gradle')
        def writer = new FileWriter(targetFile);
        def mf = new DefaultMustacheFactory()
        def mustache = mf.compile('build.gradle')
        mustache.execute(writer, parameters)
        writer.flush()
    }

    void initImpexFiles(File templateDir, File targetDir, HashMap<String, Object> parameters) {
        templateDir.eachFileRecurse { file ->
            if (file.isFile()) {
                println "file: " + file
            } else if(file.isDirectory()) {
                println "directory: " + file
                //file.mkdirs()
            }
        }   
    }    

    void initEasyTypes(File templateDir, File targetDir, HashMap<String, Object> parameters) {
        def targetFile = new File(targetDir, 'easytypes.json')
        Writer writer = new FileWriter(targetFile)
        MustacheFactory mf = new DefaultMustacheFactory()
        Mustache mustache = mf.compile('easytypes.json')
        mustache.execute(writer, parameters)
        writer.flush()
    }

    */

    /*

    def templateEngine = new MustacheTemplateEngine()

    // Scaffold project files
    def template = templateEngine.compileTemplate('src/main/resources/easy-extension-template')
    def renderedProjectFiles = template.render(['projectName': projectName])
    def targetFile = new File(targetDir, 'build.gradle')
    targetFile.text = renderedProjectFiles

    HashMap<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("name", "Mustache");

    Writer writer = new OutputStreamWriter(System.out);
    MustacheFactory mf = new DefaultMustacheFactory();
    Mustache mustache = mf.compile(new StringReader("Hello {{name}}!"), "example");
    mustache.execute(writer, parameters);
    writer.flush();

    // Scaffold directory

    def templateDir = templateEngine.compileTemplate('src/main/resources/easy-extension-template')
    def targetDirTemp = new File(targetDir, 'src/main/groovy')
    targetDirTemp.mkdirs()
    targetDirTemp.eachFileRecurse { file ->
        if (file.isFile()) {
            def renderedProjectDir = templateDir.render(['packageName': 'com.example'])
            def targetFileProjectDir = new File(targetDirTemp, file.name)
            targetFileProjectDir.text = renderedProjectDir
        }
    }

    */

}