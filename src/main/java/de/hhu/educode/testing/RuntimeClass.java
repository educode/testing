package de.hhu.educode.testing;

import static org.assertj.core.api.Assertions.fail;

@SuppressWarnings({"WeakerAccess", "unused"})
public class RuntimeClass {

    private static ClassLoader classLoader = RuntimeClass.class.getClassLoader();

    private final String name;

    private RuntimeClass(String name) {
        this.name = name;
    }

    public static RuntimeClass forName(String className) {
        return new RuntimeClass(className);
    }

    public static void useClassLoader(ClassLoader classLoader) {
        RuntimeClass.classLoader = classLoader;
    }

    public Class<?> getActual() {
        try {
            return classLoader.loadClass(name);
        } catch (ClassNotFoundException e) {
            return fail("Expected class %s to exist but it didn't", name);
        }
    }
}
