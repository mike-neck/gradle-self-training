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
package com.sample.data.internal;

import com.sample.data.api.Maybe;
import com.sample.func.api.Condition;
import com.sample.func.api.Function;

import java.util.NoSuchElementException;

public class Nothing<V> extends MaybeBase<V> {

    @Override
    protected <R> Maybe<R> mapInternal(Function<? super V, ? extends R> fun) {
        return new Nothing<>();
    }

    @Override
    protected <R> Maybe<R> fmapInternal(Function<? super V, ? extends Maybe<R>> fun) {
        return new Nothing<>();
    }

    @Override
    protected Maybe<V> filterInternal(Condition<? super V> cond) {
        return new Nothing<>();
    }

    @Override
    public boolean isSome() {
        return false;
    }

    @Override
    public V get() throws NoSuchElementException {
        throw new  NoSuchElementException("Nothing doesn't contains value.");
    }

    @Override
    protected V orInternal(V defaultValue) {
        return defaultValue;
    }

    @Override
    public String toString() {
        return "[Nothing]";
    }
}
