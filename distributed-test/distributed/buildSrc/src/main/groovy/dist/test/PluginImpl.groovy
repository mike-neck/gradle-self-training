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
import dist.test.task.RemoveContainer
import dist.test.task.RunDockerCompose
import dist.test.task.RunTestOnDocker
import dist.test.util.TestTaskCreator
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.testing.TestReport

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
        configureCopyProjectFiles(project)

        // create test results directory
        configureCreateTestResultsDir(project)

        // Dockerfile
        project.tasks.create(TaskNames.DOCKER_FILE.taskName, DockerFile)

        // docker build
        project.tasks.create(TaskNames.DOCKER_BUILD.taskName, Exec).configure {
            dependsOn TaskNames.COPY_PROJECT_FILES.taskName, TaskNames.DOCKER_FILE.taskName, TaskNames.CREATE_TEST_RESULTS_DIR.taskName
            workingDir Docker.dockerDir(project)
            commandLine 'docker', 'build', '-t', Docker.IMAGE_NAME, '.'
        }

        // prepare docker
        project.tasks.create(TaskNames.DOCKER_PREPARE.taskName) {
            dependsOn TaskNames.DOCKER_BUILD.taskName
        }

        // remove container
        project.tasks.create(TaskNames.REMOVE_CONTAINER.taskName, RemoveContainer).configure {
            def containerNames = Groups.values().collect { it.runningName }.join(', ')
            description = "Remove docker containers created by distributed test[${containerNames}]."
        }

        // run docker
        project.tasks.create(TaskNames.RUN_TEST_ON_DOCKER.taskName, RunTestOnDocker).configure {
            description = 'Run test int parallel with docker containers.'
            dependsOn TaskNames.DOCKER_PREPARE.taskName
            finalizedBy TaskNames.REMOVE_CONTAINER.taskName
        }

        // summarize test results
        project.tasks.create(TaskNames.TEST_REPORT.taskName, TestReport).configure {
            dependsOn TaskNames.RUN_TEST_ON_DOCKER.taskName
            testResultDirs = Groups.values().collect {
                project.file("${TestTaskCreator.baseDirName(project)}/${it.lowerCase}/bin")
            }
            destinationDir = project.file("${project.buildDir}/test-report")
        }

        // bundle task graph
        project.tasks.create(TaskNames.DISTRIBUTE_TEST.taskName).configure {
            dependsOn TaskNames.TEST_REPORT.taskName
            group = 'distributed test'
        }
    }

    private static Task configureCreateTestResultsDir(Project project) {
        project.tasks.create(TaskNames.CREATE_TEST_RESULTS_DIR.taskName).configure {
            def dir = project.file(TestTaskCreator.baseDirName(project))
            outputs.upToDateWhen {
                dir.exists()
            }
            doLast {
                if (!dir.exists()) {
                    dir.mkdirs()
                }
            }
        }
    }

    private static Task configureCopyProjectFiles(Project project) {
        project.tasks.create(TaskNames.COPY_PROJECT_FILES.taskName, Copy).configure {
            from project.files('build.gradle', 'settings.gradle', 'gradle.properties')
            from('buildSrc') {
                exclude '.gradle/'
                exclude 'build/'
                exclude 'out/'
                exclude 'buildSrc.i*'
                into 'buildSrc'
            }
            from('src/') {
                includeEmptyDirs = false
                into 'src'
            }
            into Docker.dockerDir(project)
        }
    }

    private static void configureClassGeneration(Project project) {
        def task = project.tasks.create(TaskNames.GENERATE_TESTS.taskName, ClassGeneration)
        task.configure {
            def list = getJavaFiles()
            def existing = list.findAll {
                it.exists()
            }
            outputs.upToDateWhen {
                task.logger.info "Java Files[expected: ${list.size()}, actual: ${existing.size()}]"
                list.size() == existing.size()
            }
        }
        project.tasks.findByPath('compileTestJava').dependsOn task
    }
}
