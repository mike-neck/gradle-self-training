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
package org.sample;

import org.junit.Ignore;
import org.junit.Test;

import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ScaffoldUrlTest {

    private final ClassLoader loader = getClass().getClassLoader();

    private final PluginResource res = new PluginResource();

    @Ignore
    @Test
    public void scaffoldIsNotNull() {
        URL scaffold = res.getResource("scaffold");
        assertThat(scaffold, is(notNullValue()));
    }

    @Test
    public void codeSourceIsFile() {
        assertTrue(res.isPluginGivenInFile());
    }
}
