package com.oppalove.util;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

public class OpStopWatch {

    private final Comparator<OpTask> comparator = Comparator.comparing(OpTask::getElapsedTime);
    private String name;
    private List<OpTask> opTaskList = new LinkedList<OpTask>();
    private OpTask currentTask;
    private long startNanoTime;
    private Summary summary;


    public OpStopWatch() {
        summary = new DefaultSummary();
    }

    public OpStopWatch(String name) {
        this.name = name;
        summary = new DefaultSummary();
    }

    public OpStopWatch(String name, Summary summary) {
        this.name = name;
        this.summary = summary;
    }

    public static OpStopWatch createAndRun() {
        return new OpStopWatch().start();
    }

    public static OpStopWatch createAndRun(String name) {
        return new OpStopWatch(name).start();
    }

    public static OpStopWatch createAndRun(String name, String taskName) {
        return new OpStopWatch(name).start(taskName);
    }

    static double convertTimeUnit(double amount, TimeUnit from, TimeUnit to) {
        return Math.floor(amount / from.convert(1, to) * 100) / 100.0d;
    }

    public OpStopWatch start() {
        return start(null);
    }

    public OpStopWatch start(String taskName) {
        checkStatus();
        startNanoTime = System.nanoTime();
        currentTask = new OpTask(taskName);
        return this;
    }

    public void stop() {
        long elapsedTime = System.nanoTime() - startNanoTime;
        currentTask.setElapsedTime(elapsedTime);
        opTaskList.add(currentTask);
        reset();
    }

    public List<OpTask> getTasks() {
        return opTaskList;
    }

    public double totalTime(TimeUnit timeUnit) {
        return opTaskList.stream()
                .map(task -> convertTimeUnit(task.getElapsedTime(), TimeUnit.NANOSECONDS, timeUnit))
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public double average(TimeUnit timeUnit) {
        double elapsed = opTaskList.stream()
                .map(OpTask::getElapsedTime)
                .mapToLong(Long::longValue)
                .average()
                .orElse(0);
        return convertTimeUnit(elapsed, TimeUnit.NANOSECONDS, timeUnit);
    }

    public double max(TimeUnit timeUnit) {
        return opTaskList.stream()
                .map(task -> convertTimeUnit(task.getElapsedTime(), TimeUnit.NANOSECONDS, timeUnit))
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0);
    }

    public OpTask getMaxTask() {
        return Collections.max(opTaskList, comparator);
    }

    public double min(TimeUnit timeUnit) {
        return opTaskList.stream()
                .map(task -> convertTimeUnit(task.getElapsedTime(), TimeUnit.NANOSECONDS, timeUnit))
                .mapToDouble(Double::doubleValue)
                .min()
                .orElse(0);
    }

    public OpTask getMinTask() {
        return Collections.min(opTaskList, comparator);
    }

    public List<Double> list(TimeUnit timeUnit) {
        return opTaskList.stream()
                .map(task -> convertTimeUnit(task.getElapsedTime(), TimeUnit.NANOSECONDS, timeUnit))
                .mapToDouble(Double::doubleValue)
                .boxed()
                .collect(toList());
    }

    public String report(TimeUnit timeUnit) {
        return summary.summaryString(this, timeUnit);
    }

    public String getName() {
        return name;
    }

    private void reset() {
        startNanoTime = 0l;
        currentTask = null;
    }

    private void checkStatus() {
        if (isRunning())
            throw new IllegalStateException("Task is running.");
    }

    private boolean isRunning() {
        return Objects.nonNull(currentTask);
    }
}
