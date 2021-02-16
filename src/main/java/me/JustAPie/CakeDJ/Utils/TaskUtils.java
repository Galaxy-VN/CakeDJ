package me.JustAPie.CakeDJ.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class TaskUtils {
    public static void setInterval(TimerTask task, long interval) {
        new Timer().scheduleAtFixedRate(task, 0, interval);
    }

    public static void setTimeout(Runnable runnable, long delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception ignored) {}
        }).start();
    }
}
