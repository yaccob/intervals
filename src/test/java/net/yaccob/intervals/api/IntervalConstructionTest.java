package net.yaccob.intervals.api;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class IntervalConstructionTest {

    @Parameter
    public Scenario scenario;

    @Parameters
    public static Iterable<Scenario> getScenarios() {
        return Arrays.asList(new Scenario(new Interval<>(0, 0), 0, 0),
                new Scenario(new Interval<>(0, 1), 0, 1),
                new Scenario(new Interval<>(1, 0), 0, 1),
                new Scenario(new Interval<>(-1, 0), -1, 0),
                new Scenario(new Interval<>(1, -1), -1, 1));
    }

    @Test
    public void testFirst() {
        Assertions.assertThat(scenario.interval.getLowerBoundInclusive()).isEqualTo(scenario.getExpectedFirst());
    }

    @Test
    public void testLast() {
        Assertions.assertThat(scenario.interval.getUpperBoundExclusive()).isEqualTo(scenario.getExpectedLast());
    }

    private static class Scenario {
        private final Interval<Integer> interval;
        private final int expectedFirst;
        private final int expectedLast;

        Scenario(Interval<Integer> interval, int expectedFirst, int expectedLast) {
            this.interval = interval;
            this.expectedFirst = expectedFirst;
            this.expectedLast = expectedLast;
        }

        int getExpectedFirst() {
            return expectedFirst;
        }

        int getExpectedLast() {
            return expectedLast;
        }
    }
}
