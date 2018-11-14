package net.yaccob.intervals.impl;

import net.yaccob.intervals.api.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class SimpleMerger<T extends Comparable<T>> implements net.yaccob.intervals.api.IntervalMerger<T> {

    @Override
    public List<Interval<T>> merge(List<Interval<T>> intervals) {

        // Since the input parameter (owned by our client) shouldn't be amended, in-place sorting wouldn't make sense.
        // Instead, we create a sorted representation of the original list (using a set to eliminate duplicates)
        // rather than just copying the original for the purpose of not amending it:
        SortedSet<Interval<T>> sorted = new TreeSet<>(intervals);

        List<Interval<T>> result = new ArrayList<>();
        if (sorted.size() > 0) {
            result.add(sorted.first());
        }

        int resultIndex = 0;
        for (Interval<T> interval : sorted) {
            if (result.get(resultIndex).overlapsWith(interval)) {
                result.set(resultIndex, result.get(resultIndex).mergeWith(interval));
            } else {
                result.add(interval);
                ++resultIndex;
            }
        }
        return result;
    }

}
