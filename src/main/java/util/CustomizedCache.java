package util;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;


public class CustomizedCache<T, R> {
    private int maxSize;
    private int refreshTime;
    private int numberOfSegments;
    private Method fetcherMethod;
    private Object fetcherClassObject;
    private CacheType cacheType = CacheType.WRITECACHE;
    private List<Map<T, Pair<R, Long>>> maps = new ArrayList<>();
    private List<Queue<Pair<T, Long>>> queues = new ArrayList<>();
    private List<ExecutorService> executorServices = new ArrayList<>();

    public CustomizedCache(int maxSize, int refreshTime, CacheType cacheType, int numberOfSegments, Method fetcherMethod, Object fetcherClassObject) {
        this.maxSize = maxSize;
        this.refreshTime = refreshTime;
        this.numberOfSegments = numberOfSegments;
        this.fetcherMethod = fetcherMethod;
        this.fetcherClassObject = fetcherClassObject;
        this.cacheType = cacheType;
        initCustomizedCache();
    }

    private void initCustomizedCache() {
        for (int i = 0; i < numberOfSegments; i++) {
            maps.add(new HashMap<>());
            queues.add(new LinkedList<>());
            executorServices.add(Executors.newSingleThreadExecutor());
        }
    }

    public R getValue(T key) throws ExecutionException, InterruptedException {
        if (maps.get(key.hashCode()).get(key) != null && isValueInValidated(maps.get(key.hashCode()).get(key).second)) {
            maps.get(key.hashCode()).remove(key);
        }

        if (maps.get(key.hashCode()).get(key) != null) {
            return maps.get(key.hashCode()).get(key).first;
        }

        Future<?> future = executorServices.get(key.hashCode()).submit(() -> {

            if (maps.get(key.hashCode()).get(key) != null) {
                return maps.get(key.hashCode()).get(key).first;
            }

            if(maps.get(key.hashCode()).size() == maxSize){
                freeUpSpace(key);
            }
            return fetcherMethod.invoke(fetcherClassObject, key);
        });
        maps.get(key.hashCode()).put(key,new Pair((R)future.get(),System.currentTimeMillis()));
        return maps.get(key.hashCode()).get(key).first;
    }

    private void freeUpSpace(T key) {
        Pair<T, Long> queueTopElement = queues.get(key.hashCode()).peek();
        while(!queueTopElement.second.equals(maps.get(key.hashCode()).get(queueTopElement.first).second)){
            queues.get(key.hashCode()).remove();
            queueTopElement = queues.get(key.hashCode()).peek();
        }
        queues.get(key.hashCode()).remove();
        maps.get(key.hashCode()).remove(queueTopElement.first);
    }


    private boolean isValueInValidated(Long time) {
        return (System.currentTimeMillis() - time) > (refreshTime);
    }

    private Pair<R, Long> fetchValue(T key) {
        return null;
    }
}
