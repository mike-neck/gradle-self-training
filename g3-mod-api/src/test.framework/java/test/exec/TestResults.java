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
package test.exec;

import test.Test;
import test.result.Result;
import test.result.Success;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestResults {

    private final Class<? extends Test> testClass;

    private final Map<String, Result> results;

    public TestResults(Class<? extends Test> testClass) {
        this.testClass = testClass;
        this.results = new LinkedHashMap<>();
    }

    public TestResults concatenate(TestResults another) {
        results.putAll(another.results);
        return this;
    }

    public void add(Result<? extends Test> result) {
        results.put(result.getTestName(), result);
    }

    public Class<? extends Test> getTestClass() {
        return testClass;
    }

    public String getClassName() {
        return testClass.getCanonicalName();
    }

    public Map<String, Result> getResults() {
        return results;
    }

    public boolean allSuccess() {
        return results.values().stream()
                .allMatch(r -> r instanceof Success);
    }
}
