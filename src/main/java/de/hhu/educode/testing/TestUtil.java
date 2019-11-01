package de.hhu.educode.testing;

import org.assertj.core.api.Assertions;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public final class TestUtil {

    private TestUtil() {
    }

    public static Object newInstance(final ClassName className, ConstructorCall constructorCall) {
        try {
            var clazz = Class.forName(className.toString());
            var constructor = clazz.getConstructor(constructorCall.getParameterTypes());
            return constructor.newInstance(constructorCall.getArgs());
        } catch (ClassNotFoundException e) {
            return fail("Expected class %s to exist but it didn't", className);
        } catch (NoSuchMethodException e) {
            return fail("Expected class %s to declare constructor %s but none was found",
                    className, className + Arrays.stream(constructorCall.getParameterTypes())
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(",", "(", ")")));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invoke(Object target, MethodCall methodCall) {
        return invoke(target, target.getClass(), methodCall);
    }

    public static Object invoke(Class<?> targetClass, MethodCall methodCall) {
        return invoke(null, targetClass, methodCall);
    }

    public static Object invoke(Object target, Class<?> targetClass, MethodCall methodCall) {
        try {
            var method = targetClass.getMethod(methodCall.getName(), methodCall.getParameterTypes());
            if (!method.getReturnType().equals(methodCall.getReturnType())) {
                return fail("Expected method %s to return value of type <%s> but was <%s>",
                        methodCall.getName() + Arrays.stream(methodCall.getParameterTypes())
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(",", "(", ")")),
                        methodCall.getReturnType().getSimpleName(), method.getReturnType().getSimpleName());
            }

            return method.invoke(target, methodCall.getArgs());
        } catch (NoSuchMethodException e) {
            return fail("Expected class %s to declare method %s but it didn't",
                    targetClass.getSimpleName(), methodCall.getName() + Arrays.stream(methodCall.getParameterTypes())
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(",", "(", ")")));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
