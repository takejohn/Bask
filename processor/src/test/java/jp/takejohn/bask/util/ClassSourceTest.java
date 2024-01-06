package jp.takejohn.bask.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassSourceTest {

    private final ClassSource simple = new ClassSource("Simple");

    private final ClassSource inPackage = new ClassSource("InPackage", "org.example.package");

    private final ClassSource subclass = new ClassSource("Subclass").superclass(new TypeUse("Superclass"));

    private final ClassSource complicated = new ClassSource("Complicated", "org.example.package")
            .addGenericParameters(new TypeUse("S"), new TypeUse("T"))
            .superclass(new TypeUse("Superclass", new TypeUse("S")))
            .addSuperInterfaces(new TypeUse("Superinterface", new TypeUse("T")));

    @Test
    void testToString() {
        assertEquals("class Simple{}", simple.toString());
        assertEquals("package org.example.package;class InPackage{}", inPackage.toString());
        assertEquals("class Subclass extends Superclass{}", subclass.toString());
        assertEquals("package org.example.package;class Complicated<S,T> extends Superclass<S> implements Superinterface<T>{}",
                complicated.toString());
    }

}
