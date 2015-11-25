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
package test.result;

import test.Test;

import java.lang.reflect.Method;

public class Accident<T extends Test> implements Result<T> {

    private final Class<T> testClass;

    private final Method testName;

    private final String explanation;

    public Accident(Class<T> testClass, Method testName, String explanation) {
        this.testClass = testClass;
        this.testName = testName;
        this.explanation = explanation;
    }

    @Override
    public Class<T> getTestClass() {
        return testClass;
    }

    @Override
    public String getTestName() {
        return testName.getName();
    }

    public String getExplanation() {
        return explanation;
    }
}
