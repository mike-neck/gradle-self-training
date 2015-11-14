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

import static com.bsf.function.Verifications.actionShouldNotBeNull;
import static com.bsf.function.Verifications.initializationNotSupported;
import static com.bsf.function.Verifications.shouldNotBeNull;

public final class Actions {

    private Actions() {
        initializationNotSupported();
    }

    public interface ActionBase {}

    public interface Action<I> extends ActionBase {
        void execute(I input) throws ExecutingException;
    }

    public interface ExAction<I> extends ActionBase {
        void execute(I input) throws Exception;
    }

    public static <I> Action<I> doAll(final Action<? super I> head, final Action<? super I>... actions) throws EvaluationException {
        shouldNotBeNull(head, "Action should be non null value.");
        for (Action<? super I> action : actions) {
            actionShouldNotBeNull(action);
        }
        return new Action<I>() {
            @Override
            public void execute(I input) throws ExecutingException {
                head.execute(input);
                for (Action<? super I> action : actions) {
                    action.execute(input);
                }
            }
        };
    }

    public static <I> Action<I> action(final ExAction<? super I> exa) throws EvaluationException {
        actionShouldNotBeNull(exa);
        return new Action<I>() {
            @Override
            public void execute(I input) throws ExecutingException {
                try {
                    exa.execute(input);
                } catch (Exception e) {
                    throw new ExecutingException("Exception on executing ExAction.", e);
                }
            }
        };
    }
}
