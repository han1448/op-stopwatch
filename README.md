# op-stopwatch

### Getting started
This is simple stop watch tool which provides information about min, max, average etc.
And it also provides summarized information.

### Run
```java
OpStopWatch opStopWatch = new OpStopWatch("MyStopWatch");
opStopWatch.start("Task1");
// do something
opStopWatch.stop();
```

#### Use multiple watch
```java
OpStopWatch opStopWatch = new OpStopWatch("MyStopWatch");
opStopWatch.start("Task1");
// do something
opStopWatch.stop();
opStopWatch.start("Task2");
// do something
opStopWatch.stop();
```

#### Use static method
##### default static method
```java
OpStopWatch opStopWatch = OpStopWatch.createAndRun();
// do something
opStopWatch.stop();
```
##### with watch name
```java
OpStopWatch opStopWatch = OpStopWatch.createAndRun("MyWatch");
// do something
opStopWatch.stop();
```

##### with task name
```java
OpStopWatch opStopWatch = OpStopWatch.createAndRun("MyWatch", "MyTask");
// do something
opStopWatch.stop();
```

### Summarize
#### getTasks
get list of task
```java
List<Optask> tasks = opStopWatch.getTasks();
```

#### totalTime
Return total amount time
```java
System.out.println(opStopWatch.totalTime(TimeUnit.MILLISECONDS));
-->
1000

```

#### list
Return execution time of each task 
```java
System.out.println(opStopWatch.list(TimeUnit.MILLISECONDS));
-->
[1503.08, 1000.24, 2505.12]

```

#### average
Return average execution time of all tasks
```java
System.out.println(opStopWatch.average(TimeUnit.MILLISECONDS));
-->
1669.48
```

#### min
Return min value of all tasks
```java
System.out.println(opStopWatch.min(TimeUnit.MILLISECONDS));
-->
12.43
```

#### getMaxTask
Return optask with the oldest time
```java
OpTask maxOpTask = opStopWatch.getMaxTask();
```

#### max
Return max value of all tasks
```java
System.out.println(opStopWatch.max(TimeUnit.MILLISECONDS));
-->
2442.12
```

#### getMinTask
Return optask with the shortest test time
```java
OpTask minOpTask = opStopWatch.getMinTask();
```

#### report
Show summary information of all tasks.
```java
System.out.println(opStopWatch.report(TimeUnit.MILLISECONDS));
-->
MyStopWatch (MILLISECONDS)
===========================
Task1		1503.08
Task2		1000.24
Task3		2505.12
Total : 5008.44, Average : 1669.48, Min : 1000.24, Max : 2505.12
```

### Custom summarize view
Implement Summary interface.
```java
public class MySummary implements Summary {
    @Override
    public String summaryString(OpStopWatch stopWatch, TimeUnit timeUnit) {
        System.out.println("my custom summary");
    }
}
```

Pass the instance to constructor when you create opstopwatch instance.
```java
OpStopWatch opStopWatch = new OpStopWatch("MyStopWatch", new MySummary());

```