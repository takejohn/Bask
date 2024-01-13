package jp.takejohn.bask.elements;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import jp.takejohn.bask.ExceptionStoringResource;

public class ResourceTypes {

    private ResourceTypes() {
        throw new AssertionError();
    }

    static {
        Classes.registerClass(new ClassInfo<>(ExceptionStoringResource.class, "resource")
                .name("Resource")
                .description("Represents a resource available in 'with' section")
                .since("0.1.0")
                .user("resources?"));

    }

}
