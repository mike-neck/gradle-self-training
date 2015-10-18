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
import dist.test.util.TestTaskCreator
import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginImpl implements Plugin<Project> {

    @Override
    void apply(Project project) {
        if (!project.pluginManager.hasPlugin('java')) {
            project.pluginManager.apply('java')
        }
        configureClassGeneration(project)
        def testTasks = Names.values().collect {
            new TestTaskCreator(project, it)
        }
        testTasks.each {it.doWork()}
    }

    protected void configureClassGeneration(Project project) {
        def task = project.tasks.create('generateTestSources', ClassGeneration)
        project.tasks.findByPath('compileTestJava').dependsOn task
    }
}
