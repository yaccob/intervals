package net.yaccob.intervals.api;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

import java.util.Objects;

public final class Interval<T extends Comparable<T>> implements Comparable<Interval<T>> {
    private final T lowerBoundInclusive;
    private final T upperBoundExclusive;

    /**
     * Construct an interval with lower value as <code>lowerBoundInclusive</code> and higher value as <code>upperBoundExclusive</code>
     *
     * @param oneBound One boundary - doesn't matter if lower or higher boundary
     * @param otherBound  The other boundary
     */
    public Interval(T oneBound, T otherBound) {
        if (oneBound.compareTo(otherBound) <= 0) {
            this.lowerBoundInclusive = oneBound;
            this.upperBoundExclusive = otherBound;
        } else { // reverse order -> normalize it
            this.lowerBoundInclusive = otherBound;
            this.upperBoundExclusive = oneBound;
        }
    }

    public T getLowerBoundInclusive() {
        return lowerBoundInclusive;
    }

    public T getUpperBoundExclusive() {
        return upperBoundExclusive;
    }

    public boolean overlapsWith(Interval<T> other) {
        return this.upperBoundExclusive.compareTo(other.lowerBoundInclusive) >= 0
                && other.upperBoundExclusive.compareTo(this.lowerBoundInclusive) >= 0;
    }

    public Interval<T> mergeWith(Interval<T> other) {
        Preconditions.checkArgument(this.overlapsWith(other));
        T first = this.lowerBoundInclusive.compareTo(other.lowerBoundInclusive) <= 0 ? this.lowerBoundInclusive : other.lowerBoundInclusive;
        T last = this.upperBoundExclusive.compareTo(other.upperBoundExclusive) >= 0 ? this.upperBoundExclusive : other.upperBoundExclusive;
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
        return value.compareTo(this.lowerBoundInclusive) >= 0 && value.compareTo(this.upperBoundExclusive) < 0;
    }

    @Override
    public int compareTo(Interval<T> other) {
        return ComparisonChain.start()
                .compare(this.lowerBoundInclusive, other.lowerBoundInclusive)
                .compare(this.upperBoundExclusive, other.upperBoundExclusive)
                .result();
    }

    @Override
    public String toString() {
        return String.format("[%s..%s]", String.valueOf(lowerBoundInclusive), String.valueOf(upperBoundExclusive));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interval<?> interval = (Interval<?>) o;
        return Objects.equals(lowerBoundInclusive, interval.lowerBoundInclusive) &&
                Objects.equals(upperBoundExclusive, interval.upperBoundExclusive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerBoundInclusive, upperBoundExclusive);
    }
}
