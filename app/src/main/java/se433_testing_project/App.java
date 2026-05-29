package se433_testing_project;

import java.util.Scanner;

public class App 
{
    public static void main(String[] args) 
    {
        try (Scanner scanner = new Scanner(System.in)) 
        {
            ConsoleOutput out = new ConsoleOutput(System.out);
            ConsoleInput in = new ConsoleInput(scanner, out);
            new MenuController(in, out).run();

        }

    }
}
