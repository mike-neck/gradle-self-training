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
package test;

import test.exec.ExecutionManager;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class TestSuite {

    private final Set<Class<Test>> testClasses;

    private TestSuite(Set<Class<Test>> testClasses) {
        if (testClasses == null || testClasses.size() == 0) throw new IllegalArgumentException("Test classes is null or empty.");
        this.testClasses = testClasses;
    }

    public static void run(Class<Test>... testClasses) {
        Set<Class<Test>> set = Stream.of(testClasses)
                .filter(t -> t != null)
                .collect(toSet());
        TestSuite suite = new TestSuite(set);
        suite.executeTests();
    }

    private void executeTests() {
        ExecutionManager manager = new ExecutionManager(testClasses);
        manager.execute();
    }
}
