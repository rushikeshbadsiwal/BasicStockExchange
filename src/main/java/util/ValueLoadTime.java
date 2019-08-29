package util;

public class ValueLoadTime<A> {
    A value;
    Long timeInMillis;
    ValueLoadTime(A value, Long timeInMillis){
        this.value = value;
        this.timeInMillis = timeInMillis;
    }
}
