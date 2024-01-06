package jp.takejohn.bask.processor.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeUseTest {

    private final TypeUse simple = new TypeUse("String");

    private final TypeUse simple2 = new TypeUse("Integer");

    private final TypeUse generics = new TypeUse("Map", simple, simple2);

    private final TypeUse nested = new TypeUse("List", generics);

    @Test
    void testToString() {
        assertEquals("String", simple.toString());
        assertEquals("Integer", simple2.toString());
        assertEquals("Map<String,Integer>", generics.toString());
        assertEquals("List<Map<String,Integer>>", nested.toString());
    }

}
