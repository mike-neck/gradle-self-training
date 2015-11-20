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

import test.Execute;
import test.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class TestExecutor {

    private final ExecutorService exec;

    private final Set<Class<? extends Test>> tests;

    private final Queue<TestResults> queue;

    public TestExecutor(ExecutorService exec, Set<Class<? extends Test>> tests, Queue<TestResults> queue) {
        this.exec = exec;
        this.tests = tests;
        this.queue = queue;
    }

    public CompletableFuture<Void> run() {
        return CompletableFuture.allOf(tests.stream()
                .map(createCases)
                .<Runnable>map(runners(queue))
                .map(r -> CompletableFuture.runAsync(r, exec))
                .collect(toList()).toArray(new CompletableFuture<?>[tests.size()]));
    }

    private static final Function<Class<? extends Test>, TestCases<? extends Test>> createCases = c -> {
        Set<Method> methods = Stream.of(c.getDeclaredMethods())
                .filter(m -> m.getAnnotation(Execute.class) != null)
                .collect(toSet());
        return new TestCases<>(c, methods);
    };

    private static Function<TestCases<? extends Test>, Runnable> runners(Queue<TestResults> q) {
        return tc -> (Runnable) () -> q.offer(tc.invoke());
    }
}
