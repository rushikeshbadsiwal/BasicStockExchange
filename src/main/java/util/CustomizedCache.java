package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;


public class CustomizedCache<T, R> {
    private int maxSize;
    private int refreshTime;
    private int numberOfSegments;
    private Method fetcherMethod;
    private Object fetcherClassObject;
    private CacheType cacheType = CacheType.WRITECACHE;
    private ArrayList<CacheSegment<T, R>> segments;
    private Function<T,R> function;

    public CustomizedCache(int maxSize, int refreshTime, CacheType cacheType, int numberOfSegments, Method fetcherMethod, Object fetcherClassObject) {
        this.maxSize = maxSize;
        this.refreshTime = refreshTime;
        this.numberOfSegments = numberOfSegments;
        this.fetcherMethod = fetcherMethod;
        this.fetcherClassObject = fetcherClassObject;
        this.cacheType = cacheType;
        segments = new ArrayList<>(numberOfSegments);
    }

    public CustomizedCache(int maxSize, int refreshTime, CacheType cacheType, int numberOfSegments, Function<T,R> function) {
        this.maxSize = maxSize;
        this.refreshTime = refreshTime;
        this.numberOfSegments = numberOfSegments;
        this.function = function;
        this.cacheType = cacheType;
        segments = new ArrayList<>(numberOfSegments);
    }

    public R getValue(T key) throws InvocationTargetException, IllegalAccessException {
        if(isValueValid(key)){
            return segments.get(getSegmentMapping(key)).map.get(key).value;
        }
        return getValueFromDataSource(key);
    }

    private R getValueFromDataSource(T key) throws InvocationTargetException, IllegalAccessException {
        synchronized(segments.get(getSegmentMapping(key))){
            if(isValueValid(key)){
                return segments.get(getSegmentMapping(key)).map.get(key).value;
            }
            if(segments.get(getSegmentMapping(key)).map.get(key) != null){
                segments.get(getSegmentMapping(key)).map.remove(key);
                removeElementFromQueue(key);
            }
            if(segments.get(getSegmentMapping(key)).map.size() >= maxSize ){
                freeUpSpace();
            }
            Long currentTime = System.currentTimeMillis();
//            segments.get(getSegmentMapping(key)).map.put(key, new ValueLoadTime<>((R)fetcherMethod.invoke(fetcherClassObject, key), currentTime));
            segments.get(getSegmentMapping(key)).map.put(key, new ValueLoadTime<>(function.apply(key), currentTime));
            insertIntoQueue(key, currentTime);
        }
        return null;
    }

    //will remove the least recent used element
    private void freeUpSpace() {
//        Pair<T, Long> queueTopElement = queues.get(key.hashCode()).peek();
//        while(!queueTopElement.second.equals(maps.get(key.hashCode()).get(queueTopElement.first).second)){
//            queues.get(key.hashCode()).remove();
//            queueTopElement = queues.get(key.hashCode()).peek();
//        }
//        queues.get(key.hashCode()).remove();
//        maps.get(key.hashCode()).remove(queueTopElement.first);
    }

    //will remove given key element from the queue
    private void removeElementFromQueue(T key){

    }
    //will insert KeyLoadTime for given key and currentTime into queue
    private void insertIntoQueue(T key, Long currentTime) {
    }

    private int getSegmentMapping(T key) {
        return (key.hashCode() + numberOfSegments) % numberOfSegments;
    }

    private boolean isValueValid(T key) {
        if(segments.get(getSegmentMapping(key)).map.get(key) == null)
            return false;
        return (System.currentTimeMillis() - segments.get(getSegmentMapping(key)).map.get(key).timeInMillis) < (refreshTime);
    }
}
