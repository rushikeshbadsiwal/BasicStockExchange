package util;

public class KeyLoadTime<A> {
    A key;
    Long timeInMillis;
    KeyLoadTime(A key, Long timeInMillis){
        this.key = key;
        this.timeInMillis = timeInMillis;
    }
}
