package util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CacheSegment<T, R> {
    Map<T, ValueLoadTime<R>> map= new HashMap<>();
    Queue<KeyLoadTime<T>> queue = new LinkedList<>();
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public Map<T, ValueLoadTime<R>> getMap() {
        return map;
    }

    public void setMap(Map<T, ValueLoadTime<R>> map) {
        this.map = map;
    }

    public Queue<KeyLoadTime<T>> getQueue() {
        return queue;
    }

    public void setQueue(Queue<KeyLoadTime<T>> queue) {
        this.queue = queue;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
