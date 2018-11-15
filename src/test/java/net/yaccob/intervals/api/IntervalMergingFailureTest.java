package net.yaccob.intervals.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class IntervalMergingFailureTest {

    @Parameter
    public Scenario scenario;

    @Parameters
    public static Iterable<Scenario> getScenarios() {
        return Arrays.asList(
                new Scenario(new Interval<>(-1, -2), new Interval<>(-3, -5)),
                new Scenario(new Interval<>(1, 2), new Interval<>(3, 5)),
                new Scenario(new Interval<>(3, 5), new Interval<>(1, 2)),
                new Scenario(new Interval<>(-3, -5), new Interval<>(-1, -2))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScenarios() {
        scenario.interval1.mergeWith(scenario.interval2);
    }

    private static class Scenario {
        private final Interval<Integer> interval1;
        private final Interval<Integer> interval2;

        Scenario(Interval<Integer> interval1, Interval<Integer> interval2) {
            this.interval1 = interval1;
            this.interval2 = interval2;
        }

    }
}
