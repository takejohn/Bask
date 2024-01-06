package jp.takejohn.bask.processor.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ClassSource {

    private final @NotNull String name;

    private final @Nullable String packageName;

    private final List<@NotNull TypeUse> genericParameters = new ArrayList<>();

    private @Nullable TypeUse superclass;

    private final List<@NotNull TypeUse> superinterfaces = new ArrayList<>();

    public ClassSource(@NotNull String name, @Nullable String packageName) {
        this.name = name;
        this.packageName = packageName;
    }

    public ClassSource(@NotNull String name) {
        this(name, null);
    }

    public @NotNull ClassSource addGenericParameters(@NotNull TypeUse @NotNull... values) {
        genericParameters.addAll(List.of(values));
        return this;
    }

    public @NotNull ClassSource superclass(TypeUse superclass) {
        this.superclass = superclass;
        return this;
    }

    public @NotNull ClassSource addSuperInterfaces(@NotNull TypeUse @NotNull... values) {
        superinterfaces.addAll(List.of(values));
        return this;
    }

    @Override
    public @NotNull String toString() {
        final StringBuilder builder = new StringBuilder();
        if (packageName != null) {
            builder.append("package ").append(packageName).append(";");
        }
        builder.append("class ").append(name);
        if (!genericParameters.isEmpty()) {
            builder.append("<");
            StringBuilders.appendJoined(builder, ",", genericParameters);
            builder.append(">");
        }
        if (superclass != null) {
            builder.append(" extends ").append(superclass);
        }
        if (!superinterfaces.isEmpty()) {
            builder.append(" implements ");
            superinterfaces.forEach(builder::append);
        }
        builder.append("{");
        builder.append("}");
        return builder.toString();
    }

}
