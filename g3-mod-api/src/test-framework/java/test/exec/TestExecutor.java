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
import java.util.Collections;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class TestExecutor {

    private final ExecutorService exec;

    private final Set<Class<Test>> tests;

    private final Queue<TestResults> queue;

    public TestExecutor(ExecutorService exec, Set<Class<Test>> tests, Queue<TestResults> queue) {
        this.exec = exec;
        this.tests = tests;
        this.queue = queue;
    }

    public CompletableFuture<Void> run() {
        return tests.stream()
                .map(createCases)
                .map(runners(queue))
                .map(r -> CompletableFuture.runAsync(r, exec))
                .collect(new FutureCollector(tests.size()));
    }

    private static final Function<Class<Test>, TestCases<Test>> createCases = c -> {
        Set<Method> methods = Stream.of(c.getDeclaredMethods())
                .filter(m -> m.getAnnotation(Execute.class) != null)
                .collect(toSet());
        return new TestCases<>(c, methods);
    };

    private static Function<TestCases<? super Test>, Runnable> runners(Queue<TestResults> q) {
        return tc -> () -> q.offer(tc.invoke());
    }

    private static class Future extends CompletableFuture<Void> {}

    private class FutureCollector implements Collector<CompletableFuture<Void>, CompletableFuture<Void>[], CompletableFuture<Void>> {

        private final int size;

        private final AtomicInteger pos = new AtomicInteger(0);

        private FutureCollector(int size) {
            this.size = size;
        }

        @Override
        public Supplier<CompletableFuture<Void>[]> supplier() {
            return () -> new Future[size];
        }

        @Override
        public BiConsumer<CompletableFuture<Void>[], CompletableFuture<Void>> accumulator() {
            return (a, f) -> a[pos.getAndIncrement()] = f;
        }

        @Override
        public BinaryOperator<CompletableFuture<Void>[]> combiner() {
            return (l, r) -> {
                CompletableFuture<Void>[] array = new Future[size];
                System.arraycopy(l, 0, array, 0, l.length);
                System.arraycopy(r, 0, array, l.length, r.length);
                return array;
            };
        }

        @Override
        public Function<CompletableFuture<Void>[], CompletableFuture<Void>> finisher() {
            return CompletableFuture::allOf;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }
    }
}
