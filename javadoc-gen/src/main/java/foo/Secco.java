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

/**
 * <img src="secco.jpg" alt="cioccolata.jpg">
 */
public class Secco implements AbilityPerson {

    private static final Stand OASIS = new Stand("Oasis");

    @Override
    public void say(String message) {
        System.out.println("セッコ : よるんじゃねーブチャラティー");
    }

    public void playWith(AbilityPerson person) {
        if (person instanceof Cioccolata) {
            Cioccolata cioccolata = (Cioccolata) person;
            cioccolata.say("よーしよしよしよしよしよしよし");
        }
    }

    /**
     * <img src="cioccolata-secco.jpg" alt="cioccolata-secco.jpg">
     * @param cubes - 砂糖の数
     * @param by - くれた人
     */
    @Override
    public void takeCube(int cubes, AbilityPerson by) {
        playWith(by);
    }
}
