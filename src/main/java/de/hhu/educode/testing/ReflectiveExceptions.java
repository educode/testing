package de.hhu.educode.testing;

public class ReflectiveExceptions {

    /**
     * Handles the provided Throwable. This function does never return normally.
     *
     * @param throwable The Throwable.
     * @param <T> A generic type.
     * @return Nothing.
     */
    public static <T> T handle(Throwable throwable) {
        // Handle exceptions thrown by reflective operations
        if (throwable instanceof ReflectiveOperationException) {
            var cause = throwable.getCause();

            // Check wether the cause is an Error
            if (cause instanceof Error) {
                throw (Error) cause;
            }

            // Check wether the cause is a RuntimeException
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }

            // Rethrow checked exceptions using RuntimeException
            throw new RuntimeException(throwable);
        } else if (throwable instanceof Error) {
            // Rethrow Errors
            throw (Error) throwable;
        } else if (throwable instanceof RuntimeException) {
            // Rethrow RuntimeExceptions
            throw (RuntimeException) throwable;
        } else {
            // Rethrow checked exceptions using RuntimeException
            throw new RuntimeException(throwable);
        }
    }
}
