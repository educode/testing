package de.hhu.educode.testing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.Permission;

import static org.assertj.core.api.Assertions.assertThat;

public class Program {

    private static final SecurityManager SECURITY_MANAGER = new TestSecurityManager();

    private static final int CODE_OK = 0;
    private static final int CODE_ERR = 1;

    /**
     * Executes the provided Runnable with the given (runtime) input and returns its result.
     *
     * @param runnable The Runnable.
     * @param input The Runnable's (runtime) input.
     * @return The Runnable's result (stdout and exit code).
     */
    public static ExecutionResult execute(Runnable runnable, String input) {
        var oldIn = System.in;
        var oldOut = System.out;
        var testOutput = new ByteArrayOutputStream();

        try (var testInput = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
             var printStream = new PrintStream(testOutput, false, StandardCharsets.UTF_8)) {
            System.setIn(testInput);
            System.setOut(printStream);
            System.setSecurityManager(SECURITY_MANAGER);
            runnable.run();
            return new ExecutionResult(testOutput.toString(), CODE_OK);
        } catch (IOException e) {
            return new ExecutionResult(testOutput.toString(), CODE_ERR);
        } catch (ExitException e) {
            return new ExecutionResult(testOutput.toString(), e.getExitCode());
        } finally {
            System.setSecurityManager(null);
            System.setOut(oldOut);
            System.setIn(oldIn);
        }
    }

    public static ExecutionResult execute(Runnable runnable) {
        return execute(runnable, "");
    }

    private static class ExecutionResult {
        private final String output;
        private final int exitCode;

        private static final String FAIL_MESSAGE = "Expected exit code to be <%d> but was <%d>";

        ExecutionResult(String output, int exitCode) {
            this.output = output;
            this.exitCode = exitCode;
        }

        void assertExitCode(int expected) {
            assertThat(exitCode).withFailMessage(FAIL_MESSAGE, expected, exitCode).isEqualTo(expected);
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
