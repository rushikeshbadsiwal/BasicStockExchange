package util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Cron {

    private final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    private final long period;
    private final TimeUnit unit;
    private final Runnable task;

    public Cron(long period, TimeUnit unit, Runnable task) {
        this.period = period;
        this.unit = unit;
        this.task = task;
    }

    public void start(){
        ses.scheduleAtFixedRate(safelyRun(task), 0, period, unit);
    }

    private static Runnable safelyRun(Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}
