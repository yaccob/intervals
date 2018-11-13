package net.yaccob.intervals.impl;

import net.yaccob.intervals.api.Interval;

import java.util.*;
import java.util.function.Consumer;

/**
 * Merger that allows merging intervals with O(n) time complexity.
 * For now this merger only supports integer intervals.
 * Maybe it could relatively easily be implemented in a way that it supports intervals for discrete values in general.
 */
public class LinearIntegerMerger implements net.yaccob.intervals.api.IntervalMerger<Integer> {

    private final Integer overallOffset;

    /**
     * Constructor that supports only positive interval-boundaries.
     * Merging fails if there is any negative bound.
     * Memory will be wasted in case of high positive bounds only.
     * That's why this constructor is private.
     */
    private LinearIntegerMerger() {
        this(null);
    }

    /**
     * Create LinearIntegerMerger that supports negative interval bounds as well.
     * Since BitSet only supports positive integers as index (the same applies to any array),
     * the overallOffset needs to be at least as big as the highest absolute value of all negative boundaries.
     *
     * If the appropriate offset is unknown, use the {@link #fromIntervals(List<Interval<Integer>)} factory method
     * instead of this constructor. If you do know the appropriate offset in advance, calling this constructor directly
     * will save one iteration over the input interval list.
     *
     * @param overallOffset Negative overallOffset for negative interval-boundaries, positive overallOffset for saving
     *                      space and time, especially if the lowest possible value is quite high
     *                      (avoiding unused bits between 0 and the lowest value covered by any interval).
     *                      Usually the value should be equal to the lowest lower bound of all input intervals.
     */
    public LinearIntegerMerger(Integer overallOffset) {
        this.overallOffset = overallOffset;
    }

    /**
     * Factory method for creating a LinearIntegerMerger initialized with the appropriate index offset.
     *
     * @param intervals
     * @return
     */
    public static LinearIntegerMerger fromIntervals(List<Interval<Integer>> intervals) {
        int lowestLowerBound = intervals.stream()
                .map(Interval::getLowerBoundInclusive)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);
        // finally discarded because it would fail for empty interval lists while an empty interval list
        // can still be correctly merged to another empty interval list:
        // .orElseThrow(() -> new IllegalArgumentException(String.format("No min value found for %s", intervals)));
        return new LinearIntegerMerger(lowestLowerBound);
    }

    @Override
    public List<Interval<Integer>> merge(List<Interval<Integer>> intervals) {

        BitSet bitSet = new BitSet();
        Map<Integer, Integer> lowerBoundsMap = new HashMap<>();
        Map<Integer, Integer> upperBoundsMap = new HashMap<>();
        List<Interval<Integer>> result = new ArrayList<>();

        intervals.forEach(interval -> {
            addBound(bitSet, lowerBoundsMap, interval.getLowerBoundInclusive(), 0);
            addBound(bitSet, upperBoundsMap, interval.getUpperBoundExclusive(), 1);
        });

        Deque<Integer> lowerBoundsStack = new LinkedList<>();
        for (int bitPosition = bitSet.nextSetBit(0); bitPosition >= 0; bitPosition = bitSet.nextSetBit(bitPosition + 1)) {
            if (bitPosition % 2 == 0) { // lower bound bit
                processMapItems(bitPosition, lowerBoundsMap, 0, lowerBoundsStack::push);
            } else { // upper bound bit
                processMapItems(bitPosition, upperBoundsMap, 1, pos -> {
                    int first = lowerBoundsStack.pop();
                    if (lowerBoundsStack.isEmpty()) {
                        Interval<Integer> interval = new Interval<>(first, pos - 1);
                        result.add(interval);
                    }
                });
            }
        }

        return result;
    }

    private void processMapItems(int bitPosition, Map<Integer, Integer> boundsMap, int boundOffset, Consumer<Integer> consumer) {
        int position = bitPosition / 2 + overallOffset + boundOffset;
        // this inner loop is executed once per interval-definition, so it doesn't make us deviate from O(n)
        for (int i = 0; i < boundsMap.get(position - boundOffset); i++) {
            consumer.accept(position);
        }
    }

    private void addBound(BitSet bitSet, Map<Integer, Integer> boundsMap, Integer bound, int boundOffset) {
        bitSet.set(((bound - overallOffset) * 2 + boundOffset));
        boundsMap.put(bound, boundsMap.getOrDefault(bound, 0) + 1);
    }

    @Override
    public List<Integer[]> mergePrimitive(List<Integer[]> intervals) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
