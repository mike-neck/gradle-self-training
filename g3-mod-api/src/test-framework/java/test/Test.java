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

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Test {

    @FunctionalInterface
    interface ExSupplier<T> {
        T get() throws Exception;
    }

    private static class Step<T> {
        private final int count;
        private final Supplier<T> supplier;

        private Step(int count, Supplier<T> input) {
            this.count = count;
            this.supplier = input;
        }

        T get() {
            try {
                return supplier.get();
            } catch (Throwable th) {
                String msg = count == 0 ? "While setup data exception occurred." : "While stretching data exception occurred.";
                throw new TestExecutionException(count, msg, th);
            }
        }
    }

    protected <T> Actual<T> setup(Supplier<T> input) {
        return new Actual<>(input);
    }

    protected <T> Actual<T> setupWithException(ExSupplier<T> input) {
        return new Actual<>(input);
    }

    public static class Actual<T> {
        private final Step<T> step;

        private Actual(Supplier<T> input) throws IllegalArgumentException {
            if (input == null) {
                throw new IllegalArgumentException("Setup requires data setup.");
            }
            this.step = new Step<>(1, input);
        }

        private Actual(ExSupplier<T> input) throws IllegalArgumentException {
            if (input == null) {
                throw new IllegalArgumentException("Setup requires data setup.");
            }
            this.step = new Step<>(1, supplier(input));
        }

        private static <T> Supplier<T> supplier(ExSupplier<T> input) {
            return () -> {
                try {
                    return input.get();
                } catch (Exception e) {
                    throw new TestExecutionException(0, "data setup", e);
                }
            };
        }

        private Actual(int step, Supplier<T> input) throws IllegalArgumentException {
            this.step = new Step<>(step, input);
        }

        public <N> Actual<N> when(Function<? super T, ? extends N> mapper) throws IllegalArgumentException {
            if (mapper == null) {
                throw new IllegalArgumentException("Stretching operation is required at when method.");
            }
            Supplier<N> mutate = () -> {
                try {
                    return mapper.apply(step.get());
                } catch (Throwable e) {
                    throw new TestExecutionException(step.count, "data mutation", e);
                }
            };
            return new Actual<>(step.count + 1, mutate);
        }

        public Asserting<T> then() {
            Supplier<T> actual = () -> {
                try {
                    return step.get();
                } catch (Throwable e) {
                    throw new TestExecutionException(step.count, "assertion", e);
                }
            };
            return new Asserting<>(new Step<>(step.count + 1, actual));
        }
    }

    public static class Asserting<T> {
        private final Step<T> step;

        public Asserting(Step<T> step) {
            this.step = step;
        }

        public void equalsTo(T expected) throws TestFailureException {
            T actual = step.get();
            new Difference<>(actual, expected).compareValue();
        }
    }

    public static class TestExecutionException extends RuntimeException {

        private final int step;
        private final String msg;
        private final Throwable cas;

        public TestExecutionException(int step, String message, Throwable cas) {
            super(message, cas);
            this.step = step;
            this.msg = message;
            this.cas = cas;
        }

        public String getExplanation() {
            return new StringBuilder("step[").append(step).append("] : ")
                    .append(msg).append(" : ").append(System.lineSeparator())
                    .append(cas.getClass().getCanonicalName()).append("[").append(cas.getMessage()).append("]")
                    .toString();
        }
    }

    private static class Difference<T> {
        private final T actual;
        private final T expected;

        private Difference(T actual, T expected) {
            this.actual = actual;
            this.expected = expected;
        }

        void compareValue() throws TestFailureException {
            if (expected != null && !expected.equals(actual)) {
                throw new TestFailureException(this);
            } else if (expected == null && actual != null) {
                throw new TestFailureException(this);
            }
        }

        private String getDiff() {
            String nl = System.lineSeparator();
            String idt = "  ";
            return new StringBuilder("Test failed:").append(nl)
                    .append(idt).append("expected : <").append(expected.getClass().getSimpleName()).append("> ").append(expected.toString()).append(nl)
                    .append(idt).append("actual   : <").append(actual.getClass()).append("> ").append(actual.toString()).append(nl)
                    .toString();
        }
    }

    public static class TestFailureException extends RuntimeException {

        private final Difference<?> diff;

        private TestFailureException(Difference<?> difference) {
            this.diff = difference;
        }

        @Override
        public String getMessage() {
            return diff.getDiff();
        }
    }
}
