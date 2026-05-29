package se433_testing_project.system;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import se433_testing_project.App;
import se433_testing_project.ConsoleInput;
import se433_testing_project.ConsoleOutput;
import se433_testing_project.MenuController;

// End to end system tests
@Tag("system")
class AppSystemTest 
{

    private static String script(String... lines) 
    {
        return String.join("\n", lines) + "\n";
    }

    private String runSession(String script) 
    {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ConsoleOutput out = new ConsoleOutput(new PrintStream(buf, true, StandardCharsets.UTF_8));
        ConsoleInput in = new ConsoleInput(new Scanner(script), out);
        new MenuController(in, out).run();
        return buf.toString(StandardCharsets.UTF_8);
    }

    @Test
    void fullSessionAddsItemViewsTotalAndChecksOut() 
    {
        String output = runSession(script(
            "Alice",       // name
            "IL",          // state
            "STANDARD",    // shipping
            "1",           // add item
            "Widget", "30.00", "2",
            "5",           // get total
            "6"            // checkout
        ));

        assertTrue(output.contains("=== Welcome to the Shopping Application ==="));
        assertTrue(output.contains("Choose an option: "));   // menu was rendered each loop
        assertTrue(output.contains("Item added. Cart now contains 1 item(s)."));
        assertTrue(output.contains("Current total (including tax and shipping): $63.60"));
        assertTrue(output.contains("Final total: $63.60"));
        assertTrue(output.contains("transaction completed"));

    }

    @Test
    void quitOptionExitsWithGoodbye() 
    {
        String output = runSession(script("Bob", "TX", "NEXT_DAY", "7"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void appClassIsInstantiable() 
    {
        // App only exposes main() so cover its default constructor
        assertNotNull(new App());
    }

    @Test
    void mainWiresRealStreamsAndRunsToCheckout() 
    {
        String script = script(
            "Alice", "IL", "STANDARD",
            "1", "Widget", "30.00", "2",
            "6"
        );

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        
        try 
        {
            System.setIn(new ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));
            App.main(new String[] {});
        } 
        
        finally 
        {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        assertTrue(captured.toString(StandardCharsets.UTF_8).contains("transaction completed"));
    
    
    }
}
