package com.oppalove.util;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class OpStopWatchTest {

    @Test
    public void example() throws InterruptedException {

        OpStopWatch opStopWatch = new OpStopWatch("MyStopWatch");

        opStopWatch.start("Task1");
        // do
        Thread.sleep(1500);
        opStopWatch.stop();

        opStopWatch.start("Task2");
        // do
        Thread.sleep(1000);
        opStopWatch.stop();

        opStopWatch.start("Task3");
        // do
        Thread.sleep(2500);
        opStopWatch.stop();

        System.out.println(opStopWatch.totalTime(TimeUnit.NANOSECONDS));
        System.out.println(opStopWatch.totalTime(TimeUnit.MILLISECONDS));
        System.out.println(opStopWatch.totalTime(TimeUnit.SECONDS));
        System.out.println(opStopWatch.totalTime(TimeUnit.MINUTES));
        System.out.println(opStopWatch.totalTime(TimeUnit.HOURS));
        System.out.println(opStopWatch.list(TimeUnit.MILLISECONDS));
        System.out.println(opStopWatch.average(TimeUnit.MILLISECONDS));
        System.out.println(opStopWatch.max(TimeUnit.MILLISECONDS));
        System.out.println(opStopWatch.min(TimeUnit.MILLISECONDS));
        System.out.println(opStopWatch.report(TimeUnit.MILLISECONDS));
        System.out.println(opStopWatch.report(TimeUnit.SECONDS));
    }

    @Test
    public void getMaxTask() throws InterruptedException {
        OpStopWatch opStopWatch = OpStopWatch.createAndRun("Max", "task1");
        Thread.sleep(10);
        opStopWatch.stop();

        opStopWatch.start("task2");
        Thread.sleep(100);
        opStopWatch.stop();

        OpTask maxTask = opStopWatch.getMaxTask();
        assertEquals("task2", maxTask.getName());
    }

    @Test
    public void getMinTask() throws InterruptedException {
        OpStopWatch opStopWatch = OpStopWatch.createAndRun("Max", "task1");
        Thread.sleep(10);
        opStopWatch.stop();

        opStopWatch.start("task2");
        Thread.sleep(100);
        opStopWatch.stop();

        OpTask minTask = opStopWatch.getMinTask();
        assertEquals("task1", minTask.getName());
    }

    @Test
    public void staticTest() throws InterruptedException {

        OpStopWatch opStopWatch = OpStopWatch.createAndRun();
        // do
        Thread.sleep(10);
        opStopWatch.stop();
        System.out.println(opStopWatch.report(TimeUnit.MILLISECONDS));


        opStopWatch = OpStopWatch.createAndRun("static name");
        // do
        Thread.sleep(10);
        opStopWatch.stop();
        System.out.println(opStopWatch.report(TimeUnit.MILLISECONDS));

        opStopWatch = OpStopWatch.createAndRun("static name", "task name");
        // do
        Thread.sleep(10);
        opStopWatch.stop();
        System.out.println(opStopWatch.report(TimeUnit.MILLISECONDS));
    }

    @Test
    public void runningException() {
        assertThrows(IllegalStateException.class, () -> {
            OpStopWatch opStopWatch = new OpStopWatch("MyStopWatch");
            opStopWatch.start("Task1");
            opStopWatch.start("Task2");
        });
    }

    @Test
    public void customSummary() {
        OpStopWatch opStopWatch = new OpStopWatch("MyStopWatch", new MySummary());
        opStopWatch.start();
        // do something
        opStopWatch.stop();

        System.out.println(opStopWatch.report(TimeUnit.MILLISECONDS));
    }

    class MySummary implements Summary {
        @Override
        public String summaryString(OpStopWatch stopWatch, TimeUnit timeUnit) {
            return "Hi";
        }
    }
}