package org.composed;

public record ComposedExtension<T extends Extension>(Class<T> type) {
    public T get(Object o) {
        Composition comp = Composition.of(o);
        return comp.get(this.type);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Extension> ComposedExtension<T> of(T extension) {
        Class<T> type = (Class<T>) extension.getClass();
        return new ComposedExtension<>(type);
    }
}
