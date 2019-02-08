package com.oppalove.util;

public class OpTask {
    private final String name;
    private long nanoTime;

    OpTask(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    long getElapsedTime() {
        return nanoTime;
    }

    void setElapsedTime(long time) {
        this.nanoTime = time;
    }
}
