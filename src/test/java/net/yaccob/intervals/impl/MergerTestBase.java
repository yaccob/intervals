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

    @Parameters(name = "{index}: {0}")
    public static Iterable<Scenario> getScenarios() {
        return Arrays.asList(
                new Scenario(
                        "from the spec",
                        Arrays.asList(new Integer[][]{{25, 30}, {2, 19}, {14, 23}, {4, 8}}),
                        Arrays.asList(new Integer[][]{{2, 23}, {25, 30}})
                ),
                new Scenario(
                        "empty input -> empty output",
                        Arrays.asList(new Integer[][]{}),
                        Arrays.asList(new Integer[][]{})
                ),
                new Scenario(
                        "one input -> same output",
                        Arrays.asList(new Integer[][]{{1, 2}}),
                        Arrays.asList(new Integer[][]{{1, 2}})
                ),
                new Scenario(
                        "full overlap",
                        Arrays.asList(new Integer[][]{{1, 2}, {1, 2}}),
                        Arrays.asList(new Integer[][]{{1, 2}})
                ),
                new Scenario(
                        "fully contained",
                        Arrays.asList(new Integer[][]{{1, 4}, {2, 3}}),
                        Arrays.asList(new Integer[][]{{1, 4}})
                ),
                new Scenario(
                        "partial overlap",
                        Arrays.asList(new Integer[][]{{1, 4}, {2, 5}}),
                        Arrays.asList(new Integer[][]{{1, 5}})
                ),
                new Scenario(
                        "partial overlap other direction",
                        Arrays.asList(new Integer[][]{{2, 5}, {1, 4}}),
                        Arrays.asList(new Integer[][]{{1, 5}})
                ),
                new Scenario(
                        "cascading",
                        Arrays.asList(new Integer[][]{{0, 1}, {0, 2}}),
                        Arrays.asList(new Integer[][]{{0, 2}})
                ),
                new Scenario(
                        "filling a gap",
                        Arrays.asList(new Integer[][]{{0, 3}, {4, 7}, {0, 7}}),
                        Arrays.asList(new Integer[][]{{0, 7}})
                ),
                new Scenario(
                        "enclosing two intervals",
                        Arrays.asList(new Integer[][]{{1, 3}, {4, 6}, {0, 7}}),
                        Arrays.asList(new Integer[][]{{0, 7}})
                ),
                new Scenario(
                        "common lower bounds",
                        Arrays.asList(new Integer[][]{{0, 3}, {0, 7}, {0, 11}}),
                        Arrays.asList(new Integer[][]{{0, 11}})
                ),
                new Scenario(
                        "common upper bounds",
                        Arrays.asList(new Integer[][]{{0, 9}, {2, 9}, {5, 9}}),
                        Arrays.asList(new Integer[][]{{0, 9}})
                ),
                new Scenario(
                        "complex one, including first/last irregularities (first > last)",
                        Arrays.asList(new Integer[][]{{8, 2}, {4, 6}, {7, 3}, {3, 5}, {-2, 1}, {-1, -3}, {0, -4}, {-6, -4}, {-5, -7}, {-8, -7}}),
                        Arrays.asList(new Integer[][]{{-8, 1}, {2, 8}})
                )
        );
    }

    protected static class Scenario {
        private final String description;
        private final List<Interval<Integer>> input;
        private final List<Interval<Integer>> output;

        /**
         * This weird constructor supports a much more compact instantiation of intervals (just static initialization
         * of a 2-dimensional array instead of instantiating every single interval with `new Interval`).
         * In my opinion this significantly improves readability by reducing noise.
         * The downside of being less type-safe doesn't matter that much
         * because it serves just for this test class - there are no external clients.
         *
         * @param input  List of pairs of Integers serving as input.
         * @param output List of pairs of Integers indicating the expected output.
         */
        Scenario(String description, List<Integer[]> input, List<Integer[]> output) {
            this.description = description;
            this.input = input.stream().map(a -> new Interval<>(a[0], a[1])).collect(Collectors.toList());
            this.output = output.stream().map(a -> new Interval<>(a[0], a[1])).collect(Collectors.toList());
        }

        List<Interval<Integer>> getInput() {
            return input;
        }

        List<Interval<Integer>> getOutput() {
            return output;
        }

        @Override
        public String toString() {
            return String.format("%s: input=%s, result=%s", description, String.valueOf(input), String.valueOf(output));
        }
    }
}
