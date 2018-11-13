package net.yaccob.intervals.impl;

import net.yaccob.intervals.api.IntervalMerger;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Probably in future we'll have more optimized implementations of IntervalMerger.
 * We want to use the same scenarios for all implementations. That's why we extract the test data
 * to a base class intended to be used by all IntervalMerger implementation tests.
 */
@RunWith(Parameterized.class)
public class LinearIntegerMergerTest extends MergerTestBase {

    private IntervalMerger<Integer> merger;

    @Before
    public void init() {
        merger = LinearIntegerMerger.fromIntervals(scenario.getInput());
    }

    @Test
    public void testScenarios() {
        Assertions.assertThat(merger.merge(scenario.getInput())).isEqualTo(scenario.getOutput());
    }
}
