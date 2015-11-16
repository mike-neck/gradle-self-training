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
package com.sample.func.api;

import com.sample.EvaluationException;
import com.sample.ExecutionException;
import com.sample.func.internal.FunctionBase;

import static com.sample.func.api.Verifications.shouldBeNotNull;

@FunctionalInterface
public interface Function<I, O> extends FunctionBase<I, O> {

    O apply(I input) throws ExecutionException;

    static <IN, OUT> Function<IN, OUT> function(final ExFunction<? super IN, ? extends OUT> exf) throws EvaluationException {
        shouldBeNotNull(exf);
        return i -> {
            try {
                return exf.apply(i);
            } catch (Exception e) {
                throw new ExecutionException(ExFunction.class, e);
            }
        };
    }
}
