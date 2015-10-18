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
package dist.test.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dist.test.Util.toTypeName;
import static dist.test.model.Groups.NORTHERN;
import static dist.test.model.Groups.SOUTHERN;
import static dist.test.model.Groups.WESTERN;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

public enum Names {

    SHEEPDOG("sheepdog", true, NORTHERN),
    FOXHOUND("foxhound", true, SOUTHERN),
    BULLDOG("bulldog", false, WESTERN),
    CHIHUAHUA("chihuahua", false, NORTHERN),
    DALMATIAN("dalmatian", true, SOUTHERN),
    CORGI("corgi", false, WESTERN),
    HUSKY("husky", false, NORTHERN);

    private final String breeds;

    private final boolean fast;

    private final Groups group;

    Names(String breeds, boolean fast, Groups group) {
        this.breeds = breeds;
        this.fast = fast;
        this.group = group;
    }

    public String getBreeds() {
        return breeds;
    }

    public String asTypeName() {
        return toTypeName(breeds);
    }

    public String getTaskName() {
        return breeds + "Test";
    }

    public boolean isFast() {
        return fast;
    }

    public Groups getGroup() {
        return group;
    }

    public String toTime() {
        return fast ? "200l" : "6000l";
    }

    public static Set<Names> byGroup(Groups group) {
        return Stream.of(values())
                .filter(n -> n.getGroup().equals(group))
                .collect(toSet());
    }

    public static Map<Groups, Set<Names>> mapGroups() {
        return Stream.of(values())
                .collect(groupingBy(Names::getGroup, toSet()));
    }
}
