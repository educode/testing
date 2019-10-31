package de.hhu.educode.testing;

public class ClassName {

    private final String className;

    private ClassName(String className) {
        this.className = className;
    }

    public static ClassName of(String className) {
        return new ClassName(className);
    }

    @Override
    public String toString() {
        return className;
    }
}
