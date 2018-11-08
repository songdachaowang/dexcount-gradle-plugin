package com.getkeepsafe.dexcount;

import spock.lang.Specification

class DexFileTest extends Specification {
    def "test AAR dexcount"() {
        setup:
        def currentDir = new File(".").getAbsolutePath()

        when:
        def aarFile = new File(currentDir + File.separatorChar + "src"
                + File.separatorChar + "test" + File.separatorChar + "resources"
                + File.separatorChar + "android-beacon-library-2.7.aar")
        def url = getClass().getResource("./")
        def files = new File(url.toURI()).listFiles()
        for (file in files) {
            println(file.absolutePath)
        }
        if (!aarFile.exists()) {
            aarFile = File.createTempFile("test", ".aar")
            aarFile.deleteOnExit()
            def buf = new byte[4096]
            getClass().getResourceAsStream('resources/android-beacon-library-2.7.aar').withStream { input ->
                aarFile.withOutputStream { output ->
                    def read
                    while ((read = input.read(buf)) != -1) {
                        output.write(buf, 0, read)
                    }
                    output.flush()
                }
            }
        }
        def dexFiles = DexFile.extractDexData(aarFile)

        then:
        dexFiles != null
        dexFiles.size() == 1
        dexFiles[0].methodRefs.size() == 982
        dexFiles[0].fieldRefs.size() == 436
    }
}
