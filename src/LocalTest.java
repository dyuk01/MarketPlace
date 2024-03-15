
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class LocalTest {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        System.out.printf("Test Count: %d.\n", result.getRunCount());
        if (result.wasSuccessful()) {
            System.out.println("Excellent - all local tests ran successfully.");
        } else {
            System.out.printf("Tests failed: %d.\n", result.getFailureCount());
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.getMessage());
                System.out.println(failure.getTestHeader());
                System.out.println(failure.getDescription());
                System.out.println(failure);
            }
        }
    } //end of main method

    public static class TestCase {
        private final PrintStream originalOutput = System.out;
        private final InputStream originalSysin = System.in;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayInputStream testIn;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayOutputStream testOut;

        public static final String MAKE_ACCOUNT = "Would you like to sign in or create an account?" +
                "\n1. Sign in\n2. Create Account";
        public static final String SIGN_IN_OPTION = "Sign in as:\n1. Seller\n2. Customer";
        public static final String ENTER_EMAIL = "Enter email:";
        public static final String ENTER_PASSWORD = "Enter password";
        public static final String LOG_IN_SUCCESSFUL = "Successfully logged in!";
        public static final String WELCOME = "Welcome dyuk!";

        @Before
        public void outputStart() {
            testOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(testOut));
        }

        @After
        public void restoreInputAndOutput() {
            System.setIn(originalSysin);
            System.setOut(originalOutput);
        }

        private String getOutput() {
            return testOut.toString();
        }

        @SuppressWarnings("SameParameterValue")
        private void receiveInput(String str) {
            testIn = new ByteArrayInputStream(str.getBytes());
            System.setIn(testIn);
        }

        @Test(timeout = 1000)
        public void testExpectedOne() {
            String input = "1" + System.lineSeparator() +
                    "2" + System.lineSeparator() +
                    "dyuk@purdue.edu" + System.lineSeparator() +
                    "123456" + System.lineSeparator();

            String expected = MAKE_ACCOUNT + System.lineSeparator() +
                    SIGN_IN_OPTION + System.lineSeparator() +
                    ENTER_EMAIL + System.lineSeparator() +
                    ENTER_PASSWORD + System.lineSeparator() +
                    LOG_IN_SUCCESSFUL + System.lineSeparator() +
                    WELCOME + System.lineSeparator();

            // Runs the program with the input values
            receiveInput(input);
            Marketplace.main(new String[0]);

            // Retrieves the output from the program
            String output = getOutput();

            // Trims the output and verifies it is correct.
            expected = expected.replaceAll("\r\n", "\n");
            output = output.replaceAll("\r\n", "\n");
            assertEquals("Make sure the program runs successfully!\n",
                    expected.trim(), output.trim());
        }

//        @Test(timeout = 1000)
//        public void testExpectedTwo() {
//            String input = "";
//            String expected = "";
//
//            // Runs the program with the input values
//            receiveInput(input);
//            Marketplace.main(new String[0]);
//
//            // Retrieves the output from the program
//            String output = getOutput();
//
//            // Trims the output and verifies it is correct.
//            expected = expected.replaceAll("\r\n", "\n");
//            output = output.replaceAll("\r\n", "\n");
//            assertEquals("Make sure the program runs successfully!",
//                    expected.trim(), output.trim());
//        }
//
//        @Test(timeout = 1000)
//        public void testExpectedThree() {
//            String input = "";
//            String expected = "";
//
//            // Runs the program with the input values
//            receiveInput(input);
//            Main.main(new String[0]);
//
//            // Retrieves the output from the program
//            String output = getOutput();
//
//            // Trims the output and verifies it is correct.
//            expected = expected.replaceAll("\r\n", "\n");
//            output = output.replaceAll("\r\n", "\n");
//            assertEquals("Make sure the program runs successfully!",
//                    expected.trim(), output.trim());
//        }
    } //end of TestCase
}


