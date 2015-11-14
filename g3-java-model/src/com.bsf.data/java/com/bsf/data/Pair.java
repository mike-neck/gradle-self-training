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

import com.bsf.function.Functions.Function;

import static com.bsf.function.Verifications.shouldNotBeNull;

public abstract class Pair<F, S> implements Function<Function<F, Function<S, Object>>, Object> {

    public static <L, R> Pair<L, R> pair(final L first, final R second) throws EvaluationException {
        shouldNotBeNull(first, "First element should be non null.");
        shouldNotBeNull(second, "Second element should be non null.");
        return new Pair<L, R>() {
            @Override
            public Object apply(Function<L, Function<R, Object>> function) throws ExecutingException {
                return function.apply(first).apply(second);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public F fst() {
        return (F)apply(new Function<F, Function<S, Object>>() {
            @Override
            public Function<S, Object> apply(final F f) throws ExecutingException {
                return new Function<S, Object>() {
                    @Override
                    public Object apply(S s) throws ExecutingException {
                        return f;
                    }
                };
            }
        });
    }

    @SuppressWarnings("unchecked")
    public S snd() {
        return (S)apply(new Function<F, Function<S, Object>>() {
            @Override
            public Function<S, Object> apply(F f) throws ExecutingException {
                return new Function<S, Object>() {
                    @Override
                    public Object apply(S s) throws ExecutingException {
                        return s;
                    }
                };
            }
        });
    }

    public Pair<S, F> swap() {
        return pair(snd(), fst());
    }
}
