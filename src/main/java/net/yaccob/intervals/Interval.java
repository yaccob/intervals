package net.yaccob.intervals;

import com.google.common.collect.ComparisonChain;

public final class Interval<T extends Comparable<T>> implements Comparable<Interval<T>> {
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
    public int compareTo(Interval other) {
        return ComparisonChain.start()
                .compare(this.first, other.first)
                .compare(this.last, other.last)
                .result();
    }
}
