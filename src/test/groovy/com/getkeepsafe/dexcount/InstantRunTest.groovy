package com.getkeepsafe.dexcount

import com.android.build.gradle.api.BaseVariantOutput
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class InstantRunTest extends Specification {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder()

    def "APKs with instant-run.zip are counted correctly"() {
        setup:
        def apkFile = tempFolder.newFile("tiles.apk")
        def apkResource = getClass().getResourceAsStream("/tiles.apk")
        apkResource.withStream { input ->
            IOUtil.drainToFile(input, apkFile)
        }

        // Ugh why is Gradle so hard to test
        def project = ProjectBuilder.builder().build()
        def task = project.tasks.create('countDexMethods', DexMethodCountTask)
        task.config = new DexMethodCountExtension()
        task.apkOrDex = Mock(BaseVariantOutput)
        task.apkOrDex.outputFile >> apkFile

        when:
        task.generatePackageTree()

        then:
        task.tree.methodCount != 32446 // If this fails, then method deduplication has failed.
        task.tree.methodCount == 29363 // determined ahead-of-time; if this fails, something else is broken.
    }
}
