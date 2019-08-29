package util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class CacheSegment<T, R> {
    Map<T, ValueLoadTime<R>> map = new HashMap<>();
    Queue<KeyLoadTime<T>> queue = new LinkedList<>();
    private long refreshTimeMillis;
    private Function<T, R> function;
    private final int size;

    public CacheSegment(long refreshTimeMillis, Function<T, R> function, int size) {
        this.refreshTimeMillis = refreshTimeMillis;
        this.function = function;
        this.size = size;
    }

    public R getValue(T key) {
        if (isValueValid(key)) {
            System.out.println("key in cache" + key + " object is "+ map.get(key).value);
            return map.get(key).value;
        }
        return getValueFromDataSource(key);
    }

    private synchronized R getValueFromDataSource(T key) {
        if (isValueValid(key)) {
            return map.get(key).value;
        }
        if (map.get(key) != null) {
            map.remove(key);
            removeElementFromQueue(key);
        }
        if (map.size() >= size) {
            freeUpSpace();
        }
        Long currentTime = System.currentTimeMillis();
        System.out.println("key not in cache, fetching from db " + key);
        R r = function.apply(key);
        map.put(key, new ValueLoadTime<>(r, currentTime));
        insertIntoQueue(key, currentTime);
        return r;
    }

    private void removeElementFromQueue(T key) {

    }

    private void freeUpSpace() {

    }

    private void insertIntoQueue(T key, Long currentTime) {

    }


    private boolean isValueValid(T key) {
        if (map.get(key) == null) {
            return false;
        }
        return (System.currentTimeMillis() - map.get(key).timeInMillis) < (refreshTimeMillis);
    }

}
