package net.yaccob.intervals.impl;

import net.yaccob.intervals.api.Interval;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Merger that allows merging intervals with O(n) time complexity.
 * For now this merger only supports integer intervals.
 * Maybe it could relatively easily be implemented in a way that it supports intervals for discrete values in general.
 */
public class LinearIntegerMerger implements net.yaccob.intervals.api.IntervalMerger<Integer> {

    @Override
    public List<Interval<Integer>> merge(List<Interval<Integer>> intervals) {
        BitSet bitSet = new BitSet();
        int offset = intervals.stream()
                .map(Interval::getLowerBoundInclusive)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);
        intervals.forEach(interval -> bitSet.set(interval.getLowerBoundInclusive() - offset, interval.getUpperBoundExclusive() - offset));
        return buildResult(bitSet, offset);
    }

    private List<Interval<Integer>> buildResult(BitSet bitSet, int offset) {
        List<Interval<Integer>> result = new ArrayList<>();
        final int bitSetLength = bitSet.length();
        int lastLowerBound = 0;
        boolean isInInterval = false;
        for (int i = 0; i < bitSetLength + 1; ++i) {
            final boolean isBitSet = bitSet.get(i);
            if (!isInInterval && isBitSet) {
                isInInterval = true;
                lastLowerBound = i;
            } else if (isInInterval && !isBitSet) {
                isInInterval = false;
                result.add(new Interval<>(lastLowerBound + offset, i + offset));
            } // else no action required
        }
        return result;
    }

}
