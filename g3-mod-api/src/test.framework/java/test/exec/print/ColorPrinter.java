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
package test.exec.print;

public final class ColorPrinter {

    private ColorPrinter() {
        throw new UnsupportedOperationException();
    }

    private static final Object LOCK = new Object();

    private static final char ESC = (char) 27;

    private static final String BLACK = "[30m";

    private static final String RED = "[31m";

    private static final String GREEN = "[32m";

    private static final String YELLOW = "[33m";

    private static final String BLUE = "[34m";

    private static final String PINK = "[35m";

    private static final String CYAN = "[36m";

    private static final String WHITE = "[37m";

    private static final String CLEAR = "[0m";

    private static void print(String txt) {
        synchronized (LOCK) {
            System.out.print(txt);
        }
    }

    private static void println(String txt) {
        synchronized (LOCK) {
            System.out.println(txt);
        }
    }

    public static void black(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(BLACK).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        print(txt);
    }

    public static void blackln(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(BLACK).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        println(txt);
    }

    public static void red(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(RED).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        print(txt);
    }

    public static void redln(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(RED).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        println(txt);
    }

    public static void green(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(GREEN).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        print(txt);
    }

    public static void greenln(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(GREEN).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        println(txt);
    }

    public static void yellow(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(YELLOW).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        print(txt);
    }

    public static void yellowln(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(YELLOW).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        println(txt);
    }

    public static void blue(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(BLUE).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        print(txt);
    }

    public static void blueln(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(BLUE).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        println(txt);
    }

    public static void pink(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(PINK).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        print(txt);
    }

    public static void pinkln(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(PINK).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        println(txt);
    }

    public static void cyan(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(CYAN).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        print(txt);
    }

    public static void cyanln(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(CYAN).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        println(txt);
    }

    public static void white(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(WHITE).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        print(txt);
    }

    public static void whiteln(String msg) {
        String txt = new StringBuilder()
                .append(ESC).append(WHITE).append(msg)
                .append(ESC).append(CLEAR).append(" ")
                .toString();
        println(txt);
    }

    public static void normal(String msg) {
        print(msg + " ");
    }

    public static void normalln(String msg) {
        println(msg + " ");
    }
}
