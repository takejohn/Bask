package jp.takejohn.bask;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import jp.takejohn.bask.annotations.SkriptDoc;
import jp.takejohn.bask.annotations.SkriptType;
import jp.takejohn.bask.annotations.SkriptTypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class BaskAPI {

    private BaskAPI() {
        throw new AssertionError();
    }

    /**
     * Register a class as a type in Skript if it has a {@link SkriptType} annotation.
     * The class may have a {@link SkriptDoc} annotation for Skript's documentation.
     * @param clazz a class to register
     */
    public static <T> void register(@NotNull Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz cannot be null");
        final @Nullable SkriptType skriptType = clazz.getAnnotation(SkriptType.class);
        if (skriptType != null) {
            final @NotNull ClassInfo<T> classInfo = new ClassInfo<>(clazz, skriptType.value());
            classInfo.user(skriptType.user());
            setDoc(classInfo, clazz.getAnnotation(SkriptDoc.class));
            setDoc(classInfo, clazz.getAnnotation(SkriptTypeUsage.class));
            classInfo.parser(new Parser<>() {

                @Override
                public boolean canParse(@NotNull ParseContext context) {
                    return false;
                }

                @Override
                public @NotNull String toString(T o, int flags) {
                    return toVariableNameString(o);
                }

                @Override
                public @NotNull String toVariableNameString(T o) {
                    return o.toString();
                }

            });
            Classes.registerClass(classInfo);
        }
    }

    private static void setDoc(@NotNull ClassInfo<?> classInfo, @Nullable SkriptDoc skriptDoc) {
        if (skriptDoc == null) {
            return;
        }
        if (!skriptDoc.name().isEmpty()) {
            classInfo.name(skriptDoc.name());
        }
        if (skriptDoc.description().length != 0) {
            classInfo.description(skriptDoc.description());
        }
        if (skriptDoc.examples().length != 0) {
            classInfo.examples(skriptDoc.examples());
        }
        if (!skriptDoc.since().isEmpty()) {
            classInfo.since(skriptDoc.since());
        }
        if (skriptDoc.requiredPlugins().length != 0) {
            classInfo.requiredPlugins(skriptDoc.requiredPlugins());
        }
        if (!skriptDoc.documentationId().isEmpty()) {
            classInfo.documentationId(skriptDoc.documentationId());
        }
    }

    private static void setDoc(@NotNull ClassInfo<?> classInfo, @Nullable SkriptTypeUsage skriptTypeUsage) {
        if (skriptTypeUsage != null) {
            classInfo.usage(skriptTypeUsage.value());
        }
    }

}
