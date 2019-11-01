package de.hhu.educode.testing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.Permission;
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Program {

    private static final SecurityManager SECURITY_MANAGER = new TestSecurityManager();

    private static final int CODE_OK = 0;
    private static final int CODE_ERR = 1;

    /**
     * Executes the provided Runnable with the given (runtime) input.
     *
     * @param runnable The Runnable.
     * @param input The Runnable's (runtime) input.
     * @return The Runnable's result (stdout and exit code).
     */
    public static ExecutionResult<Void> execute(Runnable runnable, String input) {
        return execute(() -> { runnable.run(); return null; }, input);
    }

    public static ExecutionResult<Void> execute(Runnable runnable) {
        return execute(runnable, "");
    }

    /**
     * Executes the provided Supplier with the given (runtime) input.
     *
     * @param supplier The Supplier.
     * @param input The Supplier's (runtime) input.
     * @return The Supplier's result (stdout, exit code and return value).
     */
    public static <T> ExecutionResult<T> execute(Supplier<T> supplier, String input) {
        var oldIn = System.in;
        var oldOut = System.out;
        var testOutput = new ByteArrayOutputStream();

        try (var testInput = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
             var printStream = new PrintStream(testOutput, false, StandardCharsets.UTF_8)) {
            System.setIn(testInput);
            System.setOut(printStream);
            System.setSecurityManager(SECURITY_MANAGER);
            var returnValue = supplier.get();
            return new ExecutionResult<>(testOutput.toString(), CODE_OK, returnValue);
        } catch (IOException e) {
            return new ExecutionResult<>(testOutput.toString(), CODE_ERR, null);
        } catch (ExitException e) {
            return new ExecutionResult<>(testOutput.toString(), e.getExitCode(), null);
        } finally {
            System.setSecurityManager(null);
            System.setOut(oldOut);
            System.setIn(oldIn);
        }
    }

    public static <T> ExecutionResult<T> execute(Supplier<T> supplier) {
        return execute(supplier, "");
    }


    public static class ExecutionResult<T> {
        private final String output;
        private final int exitCode;
        private final T returnValue;

        private static final String FAIL_MESSAGE = "Expected exit code to be <%d> but was <%d>";

        ExecutionResult(String output, int exitCode) {
            this(output, exitCode, null);
        }

        ExecutionResult(String output, int exitCode, T returnValue) {
            this.output = output;
            this.exitCode = exitCode;
            this.returnValue = returnValue;
        }

        public void assertExitCode(int expected) {
            assertThat(exitCode).withFailMessage(FAIL_MESSAGE, expected, exitCode).isEqualTo(expected);
        }

        public Optional<T> getReturnValue() {
            return Optional.ofNullable(returnValue);
        }

        public String getOutput() {
            return output;
        }
    }

    private static final class ExitException extends SecurityException {

        private final int exitCode;

        ExitException(int exitCode) {
            this.exitCode = exitCode;
        }

        public int getExitCode() {
            return exitCode;
        }
    }

    private static final class TestSecurityManager extends SecurityManager {

        @Override
        public void checkExit(int status) {
            super.checkExit(status);
            throw new ExitException(status);
        }

        @Override
        public void checkPermission(Permission perm) {}

        @Override
        public void checkPermission(Permission perm, Object context) {}
    }
}
