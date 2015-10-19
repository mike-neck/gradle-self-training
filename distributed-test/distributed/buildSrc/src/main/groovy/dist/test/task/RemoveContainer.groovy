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

import dist.test.model.Groups
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

class RemoveContainer extends DefaultTask {

    private final ExecutorService executorService = Executors.newFixedThreadPool(
            Groups.values().size() + 1,
            [newThread: {Runnable r ->
                def thread = new Thread(r)
                thread.daemon = true
                thread
            }] as ThreadFactory)

    @TaskAction
    void dockerRmContainer() {
        def exe = executorService
        def commands = Groups.values().collect {
            [cmd: "docker rm ${it.runningName}", containerName: it.runningName]
        }
        def log = project.logger
        def tasks = commands.collect {cmd ->
            [call: {
                def process = cmd.cmd.execute()
                log.info "Removing container[${cmd.containerName}]"
                process.waitFor()
            }] as Callable<Integer>
        }
        def futures = tasks.collect {
            exe.submit(it)
        }
        def failures = futures.collect {
            it.get()
        }.findAll {
            it != 0
        }
        if (failures.size() > 0) {
            logger.lifecycle 'While removing container, some error has occurred. Please remove container manually.'
        }
        exe.shutdown()
    }

    private static String dockerRm(Groups grp) {
        "docker rm ${grp.runningName}"
    }
}
