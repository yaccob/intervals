package net.yaccob.intervals;

import java.util.List;

public interface IntervalMerger<T extends Comparable<T>> {
    List<Interval<T>> merge(List<Interval<T>> intervals);
    List<T[]> mergePrimitive(List<T[]> intervals);
}
