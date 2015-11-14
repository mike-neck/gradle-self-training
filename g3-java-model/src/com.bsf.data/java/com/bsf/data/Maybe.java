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

import com.bsf.data.MaybeBase.Nothing;
import com.bsf.data.MaybeBase.Some;

import java.util.NoSuchElementException;

import static com.bsf.function.Verifications.actionShouldNotBeNull;
import static com.bsf.function.Verifications.conditionShouldNotBeNull;
import static com.bsf.function.Verifications.functionShouldNotBeNull;
import static com.bsf.function.Verifications.lazyShouldNotBeNull;
import static com.bsf.function.Verifications.shouldNotBeNull;

public abstract class Maybe<V> {

    // methods to retrieve value

    abstract public V get() throws NoSuchElementException;

    public V or(V defaultValue) throws EvaluationException {
        shouldNotBeNull(defaultValue, "Default value is required.");
        return orInternal(defaultValue);
    }

    abstract protected V orInternal(V defaultValue);

    public V or(Lazy<V> lazy) throws EvaluationException, ExecutingException {
        lazyShouldNotBeNull(lazy);
        return orInternal(lazy);
    }

    abstract protected V orInternal(Lazy<V> lazy) throws ExecutingException;

    public V orThrow(Lazy<? extends RuntimeException> lazyException) throws RuntimeException {
        lazyShouldNotBeNull(lazyException);
        return orThrowInternal(lazyException);
    }

    abstract protected V orThrowInternal(Lazy<? extends RuntimeException> lazyException)
            throws RuntimeException;

    // methods to mapping value to another type

    public <R> Maybe<R> map(Function<? super V, ? extends R> fun)
            throws EvaluationException, ExecutingException {
        functionShouldNotBeNull(fun);
        return mapInternal(fun);
    }

    abstract protected <R> Maybe<R> mapInternal(Function<? super V, ? extends R> fun)
            throws ExecutingException;

    public <R> Maybe<R> fmap(Function<? super V, ? extends Maybe<R>> fun)
            throws EvaluationException, ExecutingException {
        functionShouldNotBeNull(fun);
        return fmapInternal(fun);
    }

    abstract protected <R> Maybe<R> fmapInternal(Function<? super V, ? extends Maybe<R>> fun)
            throws ExecutingException;

    // methods to filter value

    public Maybe<V> filter(Condition<? super V> cond)
            throws EvaluationException, ExecutingException {
        conditionShouldNotBeNull(cond);
        return filterInternal(cond);
    }

    abstract protected Maybe<V> filterInternal(Condition<? super V> cond)
            throws ExecutingException;

    // test whether holding value

    public boolean isSome() {
        return !isNothing();
    }

    abstract public boolean isNothing();

    // attach action

    public abstract class AttachAction implements ActingContext {

        public ActingContext onNothing(Runnable forNothing) throws EvaluationException {
            shouldNotBeNull(forNothing, "Default runnable should be non null.");
            return onNothingInternal(forNothing);
        }

        abstract protected ActingContext onNothingInternal(Runnable forNothing);
    }

    public AttachAction onSome(Action<V> action) throws EvaluationException {
        actionShouldNotBeNull(action);
        return onSomeInternal(action);
    }

    abstract protected AttachAction onSomeInternal(Action<V> action);

    // constructor

    public static <T> Maybe<T> nothing() {
        return new Nothing<T>();
    }

    public static <T> Maybe<T> some(T value) {
        shouldNotBeNull(value, "Some can take non null value.");
        return new Some<T>(value);
    }

    public static <T> Maybe<T> maybe(T value) {
        if (value == null) {
            return nothing();
        } else {
            return some(value);
        }
    }
}
