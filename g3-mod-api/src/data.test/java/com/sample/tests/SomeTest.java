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
import com.sample.func.api.Function;
import test.Execute;
import test.Test;

import java.util.Arrays;

import static com.sample.data.api.Maybe.some;
import static com.sample.data.api.Maybe.nothing;

public class SomeTest extends Test {

    @Execute
    public void someIsSomeReturnsTrue() {
        setup(() -> some(20))
                .then(Maybe::isSome)
                .equalsTo(true);
    }

    @Execute
    public void someIsNothingReturnsFalse() {
        setup(() -> some(20))
                .then(Maybe::isNothing)
                .equalsTo(false);
    }

    @Execute
    public void someWillBeMappedToAnotherValue() {
        setup(() -> some("test"))
                .when(m -> m.map(String::length))
                .then(Maybe::get)
                .equalsTo(4);
    }

    @Execute
    public void mapperReturnsNullOnMapItBecomesNothing() {
        setup(() -> some("foo"))
                .when(m -> m.map(s -> null))
                .then(Maybe::isSome)
                .equalsTo(false);
    }

    @Execute
    public void someFmapedToNothingThenItBecomesNothing() {
        setup(() -> some(-5))
                .when(m -> m.fmap(i -> i < 0 ? nothing() : some(i)))
                .then(Maybe::isSome)
                .equalsTo(false);
    }

    @Execute
    public void someFmapedToSomThenItRemainsSome() {
        setup(() -> some(0))
                .when(m -> m.fmap(i -> i < 0 ? nothing() : some(i)))
                .then(Maybe::isSome)
                .equalsTo(true);
    }

    @Execute
    public void mapperReturnsNullOnFmapThenItBecomesNothing() {
        setup(() -> some(10))
                .when(m -> m.fmap(i -> null))
                .then(Maybe::isSome)
                .equalsTo(false);
    }

    @Execute
    public void someFilteredOutBecomesNothing() {
        setup(() -> some("test"))
                .when(m -> m.filter(String::isEmpty))
                .then(Maybe::isSome)
                .equalsTo(false);
    }

    @Execute
    public void someNotFilteredRemainsSome() {
        setup(() -> some("someNotFiltered"))
                .when(m -> m.filter(s -> s.startsWith("some")))
                .then(Maybe::isSome)
                .equalsTo(true);
    }

    @Execute
    public void someGetReturnsValue() {
        setup(() -> some("William"))
                .when(m -> m.map(String::toLowerCase))
                .then(Maybe::get)
                .equalsTo("william");
    }

    @Execute
    public void someOrReturnsOriginalValue() {
        setup(() -> some("gene kelly"))
                .when(m -> m.map(CAPITALIZE))
                .then(m -> m.or("Frank Sinatra"))
                .equalsTo("Gene Kelly");
    }

    private static final Function<String, String> CAPITALIZE = s -> {
        boolean toUpper = true;
        char space = ' ';
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(toUpper ? Character.toUpperCase(c) : c);
            toUpper = c == space;
        }
        return sb.toString();
    };
}
