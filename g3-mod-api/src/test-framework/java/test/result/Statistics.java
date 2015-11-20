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

import test.exec.TestResults;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class Statistics {

    private final int total;

    private final int success;

    private final int failure;

    private final int error;

    public Statistics(int success, int failure, int error) {
        this.success = success;
        this.failure = failure;
        this.error = error;
        this.total = success + failure + error;
    }

    public int getTotal() {
        return total;
    }

    public int getSuccess() {
        return success;
    }

    public int getFailure() {
        return failure;
    }

    public int getError() {
        return error;
    }

    public static Function<List<TestResults>, Statistics> GET_STATISTICS = l -> l.stream()
            .map(TestResults::getResults)
            .map(Map::values)
            .flatMap(Collection::stream)
            .collect(new StatisticsCollector());

    private static class StatisticsCollector implements Collector<Result, Builder, Statistics> {
        @Override
        public Supplier<Builder> supplier() {
            return Builder::new;
        }

        @Override
        public BiConsumer<Builder, Result> accumulator() {
            return Builder::add;
        }

        @Override
        public BinaryOperator<Builder> combiner() {
            return Builder::merge;
        }

        @Override
        public Function<Builder, Statistics> finisher() {
            return Builder::build;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }
    }

    public static class Builder {

        private static final Object LOCK = new Object();

        Builder() {}

        Builder(int success, int failure, int error) {
            this.success = success;
            this.failure = failure;
            this.error = error;
        }

        private int success = 0;
        private int failure = 0;
        private int error = 0;

        synchronized void add(Result result) {
            if (result instanceof Success) {
                success += 1;
            } else if (result instanceof Failure) {
                failure += 1;
            } else {
                error += 1;
            }
        }

        Builder merge(Builder another) {
            synchronized (LOCK) {
                return new Builder(success + another.success, failure + another.failure, error + another.error);
            }
        }

        Statistics build() {
            return new Statistics(success, failure, error);
        }
    }
}
