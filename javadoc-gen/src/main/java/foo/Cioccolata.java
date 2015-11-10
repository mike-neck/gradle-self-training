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
package foo;

public class Cioccolata implements AbilityPerson {

    private static final Stand GREEN_DAY = new Stand("Green Day");

    public CubesTo giveSugarCube(int cubes) {
        AbilityPerson self = this;
        return p -> p.takeCube(cubes, this);
    }

    @Override
    public void say(String message) {
        System.out.println("チョコラータ : " + message);
    }

    public interface CubesTo {
        void to(AbilityPerson person);
    }
}
