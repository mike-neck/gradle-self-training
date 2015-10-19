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

import dist.test.model.CommandAndTask
import dist.test.model.Docker
import dist.test.model.Groups
import dist.test.model.TaskResult
import dist.test.util.TestTaskCreator
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ThreadFactory

class RunTestOnDocker extends DefaultTask {

    private final ExecutorService execService = Executors.newFixedThreadPool(
            Groups.values().size() + 1,
            [newThread: {Runnable r ->
                def thread = new Thread(r)
                thread.daemon = true
                thread
            }] as ThreadFactory)

    static final String CACHE_FILE_DIR = 'caches/modules-2/files-2.1'

    static final String CACHE_METADATA_DIR = 'caches/modules-2/metadata-2.15'

    static final String DOCKER_HOME = '/home/gradle'

    static final String DOCKER_PS = 'docker ps -q'

    static final String BUILD_SUCCESS = 'BUILD SUCCESSFUL'

    @TaskAction
    void launchAndWaitDocker() {
        def exe = execService
        def commands = configureDockerCommand()
        def tasks = convertToTasks(commands, project.logger)
        def futures = tasks.collect {
            exe.submit(it)
        }
        def failures = hasFailure(futures)
        if (failures.size() > 0) {
            def failedTasks = failures.collect {
                it.groups.testTaskName
            }

            def msg = "Tests[${failedTasks.join(', ')}] failed."
            logger.lifecycle msg
            exe.shutdown()
            throw new GradleException(msg)
        }
        exe.shutdown()
    }

    private static List<TaskResult> hasFailure(List<Future<TaskResult>> futures) {
        futures.collect {
            it.get()
        }.findAll {
            !it.success
        }
    }

    private static List<Callable<TaskResult>> convertToTasks(List<CommandAndTask> commands, def log) {
        commands.collect { CommandAndTask cmd ->
            [call: {
                def longId = cmd.command.execute().text
                log.info "Runnting ${cmd.groups.testTaskName} on docker container ${longId}."
                def shortId = longId.substring(0, 9)
                int count = 0
                while (DOCKER_PS.execute().text.contains(shortId)) {
                    Thread.sleep(1000l)
                    if (count++ % 30 == 0) {
                        log.debug "${cmd.groups.testTaskName} is being executed."
                    }
                }
                log.info "Finished ${cmd.groups.testTaskName}."
                def succeeded = dockerLogs(shortId).contains(BUILD_SUCCESS)
                return new TaskResult(cmd.groups, succeeded)
            }] as Callable<TaskResult>
        }
    }

    private List<CommandAndTask> configureDockerCommand() {
        Groups.values().collect {
            new CommandAndTask(RunTestOnDocker.dockerRun(project, it), it)
        }
    }

    private static String dockerRun(Project project, Groups grp) {
        def resultDir = TestTaskCreator.baseDirName(project)
        def dir = project.projectDir
        "docker run --name ${grp.runningName} -d -v ${dir}/${CACHE_FILE_DIR}:${DOCKER_HOME}/.gradle/${CACHE_FILE_DIR} -v ${dir}/${CACHE_METADATA_DIR}:${DOCKER_HOME}/.gradle/${CACHE_METADATA_DIR} -v ${resultDir}:${DOCKER_HOME}/project/build/${TestTaskCreator.RESULT_DIR} ${Docker.IMAGE_NAME} gradle ${grp.testTaskName}"
    }

    private static String dockerLogs(String id) {
        "docker logs ${id}".execute().text
    }
}
