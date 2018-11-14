package net.yaccob.intervals.api;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class AscendingIntStrategyApplicatorTest {

    @Test
    public void simpleStrategy() {
        List<Integer> input = IntStream.of(7, 1, 3).boxed().collect(Collectors.toList());
        List<Integer> expected = IntStream.of(1, 3, 7, 1001, 1003, 1007).boxed().collect(Collectors.toList());
        List<Integer> result = new ArrayList<>();
        AscendingIntStrategyApplicator.apply(input,
                new IntStrategy<>(Function.identity(), result::add),
                new IntStrategy<>(e -> e + 1000, result::add)
        );
        Assertions.assertThat(result).isEqualTo(expected);
    }
}
