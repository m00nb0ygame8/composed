package org.composed;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Composition {
    private final List<ExtensionEntry<?>> extensionEntries;
    private final WeakReference<Object> object;

    private Composition(Object o) {
        this.object = new WeakReference<>(o);
        this.extensionEntries = new ArrayList<>();
        CompositionRegistry.inst().register(this);
    }

    public <T extends Extension> void extend(Class<T> type, T inst) {
        if(extensionEntries.stream().anyMatch(e -> e.type().equals(type))) throw new IllegalStateException("Extension already added for type \"%s!\"".formatted(type.getSimpleName()));
        ExtensionEntry<T> entry = new ExtensionEntry<>(
                type, inst
        );
        this.extensionEntries.add(entry);
    }

    public <T extends Extension> List<T> getExtensionsOf(Class<T> type) {
        return this.extensionEntries.stream()
                .filter(entry -> type.isAssignableFrom(entry.type()))
                .map(entry -> type.cast(entry.extension))
                .toList();
    }

    @SuppressWarnings("unchecked")
    public <T extends Extension> T get(Class<T> type) {
       return  this.extensionEntries.stream()
               .filter(e -> e.type().equals(type))
               .map(e -> (ExtensionEntry<T>) e)
               .findFirst()
               .orElseThrow(() ->
                       new IllegalArgumentException(
                               "Cannot get extension of type \"%s.\" Not added to composition!"
                                       .formatted(type.getName())
                       )
               )
               .extension();
    }
    public <T extends Extension> boolean has(Class<T> type) {
        return  this.extensionEntries.stream().map(ExtensionEntry::type)
                .anyMatch(t -> t.equals(type));
    }

    public Object wrapped() {
        return object.get();
    }

    public static Composition of(Object o) {
        if(!CompositionRegistry.inst().has(o)) return new Composition(o);
        else return CompositionRegistry.inst().get(o);
    }

    public record ExtensionEntry<T extends Extension>(Class<T> type, T extension) {}
}
