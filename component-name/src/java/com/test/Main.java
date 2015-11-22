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

package com.test;

import java.io.*;
import java.util.stream.*;

public class Main implements Runnable {

    public static void main(String... args) {
        new Main().run();
    }

    private final ClassLoader loader = getClass().getClassLoader();

    Main() {}

    @Override
    public void run() {
        try(Stream st = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("test.txt"))).lines()) {
            st.forEach(System.out::println);
        }
    }
}
