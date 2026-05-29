package se433_testing_project.unit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import se433_testing_project.ConsoleInput;
import se433_testing_project.ConsoleOutput;
import se433_testing_project.ShippingOption;

@Tag("unit")
class ConsoleInputTest 
{

    private ByteArrayOutputStream buf;

    private ConsoleInput input(String script) 
    {
        buf = new ByteArrayOutputStream();
        ConsoleOutput out = new ConsoleOutput(new PrintStream(buf, true, StandardCharsets.UTF_8));
        return new ConsoleInput(new Scanner(script), out);
    }

    private String captured() 
    {
        return buf.toString(StandardCharsets.UTF_8);
    }

    @Test
    void promptIntReturnsValueOnFirstValidInput() 
    {
        ConsoleInput in = input("7\n");
        assertEquals(7, in.promptInt("Qty: "));
        assertTrue(captured().contains("Qty: "));   // prompt text was shown
    }

    @Test
    void promptIntRepromptsOnNonInteger() 
    {
        assertEquals(12, input("abc\n12\n").promptInt("Qty: "));
        assertTrue(captured().contains("Please enter a valid integer."));
    }

    @Test
    void promptDoubleReturnsValueOnFirstValidInput() 
    {
        ConsoleInput in = input("2.5\n");
        assertEquals(2.5, in.promptDouble("Price: "));
        assertTrue(captured().contains("Price: "));
    }

    @Test
    void promptDoubleRepromptsOnNonNumber() 
    {
        assertEquals(3.5, input("x\n3.5\n").promptDouble("Price: "));
        assertTrue(captured().contains("Please enter a valid number."));
    }

    @Test
    void promptNonEmptyReturnsTrimmedValue() 
    {
        ConsoleInput in = input("  Bar  \n");
        assertEquals("Bar", in.promptNonEmpty("Name: "));
        assertTrue(captured().contains("Name: "));
    }

    @Test
    void promptNonEmptyRepromptsOnBlankLines() 
    {
        assertEquals("Foo", input("\n   \nFoo\n").promptNonEmpty("Name: "));
        assertTrue(captured().contains("Input cannot be empty."));
    }

    @Test
    void promptShippingReturnsOptionOnFirstValidInput() 
    {
        ConsoleInput in = input("STANDARD\n");
        assertSame(ShippingOption.STANDARD, in.promptShipping());
        assertTrue(captured().contains("Shipping option (STANDARD or NEXT_DAY): "));
    }

    @Test
    void promptShippingRepromptsOnInvalid() 
    {
        assertSame(ShippingOption.NEXT_DAY, input("bogus\nNEXT_DAY\n").promptShipping());
        assertTrue(captured().contains("Error: Invalid shipping option: bogus"));
    }

    @Test
    void readMenuChoiceTrimsWhitespace() 
    {
        assertEquals("3", input("  3  \n").readMenuChoice());
    }

    @Test
    void readMenuChoiceReturnsSentinelAtEof() 
    {
        assertEquals(ConsoleInput.NO_INPUT, input("").readMenuChoice());
    }

    @Test
    void promptNonEmptyThrowsAtEof() 
    {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> input("").promptNonEmpty("Name: "));
        assertEquals("No more input available", e.getMessage());
    }

    @Test
    void promptIntThrowsAtEof() 
    {
        assertThrows(IllegalStateException.class, () -> input("").promptInt("Qty: "));
    }

    @Test
    void promptDoubleThrowsAtEof() 
    {
        assertThrows(IllegalStateException.class, () -> input("").promptDouble("Price: "));
    }

    @Test
    void promptShippingThrowsAtEof() 
    {
        assertThrows(IllegalStateException.class, () -> input("").promptShipping());
    }
}
