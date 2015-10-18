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
package dist.test;

public enum TaskNames {

    GENERATE_TESTS("generateTestSources"),
    COPY_PROJECT_FILES("copyProjectFiles"),
    DOCKER_PREPARE("dockerPrepare"),
    DOCKER_COMPOSE("writeDockerCompose"),
    DOCKER_FILE("writeDockerfile"),
    DOCKER_BUILD("dockerBuild"),
    RUN_DOCKER("runDocker");

    private final String taskName;

    TaskNames(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }
}
