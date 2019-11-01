package de.hhu.educode.testing;

import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.fail;

@Builder
@Getter
@SuppressWarnings({"unused"})
public class MethodCall<T> {

    private static final int NO_MODIFIERS = 0x0;
    private static final Class<?>[] NO_PARAMETER_TYPES = {};
    private static final Object[] NO_ARGUMENTS = {};

    private final RuntimeClass runtimeClass;

    private final Class<T> returnType;

    private final String name;

    @Builder.Default
    private final int modifiers = NO_MODIFIERS;

    @Builder.Default
    private final Class<?>[] parameterTypes = NO_PARAMETER_TYPES;

    public T invoke(Object target, Object... args) {
        var clazz = runtimeClass.getActual();

        try {
            var method = clazz.getMethod(name, parameterTypes);
            if (!method.getReturnType().equals(returnType)) {
                return fail("Expected method %s to return value of type <%s> but was <%s>",
                        toString(), returnType.getSimpleName(), method.getReturnType().getSimpleName());
            }

            if (method.getModifiers() != modifiers) {
                return fail("Expected method %s to have modifiers <%s> but had <%s>",
                        toString(), Modifier.toString(modifiers), Modifier.toString(method.getModifiers()));
            }

            //noinspection JavaReflectionInvocation
            return returnType.cast(method.invoke(target, args));
        } catch (NoSuchMethodException e) {
            return fail("Expected class %s to declare method %s but it didn't",
                    clazz.getSimpleName(), toString());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return name + Arrays.stream(parameterTypes)
                .map(Class::getSimpleName)
                .collect(Collectors.joining(",", "(", ")"));
    }
}
