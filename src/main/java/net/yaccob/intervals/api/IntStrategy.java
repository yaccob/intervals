package net.yaccob.intervals.api;

import java.util.function.Consumer;
import java.util.function.Function;

public final class IntStrategy<T> {
    private final Function<T, Integer> mapper;
    private final Consumer<Integer> processor;

    public IntStrategy(Function<T, Integer> mapper, Consumer<Integer> processor) {
        this.mapper = mapper;
        this.processor = processor;
    }

    public Function<T, Integer> getMapper() {
        return mapper;
    }

    public Consumer<Integer> getProcessor() {
        return processor;
    }
}
