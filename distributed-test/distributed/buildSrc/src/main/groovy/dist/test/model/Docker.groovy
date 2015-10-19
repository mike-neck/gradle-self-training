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
package dist.test.model

import org.gradle.api.Project

final class Docker {

    private Docker(){}

    static final String DIR_NAME = 'docker'

    static final String IMAGE_NAME = 'gradle-dist-test'

    static String dockerDir(Project project) {
        "${project.buildDir}/${DIR_NAME}"
    }

    static String dockerChildDir(Project project, Groups child) {
        "${project.buildDir}/${DIR_NAME}/${child.toString().toLowerCase()}"
    }

    static void prepareDockerDir(Project project) {
        def file = {def fileName ->
            project.file(fileName)
        }
        def dir = file(dockerDir(project))
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    static void prepareDockerChildDir(Project project, Groups child) {
        def file = {def fileName ->
            project.file(fileName)
        }
        def dir = file(dockerChildDir(project, child))
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }
}
