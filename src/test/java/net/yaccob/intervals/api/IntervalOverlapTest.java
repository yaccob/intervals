package net.yaccob.intervals.api;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class IntervalOverlapTest {

    @Parameter
    public Scenario scenario;

    @Parameters
    public static Iterable<Scenario> getScenarios() {
        return Arrays.asList(
                new Scenario(new Interval<>(0, 0), new Interval<>(0, 0), true),
                new Scenario(new Interval<>(0, 1), new Interval<>(0, 1), true),
                new Scenario(new Interval<>(0, 1), new Interval<>(0, 2), true),
                new Scenario(new Interval<>(0, 2), new Interval<>(0, 1), true),
                new Scenario(new Interval<>(0, 2), new Interval<>(1, 1), true),
                new Scenario(new Interval<>(0, 2), new Interval<>(1, 3), true),
                new Scenario(new Interval<>(1, 2), new Interval<>(3, 4), false),
                new Scenario(new Interval<>(-2, 0), new Interval<>(0, 2), true),
                new Scenario(new Interval<>(-2, 0), new Interval<>(1, 2), false)
        );
    }

    @Test
    public void testScenarios() {
        Assertions.assertThat(scenario.interval1.overlapsWith(scenario.interval2)).isEqualTo(scenario.expectedResult);
    }

    private static class Scenario {
        private final Interval<Integer> interval1;
        private final Interval<Integer> interval2;
        private final boolean expectedResult;

        Scenario(Interval<Integer> interval1, Interval<Integer> interval2, boolean expectedResult) {
            this.interval1 = interval1;
            this.interval2 = interval2;
            this.expectedResult = expectedResult;
        }

    }
}
