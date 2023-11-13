package com.sap.cx.boosters.easy.gradleplugin

import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class EasyPluginUtil {

    static void displayEasyConfigInfo(EasyPluginExtension easyConfig) {
        println "Welcome to Easy Gradle Plugin\n"
        println "SAP Commerce Base URL: ${easyConfig.baseUrl.get()}"
        println "Repository: ${easyConfig.repository.get()}"
        println "Extension: ${easyConfig.extension.get()}\n"
    }

    static void unzip(File fileZip, File destDir) throws IOException {

        // def fileZip = "src/main/resources/unzipTest/compressed.zip"
        // def  destDir = new File("src/main/resources/unzipTest")

        final byte[] buffer = new byte[1024]
        def zis = new ZipInputStream(new FileInputStream(fileZip))
        ZipEntry zipEntry = zis.getNextEntry()
        while (zipEntry != null) {
            final File newFile = newFile(destDir, zipEntry)
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory ${newFile}")
                }
            } else {
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory ${parent}")
                }
                final FileOutputStream fos = new FileOutputStream(newFile)
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len)
                }
                fos.close()
            }
            zipEntry = zis.getNextEntry()
        }
        zis.closeEntry()
        zis.close()

    }

    /**
     * @see https://snyk.io/research/zip-slip-vulnerability
     */
    static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {

        def destFile = new File(destinationDir, zipEntry.name)
        def destDirPath = destinationDir.canonicalPath
        def destFilePath = destFile.canonicalPath
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: ${zipEntry.name}")
        }
        return destFile

    }

}