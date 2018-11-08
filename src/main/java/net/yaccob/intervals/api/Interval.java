package net.yaccob.intervals.api;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

import java.util.Objects;

public final class Interval<T extends Comparable<T>> implements Comparable<Interval<T>> {
    private final T first;
    private final T last;

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

    public boolean overlapsWith(Interval<T> other) {
        return this.last.compareTo(other.first) >= 0;
    }

    public Interval<T> mergeWith(Interval<T> other) {
        Preconditions.checkArgument(this.overlapsWith(other));
        T first = this.first.compareTo(other.first) <= 0 ? this.first : other.first;
        T last = this.last.compareTo(other.last) >= 0 ? this.last : other.last;
        return new Interval<>(first, last);
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
        return value.compareTo(this.first) >= 0 && value.compareTo(this.last) < 0;
    }

    @Override
    public int compareTo(Interval<T> other) {
        return ComparisonChain.start()
                .compare(this.first, other.first)
                .compare(this.last, other.last)
                .result();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("first", first)
                .add("last", last)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interval<?> interval = (Interval<?>) o;
        return Objects.equals(first, interval.first) &&
                Objects.equals(last, interval.last);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, last);
    }
}
