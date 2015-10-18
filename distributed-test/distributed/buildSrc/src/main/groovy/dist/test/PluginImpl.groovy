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
package dist.test

import dist.test.task.ClassGeneration
import dist.test.task.DockerCompose
import dist.test.task.DockerFile
import dist.test.util.TestTaskCreator
import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginImpl implements Plugin<Project> {

    static final String DOCKER = 'dockerPrepare'

    @Override
    void apply(Project project) {
        if (!project.pluginManager.hasPlugin('java')) {
            project.pluginManager.apply('java')
        }

        // class generation
        configureClassGeneration(project)

        // distributed test tasks
        def testTasks = Names.values().collect {
            new TestTaskCreator(project, it)
        }
        testTasks.each {it.doWork()}

        // docker-compose.yml
        project.tasks.create(TaskNames.DOCKER_COMPOSE.taskName, DockerCompose)

        // Dockerfile
        project.tasks.create(TaskNames.DOCKER_FILE.taskName, DockerFile)

        // prepare docker
        project.tasks.create(TaskNames.DOCKER_PREPARE.taskName) {
            dependsOn TaskNames.DOCKER_COMPOSE.taskName, TaskNames.DOCKER_FILE.taskName
        }

        // run docker-compose
        // summarize test results
        // bundle task graph
    }

    protected void configureClassGeneration(Project project) {
        def task = project.tasks.create(TaskNames.GENERATE_TESTS.taskName, ClassGeneration)
        project.tasks.findByPath('compileTestJava').dependsOn task
    }
}
