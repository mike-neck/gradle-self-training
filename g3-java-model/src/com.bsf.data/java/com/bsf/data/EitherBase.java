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
package com.bsf.data;

import com.bsf.basic.ExecutingException;

import com.bsf.function.Actions.Action;
import com.bsf.function.Conditions.Condition;
import com.bsf.function.Functions.Function;
import com.bsf.function.Lazies.Lazy;

import java.util.NoSuchElementException;

import static com.bsf.function.Verifications.initializationNotSupported;
import static com.bsf.function.Verifications.lazyShouldNotBeNull;
import static com.bsf.function.Verifications.shouldNotBeNull;

final class EitherBase {

    private EitherBase() {
        initializationNotSupported();
    }

    static class Left<L, R> extends Either<L, R> {

        private final L cause;

        Left(L cause) {
            shouldNotBeNull(cause, "Left require cause.");
            this.cause = cause;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public R get() throws NoSuchElementException {
            throw new NoSuchElementException(cause.toString());
        }

        @Override
        protected R orInternal(R defaultValue) {
            return defaultValue;
        }

        @Override
        protected R orInternal(Lazy<? extends R> lazy)
                throws ExecutingException {
            R result = lazy.get();
            if (result == null) {
                throw new ExecutingException("Default value should be non null.");
            }
            return result;
        }

        @Override
        protected R orThrowInternal(Function<? super L, ? extends RuntimeException> fun)
                throws RuntimeException {
            RuntimeException e = fun.apply(cause);
            if (e == null) {
                throw new ExecutingException("Generated exception should be non null.");
            }
            throw e;
        }

        @Override
        protected <E> Either<L, E> mapInternal(
                Function<? super R, ? extends E> fun)
                throws ExecutingException {
            return new Left<L, E>(cause);
        }

        @Override
        protected <E> Either<L, E> fmapInternal(
                Function<? super R, ? extends Either<L, E>> fun)
                throws ExecutingException {
            return new Left<L, E>(cause);
        }

        @Override
        protected Either<L, R> filterInternal(Condition<? super R> cond)
                throws ExecutingException {
            return new Left<L, R>(cause);
        }

        @Override
        protected Either<L, R> filterInternal(Condition<? super R> cond, L left)
                throws ExecutingException {
            return new Left<L, R>(cause);
        }

        @Override
        protected AttachAction<L> onRightInternal(Action<? super R> forRight) {
            return new AttachAction<L>() {
                @Override
                protected ActingContext onLeftInternal(
                        final Action<? super L> forLeft) {
                    return new ActingContext() {
                        @Override
                        public void act() throws ExecutingException {
                            forLeft.execute(cause);
                        }
                    };
                }

                @Override
                public void act() throws ExecutingException {}
            };
        }
    }

    static class Right<L, R> extends Either<L, R> {

        private final R value;

        private final Lazy<L> gen;

        Right(R value, Lazy<L> gen) {
            shouldNotBeNull(value, "Right requires value.");
            lazyShouldNotBeNull(gen);
            this.value = value;
            this.gen = gen;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public R get() throws NoSuchElementException {
            return value;
        }

        @Override
        protected R orInternal(R defaultValue) {
            return value;
        }

        @Override
        protected R orInternal(Lazy<? extends R> lazy)
                throws ExecutingException {
            return value;
        }

        @Override
        protected R orThrowInternal(
                Function<? super L, ? extends RuntimeException> fun)
                throws RuntimeException {
            return value;
        }

        private static <L> L take(Lazy<L> lazy) throws ExecutingException {
            L left = lazy.get();
            if (left == null) {
                throw new ExecutingException("Default left value is null.");
            }
            return left;
        }

        @Override
        protected <E> Either<L, E> mapInternal(
                Function<? super R, ? extends E> fun)
                throws ExecutingException {
            E result = fun.apply(value);
            if (result == null) {
                return new Left<L, E>(take(gen));
            } else  {
                return new Right<L, E>(result, gen);
            }
        }

        @Override
        protected <E> Either<L, E> fmapInternal(
                Function<? super R, ? extends Either<L, E>> fun)
                throws ExecutingException {
            Either<L, E> result = fun.apply(value);
            if (result == null) {
                return new Left<L, E>(take(gen));
            } else {
                return result;
            }
        }

        @Override
        protected Either<L, R> filterInternal(Condition<? super R> cond)
                throws ExecutingException {
            if (cond.test(value)) {
                return new Right<L, R>(value, gen);
            } else {
                return new Left<L, R>(take(gen));
            }
        }

        @Override
        protected Either<L, R> filterInternal(Condition<? super R> cond, L left)
                throws ExecutingException {
            return cond.test(value) ?
                    new Right<L, R>(value, gen) :
                    new Left<L, R>(left);
        }

        @Override
        protected AttachAction<L> onRightInternal(final Action<? super R> forRight) {
            return new AttachAction<L>() {
                @Override
                protected ActingContext onLeftInternal(
                        Action<? super L> forLeft) {
                    return this;
                }

                @Override
                public void act() throws ExecutingException {
                    forRight.execute(value);
                }
            };
        }
    }
}
