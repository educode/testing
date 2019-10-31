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

    public static Object newInstance(final ClassName className, Object... initargs) {
        try {
            var clazz = Class.forName(className.toString());
            var constructor = clazz.getConstructor(toType(initargs));
            return constructor.newInstance(initargs);
        } catch (ClassNotFoundException e) {
            return fail("Expected class %s to exist but it didn't", className);
        } catch (NoSuchMethodException e) {
            return fail("Expected class %s to declare constructor %s but none was found",
                    className, className + Arrays.stream(toType(initargs))
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(",", "(", ")")));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invoke(Object target, Class<?> returnType, String methodName, Object... args) {
        return invoke(target, target.getClass(), returnType, methodName, args);
    }

    public static Object invoke(Class<?> targetClass, Class<?> returnType, String methodName, Object... args) {
        return invoke(null, targetClass, returnType, methodName, args);
    }

    public static Object invoke(Object target, Class<?> targetClass, Class<?> returnType, String methodName, Object... args) {
        try {
            var method = targetClass.getMethod(methodName, toType(args));
            if (!method.getReturnType().equals(returnType)) {
                return fail("Expected method %s to return value of type <%s> but was <%s>",
                        methodName + Arrays.stream(toType(args))
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(",", "(", ")")),
                        returnType.getSimpleName(), method.getReturnType().getSimpleName());
            }

            return method.invoke(target, args);
        } catch (NoSuchMethodException e) {
            return fail("Expected class %s to declare method %s but it didn't",
                    targetClass.getSimpleName(), methodName + Arrays.stream(toType(args))
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(",", "(", ")")));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?>[] toType(Object... objects) {
        return Arrays.stream(objects).map(Object::getClass).toArray(Class<?>[]::new);
    }
}
