package org.composed;

import java.util.*;

class CompositionRegistry {
    private static CompositionRegistry inst;

    private final Map<Object, Composition> compositions;

    private CompositionRegistry() {
        this.compositions = new WeakHashMap<>();
    }

    void register(Composition comp) {
        this.compositions.put(comp.wrapped(), comp);
    }

    Composition get(Object o) {
        Composition comp = this.compositions.get(o);
        if(comp == null) throw new IllegalArgumentException("Cannot get composition for object\"%s.\" No registered composition!".formatted(o));
        return this.compositions.get(o);
    }

    boolean has(Object o) {
        return this.compositions.containsKey(o);
    }

    public static CompositionRegistry inst() {
        if(inst == null) inst = new CompositionRegistry();
        return inst;
    }
}
