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
package dist.test

import spock.lang.Specification
import spock.lang.Unroll

import static dist.test.Util.toTypeName

class UtilSpec extends Specification {

    def 'toTypeName exception spec' () {
        when:
        toTypeName(name)

        then:
        thrown(exception)

        where:
        name | exception
        null | IllegalArgumentException
        ''   | IllegalArgumentException
    }

    @Unroll
    def 'toTypeName(#name) returns #expected' () {
        when:
        def type = toTypeName(name)

        then:
        type == expected

        where:
        name        | expected
        'abd'       | 'Abd'
        'a'         | 'A'
        'redReborn' | 'RedReborn'
    }
}
