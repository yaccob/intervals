package net.yaccob.intervals.api;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class IntervalMergingTest {

    @Parameter
    public Scenario scenario;

    @Parameters
    public static Iterable<Scenario> getScenarios() {
        return Arrays.asList(
                new Scenario(new Interval<>(0, 0), new Interval<>(0, 0), new Interval<>(0, 0)),
                new Scenario(new Interval<>(0, 1), new Interval<>(0, 1), new Interval<>(0, 1)),
                new Scenario(new Interval<>(0, 1), new Interval<>(0, 2), new Interval<>(0, 2)),
                new Scenario(new Interval<>(0, 2), new Interval<>(0, 1), new Interval<>(0, 2)),
                new Scenario(new Interval<>(0, 2), new Interval<>(1, 1), new Interval<>(0, 2)),
                new Scenario(new Interval<>(0, 2), new Interval<>(1, 3), new Interval<>(0, 3)),
                new Scenario(new Interval<>(-2, 0), new Interval<>(0, 2), new Interval<>(-2, 2)));
    }

    @Test
    public void testScenarios() {
        Assertions.assertThat(scenario.interval1.mergeWith(scenario.interval2)).isEqualTo(scenario.expectedResult);
    }

    private static class Scenario {
        private final Interval<Integer> interval1;
        private final Interval<Integer> interval2;
        private final Interval<Integer> expectedResult;

        Scenario(Interval<Integer> interval1, Interval<Integer> interval2, Interval<Integer> expectedResult) {
            this.interval1 = interval1;
            this.interval2 = interval2;
            this.expectedResult = expectedResult;
        }

    }
}
