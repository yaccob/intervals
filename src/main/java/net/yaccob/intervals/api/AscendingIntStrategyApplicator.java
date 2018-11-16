package net.yaccob.intervals.api;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AscendingIntStrategyApplicator {
    /**
     * Applies client-logic to elements of an unsorted element list.
     * The logic is applied in ascending order of the integer elements provided by the strategies' mappers.<br/>
     * <p>
     * TODO: consider making the bitSet and the positions map fields to become more memory efficient in case of
     * parallel application for different subsets of the elements list.
     * In that case bitSet and the positions map need to be synchronized
     * (e.g. ConcurrentHashMap for positions and some smart re-implementation of BitSet)
     *
     * @param elements   List of elements to be mapped to integers
     * @param strategies List of strategies to be applied (mapper + processor)
     * @param <T>        Type of elements being processed
     */
    @SafeVarargs
    public static <T> void apply(List<T> elements, IntStrategy<T>... strategies) {
        int processorsCount = strategies.length;
        Map<Integer, Integer> positions = new HashMap<>();
        BitSet bitSet = new BitSet();

        int lowestIndex = Integer.MAX_VALUE;
        for (T element : elements) {
            for (IntStrategy<T> strategy : strategies) {
                lowestIndex = Math.min(lowestIndex, strategy.getMapper().apply(element));
            }
        }
        final int offset = lowestIndex;
        for (T element : elements) {
            for (int processorIndex = 0; processorIndex < processorsCount; ++processorIndex) {
                int bitSetIndex = (strategies[processorIndex].getMapper()
                        .apply(element) - offset) * processorsCount + processorIndex;
                bitSet.set(bitSetIndex);
                positions.put(bitSetIndex, positions.getOrDefault(bitSetIndex, 0) + 1);
            }
        }
        for (int bitSetIndex = bitSet.nextSetBit(0); bitSetIndex >= 0; bitSetIndex = bitSet.nextSetBit(bitSetIndex + 1)) {
            for (int occurrenceCounter = 0; occurrenceCounter < positions.get(bitSetIndex); ++occurrenceCounter) {
                strategies[bitSetIndex % processorsCount].getProcessor().accept(bitSetIndex / processorsCount + offset);
            }
        }
    }

}
