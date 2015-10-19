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
package dist.test.util

import dist.test.model.Groups
import dist.test.model.Names
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class TestTaskCreator {

    static final String GROUP = 'distributed test'

    static String baseDirName(Project prj) {
        "${prj.buildDir}/dist-test"
    }

    private final Project project

    private final Groups grp;

    TestTaskCreator(Project project, Groups grp) {
        this.project = project
        this.grp = grp
    }

    private File file(Object name) {
        project.file(name)
    }

    void doWork() {
        def reportBaseDir = "${baseDirName(project)}/${grp.lowerCase}"

        def testTask = project.tasks.create(grp.testTaskName, Test)
        testTask.configure {
            group = GROUP
            def names = Names.byGroup(grp)
            description = "Runs tests under [${names.collect {it.breeds}.join(', ')}] package."
            include names.collect {"**/${it.breeds}/**/*"}
            maxParallelForks = 2
            reports.html.destination = "${reportBaseDir}/html"
            reports.junitXml.destination = "${reportBaseDir}/xml"
            binResultsDir = file("${reportBaseDir}/bin")
        }
        project.tasks.findByName('test').configure {
            exclude Names.byGroup(grp).collect {"**/${it.breeds}/**/*"}
        }
    }
}
