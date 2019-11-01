package de.hhu.educode.testing;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.*;

class CallTest {

    private static final RuntimeClass STRING_CLASS = RuntimeClass.forName("java.lang.String");

    private static final ConstructorCall STRING_CONSTRUCTOR = ConstructorCall.builder()
            .runtimeClass(STRING_CLASS)
            .modifiers(Modifier.PUBLIC)
            .parameterTypes(new Class<?>[]{ String.class })
            .build();

    private static final MethodCall<String> SUBSTRING_METHOD = MethodCall.<String>builder()
            .runtimeClass(STRING_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(String.class)
            .name("substring")
            .parameterTypes(new Class<?>[]{ int.class, int.class })
            .build();

    private static final MethodCall<String> NON_EXISTENT_METHOD = MethodCall.<String>builder()
            .runtimeClass(STRING_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(String.class)
            .name("foo")
            .parameterTypes(new Class<?>[]{ int.class, int.class })
            .build();

    @Test
    void testInvoke() {
        var instance = STRING_CONSTRUCTOR.invoke("Hello World");
        var substring = SUBSTRING_METHOD.invoke(instance, 0, 5);

        assertThat(substring).isEqualTo("Hello");
    }

    @Test
    void testAssertionFails() {
        var instance = STRING_CONSTRUCTOR.invoke("Hello World");
        assertThatThrownBy(() -> NON_EXISTENT_METHOD.invoke(instance, "bar")).isInstanceOf(AssertionError.class);
    }
}