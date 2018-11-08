package net.yaccob.intervals.api;

import com.google.common.collect.ComparisonChain;

public final class Interval<T extends Comparable<T>> implements Comparable<Interval<T>> {
    private T first;
    private T last;

    /**
     * Construct an interval with lower value as <code>first</code> and higher value as <code>last</code>
     *
     * @param first One boundary - doesn't matter if lower or higher boundary
     * @param last  The other boundary
     */
    public Interval(T first, T last) {
        if (first.compareTo(last) <= 0) {
            this.first = first;
            this.last = last;
        } else { // reverse order -> normalize it
            this.first = last;
            this.last = first;
        }
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

    public boolean overlapsWith(Interval<T> other) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Check if a value falls into an interval.
     * Functionally this method isn't directly needed for the requirement of joining intervals.
     * I added it to define a clear semantics of an interval.
     *
     * @param value That's the value that will be checked for being covered by this interval.
     * @return <code>true</code> if the value is covered by this interval, otherwise <code>false</code>.
     */
    public boolean covers(T value) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Interval<T> mergeWith(Interval<T> other) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
