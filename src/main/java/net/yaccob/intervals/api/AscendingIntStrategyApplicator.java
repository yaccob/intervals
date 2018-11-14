package net.yaccob.intervals.api;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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
    @SuppressWarnings("CodeBlock2Expr")
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
        IntStream.range(0, elements.size()).forEach(element -> {
            IntStream.range(0, processorsCount).forEach(i -> {
                int index = (strategies[i].getMapper().apply(elements.get(element)) - offset) * processorsCount + i;
                bitSet.set(index);
                positions.put(index, positions.getOrDefault(index, 0) + 1);
            });
        });
        for (int bitPosition = bitSet.nextSetBit(0); bitPosition >= 0; bitPosition = bitSet.nextSetBit(bitPosition + 1)) {
            for (int i = 0; i < positions.get(bitPosition); ++i) {
                strategies[bitPosition % processorsCount].getProcessor().accept(bitPosition / processorsCount + offset);
            }
        }
    }

}
