package de.hhu.educode.testing;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

class TestUtilTest {

    private static final ConstructorCall STRING_CONSTRUCTOR = ConstructorCall.builder()
            .parameterTypes(new Class<?>[]{ String.class })
            .args(new Object[]{ "Hello World" })
            .build();

    private static final MethodCall SUBSTRING_METHOD = MethodCall.builder()
            .modifiers(Modifier.PUBLIC)
            .returnType(String.class)
            .name("substring")
            .parameterTypes(new Class<?>[]{ int.class, int.class})
            .args(new Object[]{ 0, 5})
            .build();

    @Test
    public void testInvoke() {
        var className = ClassName.of("java.lang.String");
        var instance = TestUtil.newInstance(className, STRING_CONSTRUCTOR);
        var string = TestUtil.invoke(instance, String.class, SUBSTRING_METHOD);
        assertThat(string).isEqualTo("Hello");
    }
}