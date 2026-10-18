package net.cyber_jellyfish.cc_rj45.util;

import java.util.function.Supplier;

public interface RegistryEntry<T> extends Supplier<T> {
    T get();

    static <T> RegistryEntry<T> of(T value) {
        return () -> value;
    }

    static <T> RegistryEntry<T> of(Supplier<T> supplier) {
        return supplier::get;
    }
}
