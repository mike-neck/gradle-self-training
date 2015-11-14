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

import com.bsf.basic.EvaluationException;
import com.bsf.basic.ExecutingException;

import com.bsf.function.Actions.Action;
import com.bsf.function.Conditions.Condition;
import com.bsf.function.Functions.Function;
import com.bsf.function.Lazies.Lazy;

import com.bsf.data.EitherBase.Left;
import com.bsf.data.EitherBase.Right;

import java.util.NoSuchElementException;

import static com.bsf.function.Verifications.actionShouldNotBeNull;
import static com.bsf.function.Verifications.conditionShouldNotBeNull;
import static com.bsf.function.Verifications.functionShouldNotBeNull;
import static com.bsf.function.Verifications.lazyShouldNotBeNull;
import static com.bsf.function.Verifications.shouldNotBeNull;

public abstract class Either<L, R> {

    // methods to test object

    public boolean isLeft() {
        return !isRight();
    }

    abstract public boolean isRight();

    // methods to retrieve value

    abstract public R get() throws NoSuchElementException;

    public R or(R defaultValue) throws EvaluationException {
        shouldNotBeNull(defaultValue, "Default value should be non null.");
        return orInternal(defaultValue);
    }

    abstract protected R orInternal(R defaultValue);

    public R or(Lazy<? extends R> lazy) throws EvaluationException, ExecutingException {
        lazyShouldNotBeNull(lazy);
        return orInternal(lazy);
    }

    abstract protected R orInternal(Lazy<? extends R> lazy) throws ExecutingException;

    public R orThrow(Function<? super L, ? extends RuntimeException> fun)
            throws RuntimeException {
        functionShouldNotBeNull(fun);
        return orThrowInternal(fun);
    }

    abstract protected R orThrowInternal(
            Function<? super L, ? extends RuntimeException> fun)
            throws ExecutingException;

    // methods to mapping value to another value

    public <E> Either<L, E> map(Function<? super R, ? extends E> fun)
            throws EvaluationException, ExecutingException {
        functionShouldNotBeNull(fun);
        return mapInternal(fun);
    }

    abstract protected <E> Either<L, E> mapInternal(
            Function<? super R, ? extends E> fun) throws ExecutingException;

    public <E> Either<L, E> fmap(Function<? super R, ? extends Either<L, E>> fun)
            throws EvaluationException, ExecutingException {
        functionShouldNotBeNull(fun);
        return fmapInternal(fun);
    }

    abstract protected <E> Either<L, E> fmapInternal(
            Function<? super R, ? extends Either<L, E>> fun)
            throws ExecutingException;

    // methods to filter value

    public Either<L, R> filter(Condition<? super R> cond)
            throws EvaluationException, ExecutingException {
        conditionShouldNotBeNull(cond);
        return filterInternal(cond);
    }

    abstract protected Either<L, R> filterInternal(Condition<? super R> cond);

    public Either<L, R> filter(Condition<? super R> cond, L left)
            throws EvaluationException, ExecutingException {
        conditionShouldNotBeNull(cond);
        shouldNotBeNull(left, "Left cause should be non null.");
        return filterInternal(cond, left);
    }

    abstract protected Either<L, R> filterInternal(
            Condition<? super R> cond, L left)
            throws ExecutingException;

    // attach action

    public static abstract class AttachAction<L> implements ActingContext {
        public ActingContext onLeft(Action<? super L> forLeft)
                throws EvaluationException {
            actionShouldNotBeNull(forLeft);
            return onLeftInternal(forLeft);
        }

        abstract protected ActingContext onLeftInternal(
                Action<? super L> forLeft);
    }

    public AttachAction<L> onRight(Action<? super R> forRight)
            throws EvaluationException {
        actionShouldNotBeNull(forRight);
        return onRightInternal(forRight);
    }

    abstract protected AttachAction<L> onRightInternal(
            Action<? super R> forRight);

    // constructor
    public static <L, R> Either<L, R> left(L cause) throws EvaluationException {
        shouldNotBeNull(cause, "Left cause should be non null.");
        return new Left<L, R>(cause);
    }

    private static final Lazy<String> DEFAULT_LEFT = new Lazy<String>() {
        @Override
        public String get() throws ExecutingException {
            return "The value doesn't matches to condition or is mapped to null or is f-mapped to nothing.";
        }
    };

    public static <L, R> Either<L, R> right(R value, Lazy<L> left)
            throws EvaluationException {
        shouldNotBeNull(value, "Right value should be non null.");
        lazyShouldNotBeNull(left);
        return new Right<L, R>(value, left);
    }

    public static <R> RightBuilder<R> right(final R value)
            throws EvaluationException {
        shouldNotBeNull(value, "Right value should be non null.");
        return new RightBuilder<R>() {
            @Override
            public Either<String, R> withLeft(final String left)
                    throws EvaluationException {
                shouldNotBeNull(left, "Left cause should be non null.");
                return new Right<String, R>(value,
                        new Lazy<String>() {
                            @Override
                            public String get() throws ExecutingException {
                                return left;
                            }
                        });
            }

            @Override
            public Either<String, R> build() {
                return new Right<String, R>(value, DEFAULT_LEFT);
            }
        };
    }

    public interface RightBuilderWithLeft<L, R> {
        Either<L, R> build();
    }

    public interface RightBuilder<R> extends RightBuilderWithLeft<String, R> {
        Either<String, R> withLeft(String left) throws EvaluationException;
    }
}
