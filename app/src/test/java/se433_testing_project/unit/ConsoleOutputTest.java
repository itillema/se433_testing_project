package se433_testing_project.unit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import se433_testing_project.ConsoleOutput;
import se433_testing_project.Item;

@Tag("unit")
class ConsoleOutputTest 
{

    private static final String NL = System.lineSeparator();

    private ByteArrayOutputStream buf;
    private ConsoleOutput out;

    @BeforeEach
    void setUp() 
    {
        buf = new ByteArrayOutputStream();
        out = new ConsoleOutput(new PrintStream(buf, true, StandardCharsets.UTF_8));
    }

    private String captured() 
    {
        return buf.toString(StandardCharsets.UTF_8);
    }

    @Test
    void welcomePrintsBanner() 
    {
        out.welcome();
        assertEquals("=== Welcome to the Shopping Application ===" + NL, captured());
    }

    @Test
    void menuListsAllOptionsAndPrompt() 
    {
        out.menu();
        String expected = NL
            + "--- Menu ---" + NL
            + "1. Add item to cart" + NL
            + "2. View cart contents" + NL
            + "3. Edit item quantity" + NL
            + "4. Remove item from cart" + NL
            + "5. Get current total" + NL
            + "6. Checkout" + NL
            + "7. Quit without checking out" + NL
            + "Choose an option: ";
        assertEquals(expected, captured());
    }

    @Test
    void promptPrintsWithoutNewline() 
    {
        out.prompt("Name: ");
        assertEquals("Name: ", captured());
    }

    @Test
    void messagePrintsLine() 
    {
        out.message("hello");
        assertEquals("hello" + NL, captured());
    }

    @Test
    void itemAddedReportsCount() 
    {
        out.itemAdded(2);
        assertEquals("Item added. Cart now contains 2 item(s)." + NL, captured());
    }

    @Test
    void cartContentsEmptyShowsEmptyMessageAndNoTotal() 
    {
        out.cartContents(List.of(), 0.0);
        assertEquals("Your cart is empty." + NL, captured());
        assertFalse(captured().contains("Raw total"));
    }

    @Test
    void cartContentsListsItemsAndRawTotal() 
    {
        out.cartContents(List.of(new Item("Pen", 2.50, 3)), 7.50);
        String expected = "--- Cart Contents ---" + NL
            + "  Pen | price=$2.50 | qty=3 | subtotal=$7.50" + NL
            + "Raw total: $7.50" + NL;

        assertEquals(expected, captured());
    }

    @Test
    void quantityUpdatedConfirms() 
    {
        out.quantityUpdated();
        assertEquals("Quantity updated." + NL, captured());
    }

    @Test
    void itemRemovedTrueConfirmsRemoval() 
    {
        out.itemRemoved(true);
        assertEquals("Item removed." + NL, captured());
    }

    @Test
    void itemRemovedFalseReportsNotFound() 
    {
        out.itemRemoved(false);
        assertEquals("Item not found in cart." + NL, captured());
    }

    @Test
    void currentTotalIsFormattedToTwoDecimals() 
    {
        out.currentTotal(63.60);
        assertEquals("Current total (including tax and shipping): $63.60" + NL, captured());
    }

    @Test
    void finalTotalIsFormattedToTwoDecimals() 
    {
        out.finalTotal(63.60);
        assertEquals("Final total: $63.60" + NL, captured());
    }

    @Test
    void checkoutResultPrintsMessage() 
    {
        out.checkoutResult("transaction completed");
        assertEquals("transaction completed" + NL, captured());
    }

    @Test
    void errorPrefixesWithError() 
    {
        out.error("boom");
        assertEquals("Error: boom" + NL, captured());
    }

    @Test
    void invalidChoicePrintsGuidance() 
    {
        out.invalidChoice();
        assertEquals("Invalid choice. Please enter a number from 1 to 7." + NL, captured());
    }

    @Test
    void goodbyePrintsFarewell() 
    {
        out.goodbye();
        assertEquals("Goodbye!" + NL, captured());
    }

    
}
