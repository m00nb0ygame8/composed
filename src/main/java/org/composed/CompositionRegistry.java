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
        this.compositions.add(comp);
    }

    Composition get(Object o) {
        return this.compositions.stream()
                .filter(c -> c.wrapped() == o)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot get composition for object\"%s.\" No registered composition!"
                        .formatted(o)));
    }

    static CompositionRegistry inst() {
        if(inst == null) inst = new CompositionRegistry();
        return inst;
    }
}
