package se433_testing_project;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class AppTest 
{
    @Test
    void appCanBeConstructed() 
    {
        Scanner scanner = new Scanner("");
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        App app = new App(scanner, out);
        assertNotNull(app);
    }
}
