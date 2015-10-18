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

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import static dist.test.Util.template
import static dist.test.model.Docker.dockerDir
import static dist.test.model.Docker.prepareDockerDir

class DockerFile extends DefaultTask {

    static final String DOCKER_FILE = 'Dockerfile'

    @OutputFile
    File getOutputFile() {
        def outputFile = getOutputFileName()
        file(outputFile)
    }

    private String getOutputFileName() {
        "${dockerDir(project)}/${DOCKER_FILE}"
    }

    private File file(Object fileName) {
        project.file(fileName)
    }

    @TaskAction
    void writeDockerFile() {
        def url = template(DOCKER_FILE)
        prepareDockerDir(project)
        this.outputFile.write(url.text, 'UTF-8')
    }
}
