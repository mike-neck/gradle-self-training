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
package com.sample.tests;

import com.sample.data.api.Maybe;
import test.Execute;
import test.Test;

import java.util.NoSuchElementException;

public class NothingTest extends Test {

    @Execute
    public void nothingIsNothingReturnsTrue() {
        setup(Maybe::nothing)
                .then(Maybe::isNothing)
                .equalsTo(true);
    }

    @Execute
    public void nothingIsSomeReturnsFalse() {
        setup(Maybe::nothing)
                .then(Maybe::isSome)
                .equalsTo(false);
    }

    @Execute
    public void nothingOfStringMapWithLengthMethodRemainsNothing() {
        setup(() -> Maybe.<String>nothing())
                .when(m -> m.map(String::length))
                .then(Maybe::isNothing)
                .equalsTo(true);
    }

    @Execute
    public void nothingOfStringFilteredByContentsRemainsNothing() {
        setup(() -> Maybe.<String>nothing())
                .when(m -> m.filter(s -> s.contains("test")))
                .then(Maybe::isNothing)
                .equalsTo(true);
    }

    @Execute
    public void nothingOfStringFmapedRemainsNothing() {
        setup(() -> Maybe.<String>nothing())
                .when(m -> m.fmap(s -> s.length() < 5 ? Maybe.nothing() : Maybe.some(s)))
                .then(Maybe::isNothing)
                .equalsTo(true);
    }

    @Execute
    public void nothingOrReturnsDefaultValue() {
        setup(() -> Maybe.<String>nothing())
                .then(m -> m.or("default value"))
                .equalsTo("default value");
    }

    @Execute
    public void nothingGetThrowsException() {
        setup(() -> Maybe.<String>nothing())
                .when(m -> {
                    try {
                        m.get();
                        return new SomethingWrong();
                    } catch (NoSuchElementException e) {
                        return e;
                    }
                }).then(e -> e instanceof NoSuchElementException)
                .equalsTo(true);
    }

    private static class SomethingWrong extends Exception {}
}
