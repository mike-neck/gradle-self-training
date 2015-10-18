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

import dist.test.model.Docker
import dist.test.model.Groups
import dist.test.task.ClassGeneration
import dist.test.task.DockerCompose
import dist.test.task.DockerFile
import dist.test.task.RunDockerCompose
import dist.test.util.TestTaskCreator
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec

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
        def testTasks = Groups.values().collect {
            new TestTaskCreator(project, it)
        }
        testTasks.each {it.doWork()}

        // copy project files
        project.tasks.create(TaskNames.COPY_PROJECT_FILES.taskName, Copy).configure {
            from project.files('build.gradle', 'settings.gradle', 'gradle.properties')
            from('buildSrc') {
                exclude './gradle/**/*'
                exclude 'out/**/*'
                into 'buildSrc'
            }
            into Docker.dockerDir(project)
        }

        // Dockerfile
        project.tasks.create(TaskNames.DOCKER_FILE.taskName, DockerFile)

        // docker build
        project.tasks.create(TaskNames.DOCKER_BUILD.taskName, Exec).configure {
            dependsOn TaskNames.COPY_PROJECT_FILES.taskName, TaskNames.DOCKER_FILE.taskName
            workingDir Docker.dockerDir(project)
            commandLine 'docker', 'build', '-t', 'gradle-dist-test', '.'
        }

        // docker-compose.yml
        project.tasks.create(TaskNames.DOCKER_COMPOSE.taskName, DockerCompose)

        // prepare docker
        project.tasks.create(TaskNames.DOCKER_PREPARE.taskName) {
            dependsOn TaskNames.DOCKER_BUILD.taskName, TaskNames.DOCKER_COMPOSE.taskName
        }

        // run docker-compose
        project.tasks.create(TaskNames.RUN_DOCKER.taskName, RunDockerCompose).configure {
            description = 'Run tests in parallel with docker containers'
            group = 'distributed test'
            dependsOn project.tasks.findByName('testClasses'), TaskNames.DOCKER_PREPARE.taskName
        }

        // summarize test results
        // bundle task graph
    }

    private static void configureClassGeneration(Project project) {
        def task = project.tasks.create(TaskNames.GENERATE_TESTS.taskName, ClassGeneration)
        project.tasks.findByPath('compileTestJava').dependsOn task
    }
}
