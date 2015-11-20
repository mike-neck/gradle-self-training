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

public class Failure<T extends Test> implements Result<T> {

    private final Class<T> testClass;

    private final Method testMethod;

    private final String diff;

    public Failure(Class<T> testClass, Method testMethod, String diff) {
        this.testClass = testClass;
        this.testMethod = testMethod;
        this.diff = diff;
    }

    @Override
    public Class<T> getTestClass() {
        return testClass;
    }

    @Override
    public String getTestName() {
        return testMethod.getName();
    }

    public String getDiff() {
        return diff;
    }
}
