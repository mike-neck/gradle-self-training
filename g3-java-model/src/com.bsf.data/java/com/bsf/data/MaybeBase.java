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

import com.bsf.function.Actions.Action;
import com.bsf.function.Conditions.Condition;
import com.bsf.function.Functions.Function;
import com.bsf.function.Lazies.Lazy;

import java.util.NoSuchElementException;

import static com.bsf.function.Verifications.actionShouldNotBeNull;
import static com.bsf.function.Verifications.conditionShouldNotBeNull;
import static com.bsf.function.Verifications.functionShouldNotBeNull;
import static com.bsf.function.Verifications.initializationNotSupported;
import static com.bsf.function.Verifications.lazyShouldNotBeNull;
import static com.bsf.function.Verifications.shouldNotBeNull;

class MaybeBase {

    private MaybeBase() {
        initializationNotSupported();
    }

    static class Nothing<V> extends Maybe<V> {
        @Override
        public V get() throws NoSuchElementException {
            throw new NoSuchElementException("Nothing has no element.");
        }

        @Override
        protected V orInternal(V defaultValue) {
            return defaultValue;
        }

        @Override
        protected V orInternal(Lazy<V> lazy) throws ExecutingException {
            return lazy.get();
        }

        @Override
        protected V orThrowInternal(Lazy<? extends RuntimeException> lazyException) throws RuntimeException {
            throw lazyException.get();
        }

        @Override
        protected <R> Maybe<R> mapInternal(Function<? super V, ? extends R> fun) {
            return new Nothing<R>();
        }

        @Override
        protected <R> Maybe<R> fmapInternal(Function<? super V, ? extends Maybe<R>> fun) {
            return new Nothing<R>();
        }

        @Override
        protected Maybe<V> filterInternal(Condition<? super V> cond) {
            return new Nothing<V>();
        }

        @Override
        public boolean isNothing() {
            return true;
        }

        @Override
        protected AttachAction onSomeInternal(final Action<V> action) {
            return new AttachAction() {
                @Override
                protected ActingContext onNothingInternal(final Runnable forNothing) {
                    return new ActingContext() {
                        @Override
                        public void act() throws ExecutingException {
                            forNothing.run();
                        }
                    };
                }

                @Override
                public void act() throws ExecutingException {}
            };
        }

        @Override
        public String toString() {
            return "Nothing";
        }
    }

    static class Some<V> extends Maybe<V> {

        private final V value;

        Some(V value) {
            this.value = value;
        }

        @Override
        public V get() throws NoSuchElementException {
            return value;
        }

        @Override
        protected V orInternal(V defaultValue) {
            return value;
        }

        @Override
        protected V orInternal(Lazy<V> lazy) throws ExecutingException {
            return value;
        }

        @Override
        protected V orThrowInternal(Lazy<? extends RuntimeException> lazyException) throws RuntimeException {
            return value;
        }

        @Override
        protected  <R> Maybe<R> mapInternal(Function<? super V, ? extends R> fun)
                throws ExecutingException {
            R result = fun.apply(value);
            return result == null ? new Nothing<R>() : new Some<R>(result);
        }

        @Override
        protected  <R> Maybe<R> fmapInternal(
                Function<? super V, ? extends Maybe<R>> fun)
                throws ExecutingException {
            Maybe<R> maybe = fun.apply(value);
            return maybe == null ? new Nothing<R>() : maybe;
        }

        @Override
        protected Maybe<V> filterInternal(Condition<? super V> cond)
                throws ExecutingException{
            return cond.test(value) ? new Some<V>(value) : new Nothing<V>();
        }

        @Override
        public boolean isNothing() {
            return false;
        }

        @Override
        protected AttachAction onSomeInternal(final Action<V> action) {
            return new AttachAction() {
                @Override
                protected ActingContext onNothingInternal(Runnable forNothing) {
                    return this;
                }

                @Override
                public void act() throws ExecutingException {
                    action.execute(value);
                }
            };
        }

        @Override
        public String toString() {
            return "Some[" + value + "]";
        }
    }
}
