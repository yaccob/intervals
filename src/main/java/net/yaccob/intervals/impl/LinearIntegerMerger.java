package net.yaccob.intervals.impl;

import net.yaccob.intervals.api.Interval;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Merger that allows merging intervals with O(n) time complexity.
 * For now this merger only supports integer intervals.
 * Maybe it could relatively easily be implemented in a way that it supports intervals for discrete values in general.
 */
public class LinearIntegerMerger implements net.yaccob.intervals.api.IntervalMerger<Integer> {

    private final int overallOffset;

    /**
     * Constructor that supports only positive interval-boundaries.
     */
    public LinearIntegerMerger() {
        this(0);
    }

    /**
     * Create LinearIntegerMerger that supports negative interval bounds as well.
     * Since BitSet only supports positive integers as index (the same applies to any array),
     * the overallOffset needs to be at least as big as the highest absolute value of all negative boundaries.
     * <p>
     * TODO: consider if there is a way to dynamically update the overallOffset while processing input.
     * If yes, this constructor-overload would become redundant
     *
     * @param overallOffset Positive overallOffset for negative interval-boundaries, negative overallOffset for saving
     *                      space and time, if the lowest possible value is quite high (avoiding unused bits between
     *                      0 and the lowest value covered by any interval).
     */
    public LinearIntegerMerger(int overallOffset) {
        this.overallOffset = overallOffset;
    }

    @Override
    public List<Interval<Integer>> merge(List<Interval<Integer>> intervals) {

        BitSet bitSet = new BitSet();
        Map<Integer, Integer> lowerBoundsMap = new HashMap<>();
        Map<Integer, Integer> upperBoundsMap = new HashMap<>();
        List<Interval<Integer>> result = new ArrayList<>();

        intervals.forEach(interval -> {
            registerBound(bitSet, lowerBoundsMap, interval::getLowerBoundInclusive, 0);
            registerBound(bitSet, upperBoundsMap, interval::getUpperBoundExclusive, 1);
        });

        Deque<Integer> lowerBoundsStack = new LinkedList<>();
        for (int bitPosition = bitSet.nextSetBit(0); bitPosition >= 0; bitPosition = bitSet.nextSetBit(bitPosition + 1)) {
            if (bitPosition % 2 == 0) { // lower bound bit
                int position = bitPosition / 2 - overallOffset;
                Integer count = lowerBoundsMap.get(position);
                // this inner loop is executed once per interval-definition, so it doesn't make us deviate from O(n)
                IntStream.range(0, count)
                        .mapToObj(i -> position)
                        .forEach(lowerBoundsStack::push);
            } else { // upper bound bit
                int position = bitPosition / 2 - overallOffset + 1;
                Integer count = upperBoundsMap.get(position - 1);
                // this inner loop is executed once per interval-definition, so it doesn't make us deviate from O(n)
                IntStream.range(0, count)
                        .map(i -> lowerBoundsStack.pop())
                        .filter(first -> lowerBoundsStack.isEmpty())
                        .mapToObj(first -> new Interval<>(first, position - 1))
                        .forEach(result::add);
            }
        }

        return result;
    }

    private void registerBound(BitSet bitSet, Map<Integer, Integer> boundsMap, Supplier<Integer> bound, int boundOffset) {
        bitSet.set(((bound.get() + overallOffset) * 2 + boundOffset));
        boundsMap.put(bound.get(), boundsMap.getOrDefault(bound.get(), 0) + 1);
    }

    @Override
    public List<Integer[]> mergePrimitive(List<Integer[]> intervals) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
