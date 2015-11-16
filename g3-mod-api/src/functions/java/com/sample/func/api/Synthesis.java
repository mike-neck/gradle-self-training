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

import java.util.Arrays;
import java.util.Iterator;

import static com.sample.func.api.Verifications.shouldBeNotNull;
import static com.sample.func.internal.Operation.unsupported;

public final class Synthesis {

    private Synthesis() {
        unsupported(Synthesis.class);
    }

    @SafeVarargs
    public static <S> Condition<S> all(Condition<? super S> head, Condition<? super S>... tail) throws EvaluationException {
        shouldBeNotNull(head);
        Iterator<Condition<? super S>> conditions = Arrays.asList(tail).iterator();
        return sbj -> {
            boolean result = head.test(sbj);
            while (result && conditions.hasNext()) {
                result = conditions.next().test(sbj);
            }
            return result;
        };
    }

    @SafeVarargs
    public static <S> Condition<S> any(Condition<? super S> head, Condition<? super S>... tail) throws EvaluationException {
        shouldBeNotNull(head);
        Iterator<Condition<? super S>> conditions = Arrays.asList(tail).iterator();
        return sbj -> {
            boolean result = head.test(sbj);
            while (!result && conditions.hasNext()) {
                result = conditions.next().test(sbj);
            }
            return result;
        };
    }
}
