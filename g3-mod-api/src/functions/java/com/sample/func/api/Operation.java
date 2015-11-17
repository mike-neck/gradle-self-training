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

import static com.sample.func.api.Verifications.shouldBeNotNull;

@FunctionalInterface
public interface Operation<O> {

    void execute(O obj) throws ExecutionException;

    static <O> Operation<O> operation(ExOperation<? super O> op) throws EvaluationException {
        shouldBeNotNull(op);
        return obj -> {
            try {
                op.execute(obj);
            } catch (Exception e) {
                throw new ExecutionException(ExOperation.class, e);
            }
        };
    }
}
