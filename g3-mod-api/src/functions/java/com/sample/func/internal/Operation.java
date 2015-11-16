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
package com.sample.func.internal;

public final class Operation {

    private static void unsupported(String message) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(message);
    }

    public static void unsupported(Class<?> klass) throws UnsupportedOperationException {
        unsupported(String.format("Initializing %s class is not supported", klass.getSimpleName()));
    }
}
