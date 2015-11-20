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

public class Panic<T extends Test> implements Result<T> {

    private final Class<T> testClass;

    private final Method testName;

    private final Throwable error;

    public Panic(Class<T> testClass, Method testName, Throwable error) {
        this.testClass = testClass;
        this.testName = testName;
        this.error = error;
    }

    @Override
    public Class<T> getTestClass() {
        return null;
    }

    @Override
    public String getTestName() {
        return null;
    }

    public String getCause() {
        String nl = System.lineSeparator();
        StringBuilder sb = new StringBuilder("Exception [").append(error.getClass().getCanonicalName()).append("]").append(nl)
                .append("  message : ").append(error.getMessage()).append(nl)
                .append("    caused by [").append(error.getCause().getClass().getCanonicalName()).append("]").append(nl);
        boolean firstLine = true;
        for (StackTraceElement se : error.getStackTrace()) {
            sb.append(firstLine ? "  at " : "     ").append(se.getClassName())
                    .append('#').append(se.getMethodName())
                    .append('(').append(se.getFileName())
                    .append('[').append(se.getLineNumber()).append("])").append(nl);
            firstLine = false;
        }
        return sb.toString();
    }
}
