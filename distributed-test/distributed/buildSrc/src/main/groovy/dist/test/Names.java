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
package dist.test;

import static dist.test.Util.toTypeName;

public enum Names {

    SHEEPDOG("sheepdog", true),
    FOXHOUND("foxhound", true),
    BULLDOG("bulldog", false),
    CHIHUAHUA("chihuahua", false),
    DALMATIAN("dalmatian", true),
    CORGI("corgi", false),
    HUSKY("husky", false);

    private final String breeds;

    private final boolean fast;

    Names(String breeds, boolean fast) {
        this.breeds = breeds;
        this.fast = fast;
    }

    public String getBreeds() {
        return breeds;
    }

    public String asTypeName() {
        return toTypeName(breeds);
    }

    public boolean isFast() {
        return fast;
    }

    public String toTime() {
        return fast ? "200l" : "6000l";
    }
}
