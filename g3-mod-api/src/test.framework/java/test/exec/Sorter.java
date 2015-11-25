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
package test.exec;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class Sorter {

    private final Queue<TestResults> queue;

    public Sorter(Queue<TestResults> queue) {
        this.queue = queue;
    }

    private static final Comparator<TestResults> BY_NAME = (l, r) ->
            l.getTestClass().getCanonicalName()
                    .compareTo(
                            r.getTestClass().getCanonicalName());

    public Function<Void, List<TestResults>> sortResult() {
        return v -> Collections.unmodifiableList(
                queue.stream()
                        .sorted(BY_NAME)
                        .collect(toList()));
    }
}
