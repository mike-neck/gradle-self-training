/*
 * Copyright 2015 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dist.test.task

import dist.test.Names
import dist.test.model.JavaFile
import groovy.text.SimpleTemplateEngine
import org.gradle.api.DefaultTask
import org.gradle.api.GradleScriptException
import org.gradle.api.PathValidation
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import static dist.test.Util.template

class ClassGeneration extends DefaultTask {

    static final String BASE_PACKAGE = 'sample.tests'

    static final String TEMPLATE_NAME = 'class-file.template'

    private String destDir = 'src/test/java'

    private Collection<JavaFile> javaFiles

    @TaskAction
    void generate() {
        if (javaFiles == null) {
            setOutFile()
        }
        def srcDir = project.file("${destDir}/${BASE_PACKAGE.replace('.', '/')}")
        if (!srcDir.exists()) {
            srcDir.mkdirs()
        }
        javaFiles.each {
            def url = template(TEMPLATE_NAME)
            def map = [
                    type: it.typeName,
                    time: it.time,
                    packageName: it.packageName()
            ]
            def contents = new SimpleTemplateEngine().createTemplate(url).make(map)
            def writer = project.file(it.toFileName()).newWriter('UTF-8')
            contents.writeTo(writer)
        }
    }

    void setDestDir(String destDir) {
        this.destDir = destDir
        setOutFile()
    }

    private void setOutFile() throws GradleScriptException {
        if (!project.file(destDir, PathValidation.DIRECTORY).exists()) {
            def e = new FileNotFoundException(destDir)
            throw new GradleScriptException("Output directory not found.", e)
        }
        javaFiles = Names.values().collect {pkg ->
            Names.values().collect {type ->
                new JavaFile(destDir, pkg, type)
            }
        }.flatten()
    }

    @OutputFile
    FileCollection getJavaFile() {
        if (javaFiles == null) {
            setOutFile()
        }
        project.files(javaFiles.collect {
            it.toFileName()
        })
    }
}
