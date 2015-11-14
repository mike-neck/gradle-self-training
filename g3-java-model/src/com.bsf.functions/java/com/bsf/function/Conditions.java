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

import static com.bsf.function.Verifications.conditionShouldNotBeNull;
import static com.bsf.function.Verifications.initializationNotSupported;

public final class Conditions {

    private Conditions() {
        initializationNotSupported();
    }

    public interface ConditionBase {}

    public interface Condition<S> extends ConditionBase {
        boolean test(S subject) throws ExecutingException;
    }

    public interface ExCondition<S> extends ConditionBase {
        boolean test(S subject) throws Exception;
    }

    public static <S> Condition<S> condition(final ExCondition<? super S> exc) {
        conditionShouldNotBeNull(exc);
        return new Condition<S>() {
            @Override
            public boolean test(S subject) throws ExecutingException {
                try {
                    return exc.test(subject);
                } catch (Exception e) {
                    throw new ExecutingException("Exception on testing of ExCondition.", e);
                }
            }
        };
    }

    public static <S> Condition<S> all(final Condition<? super S> head,
                                       final Condition<? super S>... tail)
            throws EvaluationException{
        conditionShouldNotBeNull(head);
        return new Condition<S>() {
            @Override
            public boolean test(S subject) throws ExecutingException {
                boolean result = head.test(subject);
                if (tail == null || tail.length == 0) {
                    return result;
                } else {
                    for (Condition<? super S> cond : tail) {
                        result &= cond.test(subject);
                        if (!result) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        };
    }

    public static <S> Condition<S> any(final Condition<? super S> head,
                                       final Condition<? super S>... tail)
            throws EvaluationException {
        conditionShouldNotBeNull(head);
        return new Condition<S>() {
            @Override
            public boolean test(S subject) throws ExecutingException {
                boolean result = head.test(subject);
                if (result || tail == null || tail.length == 0) {
                    return result;
                }
                for (Condition<? super S> cond : tail) {
                    if (cond.test(subject)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
