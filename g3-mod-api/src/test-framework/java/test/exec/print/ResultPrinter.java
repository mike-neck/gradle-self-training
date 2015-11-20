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

import test.exec.TestResults;
import test.result.Accident;
import test.result.Failure;
import test.result.Panic;
import test.result.Result;
import test.result.Success;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static test.exec.print.ColorPrinter.greenln;
import static test.exec.print.ColorPrinter.normalln;
import static test.exec.print.ColorPrinter.redln;
import static test.exec.print.ColorPrinter.yellowln;

public class ResultPrinter implements UnaryOperator<List<TestResults>> {

    @Override
    public List<TestResults> apply(List<TestResults> list) {
        list.stream()
                .map(TO_PRINTER)
                .forEach(Printer::print);
        return list;
    }

    private static final Function<TestResults, Printer> TO_PRINTER = tr ->
            tr.allSuccess() ? success(tr) : failure(tr);

    private static Printer success(TestResults tr) {
        return new SuccessPrinter(tr);
    }

    private static Printer failure(TestResults tr) {
        return new FailurePrinter(tr);
    }

    private interface Printer {
        void print();
    }

    private static abstract class AbstractPrinter implements Printer {

        protected final TestResults results;

        protected AbstractPrinter(TestResults results) {
            this.results = results;
        }
    }

    private static class SuccessPrinter extends AbstractPrinter {

        SuccessPrinter(TestResults results) {
            super(results);
        }

        @Override
        public void print() {
            // クラス名(緑)
            greenln(results.getClassName());

            // 以下繰り返し
            //     - テスト名(緑)
            results.getResults().keySet().stream()
                    .map(name -> "    - " + name)
                    .forEach(ColorPrinter::greenln);
        }
    }

    private static class FailurePrinter extends AbstractPrinter {

        FailurePrinter(TestResults results) {
            super(results);
        }

        @Override
        public void print() {
            // クラス名(赤)
            redln(results.getClassName());

            // 以下繰り返し
            // Failureの場合
            //     - テスト名(赤)
            // diff(赤)
            // Accidentの場合
            //     - テスト名(黄)
            // explanation(normal)
            // Panicの場合
            //     - テスト名(黄)
            // cause(normal)
            results.getResults().entrySet().stream()
                    .map(e -> new FailType(e.getKey(), e.getValue()))
                    .forEach(FailType::print);
        }
    }

    private static class FailType {

        private String testName;

        private Result result;

        FailType(String testName, Result result) {
            this.testName = testName;
            this.result = result;
        }

        void print() {
            String nameLine = "    - " + testName;
            // Successの場合
            //     - テスト名(緑)
            if (result instanceof Success) {
                greenln(nameLine);
                // Failureの場合
                //     - テスト名(赤)
                // diff(赤)
            } else if (result instanceof Failure) {
                redln(nameLine);
                normalln(((Failure) result).getDiff());
            // Accidentの場合
            //     - テスト名(黄)
            // explanation(normal)
            // Panicの場合
            //     - テスト名(黄)
            // cause(normal)
            } else  {
                yellowln(nameLine);
                if (result instanceof Accident) {
                    normalln(((Accident) result).getExplanation());
                } else {
                    normalln(((Panic) result).getCause());
                }
            }
        }
    }
}
