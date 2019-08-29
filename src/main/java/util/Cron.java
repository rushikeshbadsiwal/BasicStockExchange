package util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Cron {

    private static final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    public static void schedule(long period, TimeUnit unit, Runnable task) {
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
