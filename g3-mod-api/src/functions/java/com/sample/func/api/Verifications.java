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
import com.sample.func.internal.ConditionBase;
import com.sample.func.internal.FunctionBase;
import com.sample.func.internal.OperationBase;

import static com.sample.func.internal.Operation.unsupported;

public final class Verifications {

    private Verifications() {
        unsupported(Verifications.class);
    }

    public static void shouldBeNotNull(FunctionBase<?, ?> fun) throws EvaluationException {
        if(fun == null) throw new EvaluationException(Function.class);
    }

    public static void shouldBeNotNull(ConditionBase<?> cond) throws EvaluationException {
        if (cond == null) throw new EvaluationException(Condition.class);
    }

    public static void shouldBeNotNull(OperationBase<?> op) throws EvaluationException {
        if (op == null) throw new EvaluationException(Operation.class);
    }

    public static void objectShouldBeNotNull(Object object) throws EvaluationException {
        if (object == null) throw new EvaluationException(Object.class);
    }
}
