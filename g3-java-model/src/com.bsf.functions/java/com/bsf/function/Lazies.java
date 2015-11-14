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
package com.bsf.function;

import com.bsf.basic.EvaluationException;
import com.bsf.basic.ExecutingException;

import static com.bsf.function.Verifications.initializationNotSupported;
import static com.bsf.function.Verifications.lazyShouldNotBeNull;

public final class Lazies {

    private Lazies(){
        initializationNotSupported();
    }

    public interface LazyBase {}

    public interface Lazy<O> extends LazyBase {
        O get() throws ExecutingException;
    }

    public interface ExLazy<O> extends LazyBase {
        O get() throws Exception;
    }

    public static <O> Lazy<O> lazy(final ExLazy<? extends O> el) throws EvaluationException {
        lazyShouldNotBeNull(el);
        return new Lazy<O>() {
            @Override
            public O get() {
                try {
                    return el.get();
                } catch (Exception e) {
                    throw new ExecutingException("Exception on executing ExLazy.", e);
                }
            }
        };
    }
}
