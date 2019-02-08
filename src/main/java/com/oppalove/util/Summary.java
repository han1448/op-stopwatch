package com.oppalove.util;

import java.util.concurrent.TimeUnit;

public interface Summary {
    public String summaryString(OpStopWatch stopWatch, TimeUnit timeUnit);
}
