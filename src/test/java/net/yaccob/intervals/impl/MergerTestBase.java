package net.yaccob.intervals.impl;

import net.yaccob.intervals.api.Interval;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MergerTestBase {

    @Parameter
    public Scenario scenario;

    @Parameters
    public static Iterable<Scenario> getScenarios() {
        return Arrays.asList(
                new Scenario( // from the spec
                        Arrays.asList(new Integer[][]{{25, 30}, {2, 19}, {14, 23}, {4, 8}}),
                        Arrays.asList(new Integer[][]{{2, 23}, {25, 30}})
                ),
                new Scenario( // empty input -> empty output
                        Arrays.asList(new Integer[][]{}),
                        Arrays.asList(new Integer[][]{})
                ),
                new Scenario( // one input -> same output
                        Arrays.asList(new Integer[][]{{1, 2}}),
                        Arrays.asList(new Integer[][]{{1, 2}})
                ),
                new Scenario( // full overlap
                        Arrays.asList(new Integer[][]{{1, 2}, {1, 2}}),
                        Arrays.asList(new Integer[][]{{1, 2}})
                ),
                new Scenario( // fully contained
                        Arrays.asList(new Integer[][]{{1, 4}, {2, 3}}),
                        Arrays.asList(new Integer[][]{{1, 4}})
                ),
                new Scenario( // partial overlap
                        Arrays.asList(new Integer[][]{{1, 4}, {2, 5}}),
                        Arrays.asList(new Integer[][]{{1, 5}})
                ),
                new Scenario( // partial overlap other direction
                        Arrays.asList(new Integer[][]{{2, 5}, {1, 4}}),
                        Arrays.asList(new Integer[][]{{1, 5}})
                ),
                new Scenario( // complex one, including first/last irregularities (first > last)
                        Arrays.asList(new Integer[][]{{8, 2}, {4, 6}, {7, 3}, {3, 5}, {-2, 1}, {-1, -3}, {0, -4}, {-6, -4}, {-5, -7}, {-8, -7}}),
                        Arrays.asList(new Integer[][]{{-8, 1}, {2, 8}})
                )
        );
    }

    protected static class Scenario {
        private final List<Interval<Integer>> input;
        private final List<Interval<Integer>> output;

        Scenario(List<Integer[]> input, List<Integer[]> output) {
            this.input = input.stream().map(a -> new Interval<>(a[0], a[1])).collect(Collectors.toList());
            this.output = output.stream().map(a -> new Interval<>(a[0], a[1])).collect(Collectors.toList());
        }

        List<Interval<Integer>> getInput() {
            return input;
        }

        List<Interval<Integer>> getOutput() {
            return output;
        }
    }
}