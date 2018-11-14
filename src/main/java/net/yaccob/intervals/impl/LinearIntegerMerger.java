package net.yaccob.intervals.impl;

import net.yaccob.intervals.api.AscendingIntStrategyApplicator;
import net.yaccob.intervals.api.IntStrategy;
import net.yaccob.intervals.api.Interval;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Merger that allows merging intervals with O(n) time complexity.
 * For now this merger only supports integer intervals.
 * Maybe it could relatively easily be implemented in a way that it supports intervals for discrete values in general.
 */
public class LinearIntegerMerger implements net.yaccob.intervals.api.IntervalMerger<Integer> {

    @Override
    public List<Interval<Integer>> merge(List<Interval<Integer>> intervals) {

        Deque<Integer> lowerBoundsStack = new LinkedList<>();
        List<Interval<Integer>> result = new ArrayList<>();

        AscendingIntStrategyApplicator.apply(intervals,
                new IntStrategy<Interval<Integer>>(Interval::getLowerBoundInclusive, lowerBoundsStack::push),
                new IntStrategy<Interval<Integer>>(Interval::getUpperBoundExclusive, pos -> {
                    int lower = lowerBoundsStack.pop();
                    if (lowerBoundsStack.isEmpty()) {
                        result.add(new Interval<>(lower, pos));
                    }
                })
        );

        return result;
    }

}
