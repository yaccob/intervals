package net.yaccob.intervals.api;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class IntervalCoverageTest {

    @Parameter
    public Scenario scenario;

    @Parameters
    public static Iterable<Scenario> getScenarios() {
        return Arrays.asList(
                new Scenario(new Interval<>(-1, 0), 0, false),
                new Scenario(new Interval<>(0, 0), 0, false),
                new Scenario(new Interval<>(0, 1), 0, true),
                new Scenario(new Interval<>(0, 1), 1, false)
        );
    }

    @Test
    public void testScenarios() {
        Assertions.assertThat(scenario.interval1.covers(scenario.value)).isEqualTo(scenario.expectedResult);
    }

    private static class Scenario {
        private final Interval<Integer> interval1;
        private final Integer value;
        private final boolean expectedResult;

        Scenario(Interval<Integer> interval1, Integer value, boolean expectedResult) {
            this.interval1 = interval1;
            this.value = value;
            this.expectedResult = expectedResult;
        }

    }
}
