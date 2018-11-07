package net.yaccob.intervals;

public final class Interval<T extends Comparable<T>> implements Comparable<T> {
    private T first;
    private T last;

    public Interval(T first, T last) {
        this.first = first;
        this.last = last;
    }

    public T getFirst() {
        return first;
    }

    public T getLast() {
        return last;
    }

    @Override
    public int compareTo(T o) {
        return first.compareTo(last);
    }
}
