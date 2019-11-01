package de.hhu.educode.testing;

import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.fail;

@Builder
@Getter
@SuppressWarnings({"unused"})
public class ConstructorCall {
    private final RuntimeClass runtimeClass;
    private final Class<?>[] parameterTypes;

    public Object invoke(Object... args) {
        var clazz = runtimeClass.getActual();

        try {
            var constructor = clazz.getConstructor(parameterTypes);
            return constructor.newInstance(args);
        } catch (NoSuchMethodException e) {
            return fail("Expected class %s to declare constructor %s but none was found",
                    clazz.getSimpleName(), clazz.getSimpleName() + Arrays.stream(parameterTypes)
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(",", "(", ")")));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
