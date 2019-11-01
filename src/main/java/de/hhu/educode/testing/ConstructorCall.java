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
public class ConstructorCall {

    private static final int NO_MODIFIERS = 0x0;
    private static final Class<?>[] NO_PARAMETER_TYPES = {};

    private final RuntimeClass runtimeClass;

    @Builder.Default
    private final int modifiers = NO_MODIFIERS;

    @Builder.Default
    private final Class<?>[] parameterTypes = NO_PARAMETER_TYPES;

    public Object invoke(Object... args) {
        var clazz = runtimeClass.getActual();

        try {
            var constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            if (constructor.getModifiers() != modifiers) {
                return fail("Expected constructor %s to have modifiers <%s> but had <%s>",
                        toString(), Modifier.toString(modifiers), Modifier.toString(constructor.getModifiers()));
            }

            //noinspection JavaReflectionInvocation
            return constructor.newInstance(args);
        } catch (NoSuchMethodException e) {
            return fail("Expected class %s to declare constructor %s but none was found",
                    clazz.getSimpleName(), toString());
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return runtimeClass.getActual().getSimpleName() + Arrays.stream(parameterTypes)
                .map(Class::getSimpleName)
                .collect(Collectors.joining(",", "(", ")"));
    }
}
