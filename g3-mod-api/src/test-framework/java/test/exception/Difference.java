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
package test.exception;

public class Difference<T> {

    private static final String INDEX = "        ";

    private final T actual;
    private final T expected;

    public Difference(T actual, T expected) {
        this.actual = actual;
        this.expected = expected;
    }

    public void compareValue() throws TestFailureException {
        if (expected != null && !expected.equals(actual)) {
            throw new TestFailureException(this);
        } else if (expected == null && actual != null) {
            throw new TestFailureException(this);
        }
    }

    public String getDiff() {
        String nl = System.lineSeparator();
        String idt = "  ";
        StringBuilder sb = new StringBuilder(INDEX).append("Test failed:").append(nl);
        if (expected == null) {
            sb.append(INDEX).append(idt).append("expected : <null> null").append(nl);
        } else {
            sb.append(INDEX).append(idt).append("expected : <").append(expected.getClass().getSimpleName()).append("> ").append(expected.toString()).append(nl);
        }
        if (actual == null) {
            sb.append(INDEX).append(idt).append("actual   : <null> null").append(nl);
        } else {
            sb.append(INDEX).append(idt).append("actual   : <").append(actual.getClass()).append("> ").append(actual.toString()).append(nl);
        }
        return sb.toString();
    }
}
