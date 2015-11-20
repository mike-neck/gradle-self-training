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

import test.Test;
import test.exec.print.ResultPrinter;
import test.result.Statistics;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

import static test.exec.print.ColorPrinter.green;
import static test.exec.print.ColorPrinter.greenln;
import static test.exec.print.ColorPrinter.normal;
import static test.exec.print.ColorPrinter.normalln;
import static test.exec.print.ColorPrinter.red;
import static test.exec.print.ColorPrinter.redln;
import static test.exec.print.ColorPrinter.yellowln;

public class ExecutionManager {

    private final ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }
    });

    private final long started;

    private final Set<Class<? extends Test>> tests;

    private final Queue<TestResults> queue;

    public ExecutionManager(Set<Class<? extends Test>> tests) {
        this.tests = tests;
        this.queue = new ConcurrentLinkedQueue<>();
        this.started = System.nanoTime();
    }

    public void execute() {
        CompletableFuture<Void> future = new TestExecutor(exec, tests, queue).run();
        CompletableFuture<List<TestResults>> sorted = future.thenApplyAsync(new Sorter(queue).sortResult());
        // 結果表示
        CompletableFuture<List<TestResults>> printed = sorted.thenApply(new ResultPrinter());
        // 集計
        CompletableFuture<Statistics> summary = printed.thenApply(Statistics.GET_STATISTICS);
        // 集計結果表示
        CompletableFuture<Void> finish = summary.thenAccept(SHOW_STATISTICS);
        finish.join();
        // 経過時間表示
        showTime();
    }

    private void showTime() {
        long time = System.nanoTime() - started;
        long millisec = time / 1_000_000;
        long sec = millisec / 1_000;
        long min = sec / 60;
        StringBuilder sb = new StringBuilder();
        if (min > 0) {
            sb.append(min).append(" min ");
        }
        long second = sec - min * 60;
        long milli = millisec - sec * 1_000 - min * 60_1000;
        sb.append(String.format("%02d.%03d sec", second, milli));
        normalln(sb.toString());
    }

    private static final Consumer<Statistics> SHOW_STATISTICS = s -> {
        String total = "total : " + s.getTotal();
        String success = "success : " + s.getSuccess();
        String failure = "failure : " + s.getFailure();
        String error = "error : " + s.getError();

        normal(total);
        green(success);
        red(failure);
        yellowln(error);
        normalln("");

        boolean state = s.isTestSuccess();
        String result = state ? "TEST SUCCEEDED" : "TEST FAILED";
        if (state) {
            greenln(result);
        } else {
            redln(result);
        }
        normalln("");
    };
}
