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
package com.sample.data.api;

import com.sample.EvaluationException;
import com.sample.data.internal.Nothing;
import com.sample.data.internal.Some;
import com.sample.func.api.Condition;
import com.sample.func.api.Function;

import java.util.NoSuchElementException;

import static com.sample.func.api.Verifications.objectShouldBeNotNull;

public interface Maybe<V> {

    boolean isSome();

    default boolean isNothing() {
        return !isSome();
    }

    <R> Maybe<R> map(Function<? super V, ? extends R> fun) throws EvaluationException;

    <R> Maybe<R> fmap(Function<? super V, ? extends Maybe<R>> fun) throws EvaluationException;

    Maybe<V> filter(Condition<? super V> cond) throws EvaluationException;

    V get() throws NoSuchElementException;

    V or(V defaultValue) throws EvaluationException;

    static <V> Maybe<V> some(V value) throws EvaluationException {
        objectShouldBeNotNull(value);
        return new Some<>(value);
    }

    static <V> Maybe<V> nothing() {
        return new Nothing<>();
    }
}
