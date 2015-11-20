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

import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import test.Test;
import test.exception.TestExecutionException;
import test.exception.TestFailureException;
import test.result.Accident;
import test.result.Failure;
import test.result.Panic;
import test.result.Result;
import test.result.Success;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class TestCases<T extends Test> {

    private final Class<T> testClass;

    private final Set<Method> cases;

    public TestCases(Class<T> testClass, Set<Method> cases) {
        this.testClass = testClass;
        this.cases = cases;
    }

    private Function<Method, Result<T>> invokeTest(T test) {
        return m -> {
            try {
                m.invoke(test);
                return new Success<>(testClass, m);
            } catch (TestExecutionException e) {
                return new Accident<>(testClass, m, e.getExplanation());
            } catch (TestFailureException e) {
                return new Failure<>(testClass, m, e.getMessage());
            } catch (IllegalAccessException | InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause == null) {
                    return new Panic<>(testClass, m, e);
                } else if (cause instanceof TestExecutionException) {
                    TestExecutionException t = (TestExecutionException) cause;
                    return new Accident<>(testClass, m, t.getExplanation());
                } else if (cause instanceof TestFailureException) {
                    TestFailureException t = (TestFailureException) cause;
                    return new Failure<>(testClass, m, t.getMessage());
                } else {
                    return new Panic<>(testClass, m, e);
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    public TestResults invoke() {
        try {
            T test = testClass.newInstance();
            return cases.stream()
                    .map(invokeTest(test))
                    .collect(new ResultCollector<>(testClass));
        } catch (InstantiationException | IllegalAccessException e) {
            return cases.stream()
                    .map(m -> new Panic<>(testClass, m, e))
                    .collect(new ResultCollector<>(testClass));
        }
    }

    private static class ResultCollector<T extends Test>
            implements Collector<Result<T>, TestResults, TestResults> {

        private final Class<T> testClass;

        private ResultCollector(Class<T> testClass) {
            this.testClass = testClass;
        }

        @Override
        public Supplier<TestResults> supplier() {
            return () -> new TestResults(testClass);
        }

        @Override
        public BiConsumer<TestResults, Result<T>> accumulator() {
            return TestResults::add;
        }

        @Override
        public BinaryOperator<TestResults> combiner() {
            return TestResults::concatenate;
        }

        @Override
        public Function<TestResults, TestResults> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }
    }
}
