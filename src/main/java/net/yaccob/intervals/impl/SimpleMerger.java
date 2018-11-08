package net.yaccob.intervals.impl;

import net.yaccob.intervals.api.Interval;

import java.util.List;

public class SimpleMerger<T extends Comparable<T>> implements net.yaccob.intervals.api.IntervalMerger<T> {
    @Override
    public List<Interval<T>> merge(List<Interval<T>> intervals) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public List<T[]> mergePrimitive(List<T[]> intervals) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
