package org.composed;

import java.util.ArrayList;
import java.util.List;

class CompositionRegistry {
    private static CompositionRegistry inst;

    private final List<Composition> compositions;

    private CompositionRegistry() {
        this.compositions = new ArrayList<>();
    }

    void register(Composition comp) {
        check();
        this.compositions.add(comp);
    }

    Composition get(Object o) {
        check();
        return this.compositions.stream()
                .filter(c -> c.wrapped() == o)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot get composition for object\"%s.\" No registered composition!"
                        .formatted(o)));
    }

    boolean has(Object o) {
        return this.compositions.stream()
                .anyMatch(c -> c.wrapped() == o);
    }

    private void check() {
        this.compositions.removeIf(c -> c.wrapped() == null);
    }

    static CompositionRegistry inst() {
        if(inst == null) inst = new CompositionRegistry();
        return inst;
    }
}
