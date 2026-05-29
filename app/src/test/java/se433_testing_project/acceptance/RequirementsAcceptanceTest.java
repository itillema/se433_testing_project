package se433_testing_project.acceptance;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import se433_testing_project.ConsoleInput;
import se433_testing_project.ConsoleOutput;
import se433_testing_project.Item;
import se433_testing_project.MenuController;
import se433_testing_project.OrderProcessor;
import se433_testing_project.ShippingOption;
import se433_testing_project.ShoppingCart;
import se433_testing_project.TaxCalculator;


// Acceptance tests for each requirement 
@Tag("acceptance")
class RequirementsAcceptanceTest 
{

    private ByteArrayOutputStream buf;

    private MenuController controller(String script) 
    {
        buf = new ByteArrayOutputStream();
        ConsoleOutput out = new ConsoleOutput(new PrintStream(buf, true, StandardCharsets.UTF_8));
        ConsoleInput in = new ConsoleInput(new Scanner(script), out);
        return new MenuController(in, out);
    }

    private String captured() 
    {
        return buf.toString(StandardCharsets.UTF_8);
    }

    private OrderProcessor processor(String state, ShippingOption ship, Item... items) 
    {
        ShoppingCart cart = new ShoppingCart();
        for (Item i : items) 
        {
            cart.addItem(i);
        }

        return new OrderProcessor(cart, "Alice", state, ship);
    }

    // Item added, user receives a message indicating the current count of items in the cart
    @Test
    void addItemReportsCurrentCount() 
    {
        ShoppingCart cart = new ShoppingCart();
        controller("Widget\n10.00\n1\n").addItem(cart);
        assertEquals(1, cart.getItemCount());
        assertTrue(captured().contains("Cart now contains 1 item(s)."));
    }

    // checks taxes and shipping charges are included in this sum
    @Test
    void getTotalIncludesTaxesAndShipping() 
    {
        // raw 60 in IL with NEXT_DAY: 60 + 3.60 tax + 25 shipping = 88.60
        OrderProcessor p = processor("IL", ShippingOption.NEXT_DAY, new Item("Widget", 30.0, 2));
        assertEquals(88.60, p.calculateTotal());
    }

    // A message 'transaction completed' will be displayed to the user
    @Test
    void checkoutReportsTransactionCompleted() 
    {
        OrderProcessor p = processor("TX", ShippingOption.STANDARD, new Item("Widget", 30.0, 2));
        assertEquals("transaction completed", p.checkout());
    }

    // Quantity of less than 1 returns an error
    @Test
    void quantityLessThanOneIsAnError() 
    {
        assertThrows(IllegalArgumentException.class, () -> new Item("Widget", 5.0, 0));
    }

    // Non integer should result in a error
    @Test
    void nonIntegerQuantityIsRejected() 
    {
        // A non-integer quantity is refused, prompt repeats until a valid integer is given
        ShoppingCart cart = new ShoppingCart();
        controller("Widget\n10.00\n1.5\n2\n").addItem(cart);
        assertTrue(captured().contains("Please enter a valid integer."));
        assertEquals(1, cart.getItemCount());        // recovered with valid 2
        assertEquals(2, cart.getItems().get(0).getQuantity());
    }

    //Smallest acceptable purchase amount is $1
    @Test
    void smallestAcceptablePurchaseIsOneDollar() 
    {
        assertDoesNotThrow(() -> processor("TX", ShippingOption.STANDARD, new Item("Tiny", 1.0, 1)).calculateTotal());
        assertThrows(IllegalStateException.class, () -> processor("TX", ShippingOption.STANDARD).calculateTotal()); // empty cart, 0
    }

    // Max acceptable purchase amount is $99,999.99
    @Test
    void largestAcceptablePurchaseIs99999_99() 
    {
        assertDoesNotThrow(() -> processor("TX", ShippingOption.STANDARD, new Item("Big", 99999.99, 1)).calculateTotal());
        assertThrows(IllegalStateException.class, () -> processor("TX", ShippingOption.STANDARD, new Item("Huge", 50000.0, 2)).calculateTotal());
    }

    // 3 states require sales tax, IL, CA, NY, all 6%
    @Test
    void taxedStatesChargeSixPercent() 
    {
        assertEquals(6.0, TaxCalculator.calculateTax("IL", 100.0));
        assertEquals(6.0, TaxCalculator.calculateTax("CA", 100.0));
        assertEquals(6.0, TaxCalculator.calculateTax("NY", 100.0));
    }

    // All other states charge no tax
    @Test
    void otherStatesChargeNoTax() 
    {
        assertEquals(0.0, TaxCalculator.calculateTax("WA", 100.0));
    }

    // Standard shipping is $10
    @Test
    void standardShippingCostsTenDollars() 
    {
        assertEquals(10.0, ShippingOption.STANDARD.calculateShippingCost(20.0));
    }

    // If the raw purchase price is over $50, standard shipping is free
    @Test
    void standardShippingIsFreeOverFifty() {
        assertEquals(0.0, ShippingOption.STANDARD.calculateShippingCost(50.01));
    }

    // Next day is $25 regardless of purchase price
    @Test
    void nextDayShippingAlwaysCostsTwentyFive() {
        assertEquals(25.0, ShippingOption.NEXT_DAY.calculateShippingCost(10.0));
        assertEquals(25.0, ShippingOption.NEXT_DAY.calculateShippingCost(1000.0));
    }

    
}
