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
import groovy.text.SimpleTemplateEngine
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import static dist.test.Util.template
import static dist.test.model.Docker.dockerChildDir
import static dist.test.model.Docker.prepareDockerChildDir

class DockerCompose extends DefaultTask {

    static final String DOCKER_COMPOSE = 'docker-compose.yml'

    static final String TEMPLATE = 'docker-compose.template'

    @TaskAction
    void writeDockerCompose() {
        Names.values().each {
            prepareDockerChildDir(project, it)
            def url = template(TEMPLATE)
            def map = [
                    container: it.breeds,
                    taskName: it.taskName
            ]
            def contents = new SimpleTemplateEngine().createTemplate(url).make(map)
            def writer = project.file("${dockerChildDir(project, it)}/${DOCKER_COMPOSE}").newWriter('UTF-8')
            contents.writeTo(writer)
        }
    }
}
