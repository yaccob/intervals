package net.yaccob.intervals.impl;

import net.yaccob.intervals.api.Interval;
import net.yaccob.intervals.api.IntervalMerger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class IntervalMergerPerformanceComparison {

    private final static int RANGE = 100000;
    @Parameter
    public Scenario scenario;

    @Parameters(name = "{index}: {0}")
    public static Iterable<Scenario> scenarios() {
        return Arrays.asList(
                new Scenario("bigIntervalsBigGaps", RANGE / 10, RANGE / 10),
                new Scenario("bigIntervalsSmallGaps", RANGE / 10 - 10, 10),
                new Scenario("smallIntervalsBigGaps", 10, RANGE / 10 - 10),
                new Scenario("smallIntervalsSmallGaps", 60, 40),
                new Scenario("smallerIntervalsSmallerGaps", 6, 4),
                new Scenario("smallestIntervalsSmallestGaps", 2, 1)
        );
    }

    @Test
    public void simpleMerger() {
        repeatTestFor("simpleMerger", new SimpleMerger<>());
    }

    @Test
    public void linearInteger() {
        repeatTestFor("linearInteger", new LinearIntegerMerger());
    }

    @Test
    public void complexLinearInteger() {
        repeatTestFor("complexLinearInteger", new ComplexLinearMerger());
    }

    private void repeatTestFor(String name, IntervalMerger<Integer> merger) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; ++i) {
            merger.merge(scenario.list);
        }
        final long endTime = System.currentTimeMillis() - startTime;
        System.out.println(String.format("%-24s (%4dms) %s", name, endTime, scenario));
    }

    private static class Scenario {

        private final String name;
        private final List<Interval<Integer>> list;
        private final int intervalSize;
        private final int gapSize;

        private Scenario(String name, int intervalSize, int gapSize) {
            this.name = name;
            this.list = new ArrayList<>();
            for (int i = 0; i <= RANGE - intervalSize - gapSize + 1; i += intervalSize + gapSize) {
                list.add(new Interval<>(i, i + intervalSize));
            }
            this.intervalSize = intervalSize;
            this.gapSize = gapSize;
        }

        @Override
        public String toString() {
            final double coverage = (double) intervalSize / (intervalSize + gapSize);
            final double ratio = (double) list.size() / RANGE / coverage;
            return String.format("%-30s valueCoverage: %5.2f%%, intervals/valueCoverage: %6.3f%% (intervalSize: %8d, gapSize: %8d)",
                    name, coverage * 100, ratio * 100, intervalSize, gapSize);
        }
    }

}
