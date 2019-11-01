package de.hhu.educode.testing;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MethodCall {

    private static final int NO_MODIFIERS = 0x0;
    private static final Class<?> NO_RETURN_TYPE = void.class;
    private static final Class<?>[] NO_PARAMETER_TYPES = {};
    private static final Object[] NO_ARGUMENTS = {};

    @Builder.Default
    private final int modifiers = NO_MODIFIERS;

    @Builder.Default
    private final Class<?> returnType = NO_RETURN_TYPE;

    private final String name;

    @Builder.Default
    private final Class<?>[] parameterTypes = NO_PARAMETER_TYPES;

    @Builder.Default
    private final Object[] args = NO_ARGUMENTS;
}
