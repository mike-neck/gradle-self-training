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
package com.example;

import org.junit.Ignore;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static com.example.Maybe.nothing;
import static com.example.Maybe.some;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * A test class for {@link Maybe}.
 */
@RunWith(Enclosed.class)
public class MaybeTest {

    /**
     * Test for testing container type.
     */
    public static class ContainerTypeTest {

        @Ignore
        public void someShouldReturnTrue_onIsSome() {
            assertThat(some(1).isSome(), is(true));
        }

        @Ignore
        public void nothingShouldReturnFalse_onIsSome() {
            assertThat(nothing().isSome(), is(false));
        }
    }
}
