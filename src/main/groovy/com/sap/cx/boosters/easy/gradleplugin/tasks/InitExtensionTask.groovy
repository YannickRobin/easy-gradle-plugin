package com.sap.cx.boosters.easy.gradleplugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input

import groovyx.net.http.RESTClient

import com.sap.cx.boosters.easy.gradleplugin.EasyPluginExtension
import com.sap.cx.boosters.easy.gradleplugin.EasyPluginUtil

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

class InitExtensionTask extends AbstractEasyTask {

    @Input
    EasyPluginExtension easyConfig

    @TaskAction
    void initExtension() {
        init("Init extension...") 

        def targetDir = project.file(project.name)

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("projectName", project.name);

        URI resource = getClass().getResource("easy-extension-template").toURI();
        println "resource: " + resource;
        def templateDir = new File(resource.getPath());
        println "templateDir: " + templateDir;

        initBuildGradleFile(templateDir, targetDir, parameters);
        initImpexFiles(templateDir, targetDir, parameters);
        initEasyTypes(templateDir, targetDir, parameters);
    }

    void initBuildGradleFile(File templateDir, File targetDir, HashMap<String, Object> parameters) {    
        def targetFile = new File(targetDir, "build.gradle")
        Writer writer = new FileWriter(targetFile);
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("build.gradle");
        mustache.execute(writer, parameters);
        writer.flush();
    }

    void initImpexFiles(File templateDir, File targetDir, HashMap<String, Object> parameters) {
        templateDir.eachFileRecurse { file ->
            if (file.isFile()) {
                println "file: " + file
            }
            else if(file.isDirectory()) {
                println "directory: " + file
                //file.mkdirs()
            }
        }   
    }    

    void initEasyTypes(File templateDir, File targetDir, HashMap<String, Object> parameters) 
    {
        def targetFile = new File(targetDir, "easytypes.json")
        Writer writer = new FileWriter(targetFile);
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("easytypes.json");
        mustache.execute(writer, parameters);
        writer.flush();   
    }

        /*
        def templateEngine = new MustacheTemplateEngine()

        // Scaffold project files
        def template = templateEngine.compileTemplate('src/main/resources/easy-extension-template')
        def renderedProjectFiles = template.render(['projectName': projectName])
        def targetFile = new File(targetDir, 'build.gradle')
        targetFile.text = renderedProjectFiles
        */

/*
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", "Mustache");

        Writer writer = new OutputStreamWriter(System.out);
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader("Hello {{name}}!"), "example");
        mustache.execute(writer, parameters);
        writer.flush();
*/
        

        // Scaffold directory
        /*
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