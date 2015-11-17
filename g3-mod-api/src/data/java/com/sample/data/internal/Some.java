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

public class Some<V> extends MaybeBase<V> {

    private final V value;

    public Some(V value) {
        this.value = value;
    }

    @Override
    protected <R> Maybe<R> mapInternal(Function<? super V, ? extends R> fun) {
        R result = fun.apply(value);
        return result == null ? new Nothing<>() : new Some<>(result);
    }

    @Override
    protected <R> Maybe<R> fmapInternal(Function<? super V, ? extends Maybe<R>> fun) {
        Maybe<R> result = fun.apply(value);
        return result == null ? new Nothing<>() : result;
    }

    @Override
    protected Maybe<V> filterInternal(Condition<? super V> cond) {
        return cond.test(value)? new Some<>(value) : new Nothing<>();
    }

    @Override
    protected V orInternal(V defaultValue) {
        return value;
    }

    @Override
    public boolean isSome() {
        return true;
    }

    @Override
    public V get() throws NoSuchElementException {
        return value;
    }
}
