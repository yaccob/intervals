package net.yaccob.intervals.impl;

import net.yaccob.intervals.api.AscendingIntStrategyApplicator;
import net.yaccob.intervals.api.IntStrategy;
import net.yaccob.intervals.api.Interval;
import net.yaccob.intervals.api.IntervalMerger;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class ComplexLinearMerger implements IntervalMerger<Integer> {

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
