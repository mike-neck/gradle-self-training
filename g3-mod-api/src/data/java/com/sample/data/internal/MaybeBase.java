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

import com.sample.EvaluationException;
import com.sample.data.api.Maybe;
import com.sample.func.api.Condition;
import com.sample.func.api.Function;

import static com.sample.func.api.Verifications.objectShouldBeNotNull;
import static com.sample.func.api.Verifications.shouldBeNotNull;

public abstract class MaybeBase<V> implements Maybe<V> {

    @Override
    public <R> Maybe<R> map(Function<? super V, ? extends R> fun) throws EvaluationException {
        shouldBeNotNull(fun);
        return mapInternal(fun);
    }

    abstract protected <R> Maybe<R> mapInternal(Function<? super V, ? extends R> fun);

    @Override
    public <R> Maybe<R> fmap(Function<? super V, ? extends Maybe<R>> fun) throws EvaluationException {
        shouldBeNotNull(fun);
        return fmapInternal(fun);
    }

    abstract protected <R> Maybe<R> fmapInternal(Function<? super V, ? extends Maybe<R>> fun);

    @Override
    public Maybe<V> filter(Condition<? super V> cond) throws EvaluationException {
        shouldBeNotNull(cond);
        return filterInternal(cond);
    }

    abstract protected Maybe<V> filterInternal(Condition<? super V> cond);

    @Override
    public V or(V defaultValue) throws EvaluationException {
        objectShouldBeNotNull(defaultValue);
        return orInternal(defaultValue);
    }

    abstract protected V orInternal(V defaultValue);
}
