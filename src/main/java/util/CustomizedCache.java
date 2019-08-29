package util;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


public class CustomizedCache<T, R> {
    private int numberOfSegments;
    private List<CacheSegment<T, R>> segments;

    public CustomizedCache(int maxSize, int refreshTime, TimeUnit unit, int numberOfSegments, Function<T, R> function) {
        this.numberOfSegments = numberOfSegments;
        this.segments = createSegments(numberOfSegments, maxSize, unit.toMillis(refreshTime), function);
    }

    private List<CacheSegment<T, R>> createSegments(int numberOfSegments, int maxSize, long refreshTimeMillis, Function<T, R> function) {
        List<CacheSegment<T, R>> segments = new ArrayList<>(numberOfSegments);
        for (int i = 0; i < numberOfSegments; i++) {
            segments.add(new CacheSegment<>(refreshTimeMillis, function, maxSize / numberOfSegments));
        }
        return segments;
    }

    public R getValue(T key) {
        CacheSegment<T, R> segment = segments.get(getSegmentMapping(key));
        return segment.getValue(key);
    }

    //will insert KeyLoadTime for given key and currentTime into queue

    private int getSegmentMapping(T key) {
        return (Math.abs(key.hashCode())) % numberOfSegments;
    }

}
