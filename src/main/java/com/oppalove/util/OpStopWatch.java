package com.oppalove.util;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

/**
 * Main class for stopwatch.
 */
public class OpStopWatch {

    private final Comparator<OpTask> comparator = Comparator.comparing(OpTask::getElapsedTime);
    private String name;
    private List<OpTask> opTaskList = new LinkedList<OpTask>();
    private OpTask currentTask;
    private long startNanoTime;
    private Summary summary;

    /**
     * Start stopwatch with default summary.
     */
    public OpStopWatch() {
        summary = new DefaultSummary();
    }

    /**
     * Start stopwatch with name.
     *
     * @param name name of stopwatch
     */
    public OpStopWatch(String name) {
        this.name = name;
        summary = new DefaultSummary();
    }

    /**
     * Start stopwatch with summary interface and name.
     *
     * @param name    name of stopwatch
     * @param summary implementation reference of summary
     */
    public OpStopWatch(String name, Summary summary) {
        this.name = name;
        this.summary = summary;
    }

    /**
     * Create OpStopWatch and run.
     *
     * @return new instance of stopwatch
     */
    public static OpStopWatch createAndRun() {
        return new OpStopWatch().start();
    }

    /**
     * Create OpStopWatch with name and run.
     *
     * @return new instance of stopwatch
     */
    public static OpStopWatch createAndRun(String name) {
        return new OpStopWatch(name).start();
    }

    /**
     * Create OpStopWatch with name and task name and run.
     *
     * @param name     name of stopwatch
     * @param taskName name of task
     * @return new instance of stopwatch
     */
    public static OpStopWatch createAndRun(String name, String taskName) {
        return new OpStopWatch(name).start(taskName);
    }

    static double convertTimeUnit(double amount, TimeUnit from, TimeUnit to) {
        return Math.floor(amount / from.convert(1, to) * 100) / 100.0d;
    }

    /**
     * Start stopwatch.
     *
     * @return OpStopWatch which has new task
     */
    public OpStopWatch start() {
        return start(null);
    }

    /**
     * Start stopwatch with taskName.
     *
     * @param taskName name of task
     * @return OpStopWatch which has new task
     */
    public OpStopWatch start(String taskName) {
        checkStatus();
        startNanoTime = System.nanoTime();
        currentTask = new OpTask(taskName);
        return this;
    }

    /**
     * Stop stopwatch.
     */
    public void stop() {
        long elapsedTime = System.nanoTime() - startNanoTime;
        currentTask.setElapsedTime(elapsedTime);
        opTaskList.add(currentTask);
        reset();
    }

    /**
     * Returns all task.
     *
     * @return list of task
     */
    public List<OpTask> getTasks() {
        return opTaskList;
    }

    /**
     * Returns total amount time to complete all tasks.
     *
     * @param timeUnit set of time unit that it returns to. {@link TimeUnit}
     * @return total amount time
     */
    public double totalTime(TimeUnit timeUnit) {
        return opTaskList.stream()
                .map(task -> convertTimeUnit(task.getElapsedTime(), TimeUnit.NANOSECONDS, timeUnit))
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    /**
     * Returns average time to complete all tasks.
     *
     * @param timeUnit set of time unit that it returns to. {@link TimeUnit}
     * @return tatal average time
     */
    public double average(TimeUnit timeUnit) {
        double elapsed = opTaskList.stream()
                .map(OpTask::getElapsedTime)
                .mapToLong(Long::longValue)
                .average()
                .orElse(0);
        return convertTimeUnit(elapsed, TimeUnit.NANOSECONDS, timeUnit);
    }

    /**
     * Returns longest task time to complete its task.
     *
     * @param timeUnit set of time unit that it returns to. {@link TimeUnit}
     * @return the longest task time it took.
     */
    public double max(TimeUnit timeUnit) {
        return opTaskList.stream()
                .map(task -> convertTimeUnit(task.getElapsedTime(), TimeUnit.NANOSECONDS, timeUnit))
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0);
    }

    /**
     * Returns task which took longest time.
     *
     * @return OpTask
     */
    public OpTask getMaxTask() {
        return Collections.max(opTaskList, comparator);
    }

    /**
     * Returns shortest task time to complete its task.
     *
     * @param timeUnit set of time unit that it returns to. {@link TimeUnit}
     * @return the shortest task time it took.
     */
    public double min(TimeUnit timeUnit) {
        return opTaskList.stream()
                .map(task -> convertTimeUnit(task.getElapsedTime(), TimeUnit.NANOSECONDS, timeUnit))
                .mapToDouble(Double::doubleValue)
                .min()
                .orElse(0);
    }

    /**
     * Returns task which took shortest time.
     *
     * @return OpTask
     */
    public OpTask getMinTask() {
        return Collections.min(opTaskList, comparator);
    }

    /**
     * Returns list of time to complete all task took.
     *
     * @param timeUnit set of time unit that it returns to. {@link TimeUnit}
     * @return list of time
     */
    public List<Double> list(TimeUnit timeUnit) {
        return opTaskList.stream()
                .map(task -> convertTimeUnit(task.getElapsedTime(), TimeUnit.NANOSECONDS, timeUnit))
                .mapToDouble(Double::doubleValue)
                .boxed()
                .collect(toList());
    }

    /**
     * Print summarize all tasks info.
     *
     * @param timeUnit set of time unit that it returns to. {@link TimeUnit}
     * @return report information.
     */
    public String report(TimeUnit timeUnit) {
        return summary.summaryString(this, timeUnit);
    }

    /**
     * Returns name of stopwatch.
     *
     * @return name of stopwatch.
     */
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
