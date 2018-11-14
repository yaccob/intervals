package net.yaccob.intervals.impl;

import net.yaccob.intervals.api.IntervalMerger;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * While we have different implementations of IntervalMerger,
 * we want to use the same scenarios for all implementations. That's why we extract the test data
 * to a base class intended to be used by all IntervalMerger implementation tests.
 */
@RunWith(Parameterized.class)
public class SimpleMergerTest extends MergerTestBase {

    private IntervalMerger<Integer> merger;

    @Before
    public void init() {
        merger = new SimpleMerger<>();
    }

    @Test
    public void testScenarios() {
        Assertions.assertThat(merger.merge(scenario.getInput())).isEqualTo(scenario.getOutput());
    }
}
