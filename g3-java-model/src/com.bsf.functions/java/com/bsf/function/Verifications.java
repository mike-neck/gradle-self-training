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

import com.bsf.function.Actions.ActionBase;
import com.bsf.function.Conditions.ConditionBase;
import com.bsf.function.Functions.FunctionBase;
import com.bsf.function.Lazies.LazyBase;

public final class Verifications {

    public static void initializationNotSupported()
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException("To initialize this class " +
                "is not supported");
    }

    private Verifications() throws UnsupportedOperationException {
        initializationNotSupported();
    }

    public static void shouldNotBeNull(Object object, String message)
            throws EvaluationException {
        if (object == null) {
            String msg = message == null || message.isEmpty() ?
                    "Message undefined." : message;
            throw new EvaluationException(msg);
        }
    }

    public static void lazyShouldNotBeNull(LazyBase lazy) throws EvaluationException {
        shouldNotBeNull(lazy, "Lazy should be non null value.");
    }

    public static void actionShouldNotBeNull(ActionBase action) throws EvaluationException {
        shouldNotBeNull(action, "Action should be non null value.");
    }

    public static void functionShouldNotBeNull(FunctionBase function) throws EvaluationException {
        shouldNotBeNull(function, "Function should be non null value.");
    }

    public static void conditionShouldNotBeNull(ConditionBase condition) throws EvaluationException {
        shouldNotBeNull(condition, "Condition should be non null value.");
    }
}
