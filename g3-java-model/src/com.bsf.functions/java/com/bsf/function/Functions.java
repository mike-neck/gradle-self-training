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

import static com.bsf.function.Verifications.functionShouldNotBeNull;
import static com.bsf.function.Verifications.initializationNotSupported;

public final class Functions {

    private Functions() {
        initializationNotSupported();
    }

    public interface FunctionBase {}

    public interface Function<I, O> extends FunctionBase {
        O apply(I input) throws ExecutingException;
    }

    public interface ExFunction<I, O> extends FunctionBase {
        O apply(I input) throws Exception;
    }

    public static <I, O> Function<I, O> function(final ExFunction<? super I, ? extends O> fun) throws EvaluationException {
        functionShouldNotBeNull(fun);
        return new Function<I, O>() {
            @Override
            public O apply(I input) throws ExecutingException {
                try {
                    return fun.apply(input);
                } catch (Exception e) {
                    throw new ExecutingException("Exception on executing ExFunction.", e);
                }
            }
        };
    }
}
