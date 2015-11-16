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
package com.example;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The data container value which may have data.
 * @param <V> - the type of data.
 */
public interface Maybe<V> {

    /**
     * Test whether this container has data or not.
     * @return {@code true} - if this container has data.
     */
    boolean isSome();

    /**
     * Test whether this container doesn't have data or not.
     * @return {@code true} - if this container doesn't have data.
     */
    default boolean isNothing() {
        return !isSome();
    }

    /**
     * Mapping data from type {@code V} to type {@code R}.
     * If given {@link Function} is {@code null}, {@link IllegalArgumentException} will be thrown.
     * If given {@link Function} returns {@code null}, the container, which this method returns, will be {@code Nothing}.
     * @param fun - A mapper function from {@code V} to {@code R}.
     * @param <R> - The type of result value.
     * @return - A new data container.
     * @throws IllegalArgumentException - If mapper function is {@code null}.
     */
    <R> Maybe<R> map(Function<? super V, ? extends R> fun) throws IllegalArgumentException;

    /**
     * Mapping data from type {@code V} to type {@code R}.
     * If given {@link Function} is {@code null}, {@link IllegalArgumentException} will be thrown.
     * If given {@link Function} returns {@code null}, the container, which this method returns, will be {@code Nothing}.
     * @param fun - A mapper function from {@code V} to {@code R}.
     * @param <R> - The type of result value.
     * @return - A new data container.
     * @throws IllegalArgumentException - If mapper function is {@code null}.
     */
    <R> Maybe<R> fmap(Function<? super V, ? extends Maybe<R>> fun) throws IllegalArgumentException;

    /**
     * Test data and returns a new container.
     * If given {@link Predicate} is {@code null}, {@link IllegalArgumentException} will be thrown.
     * If this is already {@code Nothing}, a new container becomes {@code Nothing}.
     * @param condition - A test for data.
     * @return - A new data container.
     * @throws IllegalArgumentException - if testing condition is {@code null}.
     */
    Maybe<V> filter(Predicate<? super V> condition) throws IllegalArgumentException;

    /**
     * Retrieves a value which this container holds.
     * @return - A value which this container holds.
     * @throws NoSuchElementException - if this container is {@code Nothing}.
     */
    V get() throws NoSuchElementException;

    /**
     * Retrieves a value or returns default value.
     * @param defaultValue - A default value.
     * @return - A value if this container has a data. The default data if this container doesn't have a data.
     * @throws IllegalArgumentException - if default data is {@code null}.
     */
    V or(V defaultValue) throws IllegalArgumentException;

    /**
     * The public static constructor for {@link Maybe}.
     * @param value - non null value.
     * @param <T> the type of value.
     * @return instance of {@code Some}.
     * @throws IllegalArgumentException - if a given value is {@code null}.
     */
    static <T> Maybe<T> some(T value) throws IllegalArgumentException {
        return null;
    }

    /**
     * The public static constructor for {@link Maybe}
     * @param <T> the type of value.
     * @return - instance of {@code Nothing}.
     */
    static <T> Maybe<T> nothing() {
        return null;
    }

    /**
     * The public static constructor for {@link Maybe}.
     * @param value - an uncertain value whether it's {@code null} or not
     * @param <T> - type of value.
     * @return - instance of {@link Maybe}.
     */
    static <T> Maybe<T> maybe(T value) {
        return null;
    }
}
