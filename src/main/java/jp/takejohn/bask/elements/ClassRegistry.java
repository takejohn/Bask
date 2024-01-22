package jp.takejohn.bask.elements;

import jp.takejohn.bask.BaskAPI;
import jp.takejohn.bask.ExceptionStoringResource;
import jp.takejohn.bask.elements.effects.EffRead;
import jp.takejohn.bask.elements.expressions.ExprBuffer;
import jp.takejohn.bask.elements.expressions.ExprFile;
import jp.takejohn.bask.elements.expressions.ExprNone;
import jp.takejohn.bask.elements.sections.SecOpenFile;
import jp.takejohn.bask.io.Blob;
import jp.takejohn.bask.io.DataSize;
import jp.takejohn.bask.io.OpenedFile;
import jp.takejohn.bask.io.ReadableFile;
import org.jetbrains.annotations.NotNull;

public final class ClassRegistry {

    private ClassRegistry() {
        throw new AssertionError();
    }

    public static void load() {
        BaskAPI.register(ExceptionStoringResource.class);
        BaskAPI.register(Blob.class);
        BaskAPI.register(DataSize.class);
        BaskAPI.register(OpenedFile.class);
        BaskAPI.register(ReadableFile.class);

        loadClass(ExprNone.class);
        loadClass(SecOpenFile.class);
        loadClass(ExprFile.class);
        loadClass(EffRead.class);
        loadClass(ExprBuffer.class);
    }

    public static void loadClass(@NotNull Class<?> clazz) {
        try {
            Class.forName(clazz.getName());
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }

}
