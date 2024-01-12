package jp.takejohn.bask.elements;

import jp.takejohn.bask.elements.expressions.ExprNone;
import org.jetbrains.annotations.NotNull;

public final class ClassRegistry {

    private ClassRegistry() {
        throw new AssertionError();
    }

    public static void load() {
        loadClass(ResourceTypes.class);

        loadClass(ExprNone.class);
    }

    public static void loadClass(@NotNull Class<?> clazz) {
        try {
            Class.forName(clazz.getName());
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }

}
