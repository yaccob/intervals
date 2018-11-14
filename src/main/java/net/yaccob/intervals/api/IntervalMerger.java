package net.yaccob.intervals.api;

import java.util.List;

public interface IntervalMerger<T extends Comparable<T>> {
    List<Interval<T>> merge(List<Interval<T>> intervals);
}
