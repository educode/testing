package de.hhu.educode.testing;

import static org.assertj.core.api.Assertions.fail;

@SuppressWarnings({"WeakerAccess", "unused"})
public class RuntimeClass {

    private final String name;

    private RuntimeClass(String name) {
        this.name = name;
    }

    public static RuntimeClass forName(String className) {
        return new RuntimeClass(className);
    }

    public Class<?> getActual() {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return fail("Expected class %s to exist but it didn't", name);
        }
    }
}
