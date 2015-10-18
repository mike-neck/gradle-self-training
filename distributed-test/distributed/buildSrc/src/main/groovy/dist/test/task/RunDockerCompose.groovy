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
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

import java.util.concurrent.BlockingQueue
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory

import static dist.test.model.Docker.dockerChildDir

class RunDockerCompose extends DefaultTask {

    private final ExecutorService exec = Executors.newFixedThreadPool(
            Names.values().size() + 1, [newThread: {Runnable r ->
        def thread = new Thread(r)
        thread.daemon = true
        thread
    }] as ThreadFactory)

    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>()

    @TaskAction
    void build() {
        def execService = exec
        def blockingQueue = queue
        exec.submit([run: {
            while (true) {
                def stdOut = blockingQueue.take()
                logger.lifecycle(stdOut)
            }
        }] as Runnable)
        def results = Names.values().collect {Names n ->
            [
                    callable: [call: {
                        def command = "docker-compose -f ${dockerChildDir(project, n)}/docker-compose.yml up"
                        logger.info("Calling command [${command}].")
                        def process = command.execute()
                        blockingQueue.put(process.text)
                        process.waitFor()
                    }] as Callable<Integer>,
                    names: n
            ]
        }.collect {
            Future<Integer> future = execService.submit(it.callable)
            [future: future, names: it.names]
        }
        def failed = results.collect {
            [exit: it.future.get(), names: it.names]
        }.findAll {
            it.exit != 0
        }
        if (failed.size() > 0) {
            def tests = failed.collect {
                "${it.names.breeds}Test[exit value: ${it.exit}]"
            }.each {
                logger.info it
            }.join(', ')
            exec.shutdown()
            throw new GradleException("Tests [${tests}] failed.")
        }
        exec.shutdown()
    }
}
