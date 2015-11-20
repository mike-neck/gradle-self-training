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

public class TestExecutionException extends RuntimeException {

    private static final String INDEX = "        ";

    private final int step;
    private final String msg;
    private final Throwable cas;

    public TestExecutionException(int step, String message, Throwable cas) {
        super(message, cas);
        this.step = step;
        this.msg = message;
        this.cas = cas;
    }

    public String getExplanation() {
        String nl = System.lineSeparator();
        return new StringBuilder(INDEX).append("step[").append(step).append("] : ")
                .append(msg).append(" : ").append(nl)
                .append(INDEX)
                .append(cas.getClass().getCanonicalName()).append("[").append(cas.getMessage()).append("]")
                .append(nl)
                .toString();
    }
}
