package se433_testing_project;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Smoke test for the bootstrap entry point. App.main() blocks on real
 * System.in, so it is exercised by temporarily redirecting the standard
 * streams and feeding a scripted session that completes a checkout. The
 * detailed behavior is unit-tested in MenuControllerTest, ConsoleInputTest,
 * and ConsoleOutputTest.
 */
class AppTest {
    @Test
    void mainRunsAFullSessionToCheckout() {
        String script = String.join("\n",
            "Alice",       // name
            "IL",          // state
            "STANDARD",    // shipping
            "1",           // add item
            "Widget",      // item name
            "30.00",       // price
            "2",           // quantity
            "6"            // checkout
        ) + "\n";

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        try {
            System.setIn(new java.io.ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));
            App.main(new String[] {});
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        assertTrue(captured.toString(StandardCharsets.UTF_8).contains("transaction completed"),
            "a full scripted session should reach checkout");
    }
}
