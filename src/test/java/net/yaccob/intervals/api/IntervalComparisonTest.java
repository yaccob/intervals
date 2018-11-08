package net.yaccob.intervals.api;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class IntervalComparisonTest {

    @Parameter
    public Scenario scenario;

    @Parameters
    public static Iterable<Scenario> getScenarios() {
        return Arrays.asList(new Scenario(new Interval<>(0, 0), new Interval<>(0, 0), 0),
                new Scenario(new Interval<>(0, 1), new Interval<>(0, 1), 0),
                new Scenario(new Interval<>(0, 1), new Interval<>(0, 2), -1),
                new Scenario(new Interval<>(0, 2), new Interval<>(0, 1), 1),
                new Scenario(new Interval<>(0, 2), new Interval<>(1, 1), -1),
                new Scenario(new Interval<>(0, 2), new Interval<>(1, 3), -1),
                new Scenario(new Interval<>(-2, 0), new Interval<>(0, 2), -1));
    }

    @Test
    public void testScenarios() {
        Assertions.assertThat(scenario.interval1.compareTo(scenario.interval2)).isEqualTo(scenario.expectedResult);
    }

    private static class Scenario {
        private final Interval<Integer> interval1;
        private final Interval<Integer> interval2;
        private final int expectedResult;

        private Scenario(Interval<Integer> interval1, Interval<Integer> interval2, int expectedResult) {
            this.interval1 = interval1;
            this.interval2 = interval2;
            this.expectedResult = expectedResult;
        }
    }
}
