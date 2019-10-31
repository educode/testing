package de.hhu.educode.testing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestUtilTest {

    @Test
    public void testInvoke() {
        var className = ClassName.of("java.lang.String");
        var instance = TestUtil.newInstance(className, "Hello World");
        var length = TestUtil.invoke(instance, int.class, "length");
        assertThat(length).isEqualTo(11);
    }
}