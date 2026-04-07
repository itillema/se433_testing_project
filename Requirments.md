# Testing Project
This project will have you implement and test a shopping application. The application should only be a CLI application for simplicity so there is no need for a GUI.
## Shopping Application
Here is an outline of the application actions you will need to implement:

### Purchasing Items
- The customer enters the following information:
  - Their name
  - Their state of residence
  - Item to be purchased
  - Quantity of items to be purchases
  - A shipping option. One of:
    - Standard
    - Next day
- The available user actions are:
  - Add item to the shopping cart
  - Get current total
  - See contents of shopping cart
  - Edit quantity of items in shopping cart
  - Remove items from shopping cart
- Add item requirements
  - When the add item action selected, the given item is added to the shopping cart and the user receives a message indicating the current count of items in the cart.
- Get current total
  - When the get current total action is selected, the user receives a message that reports the total for the items in the shopping cart. Taxes and shipping charges are included in this sum. Taxes and shipping are defined in “taxes and shipping” section below.
- Checkout
  - When the checkout action is selected, a message “transaction completed” will be displayed to the user.
- Other requirements
  - A quantity of less than 1 or that is non integer should result in a error.
  - The smallest acceptable purchase amount is $1.The maximum acceptable purchase amount is $99,999.99.
### Taxes and Shipping
- 3 states require the payment of a sales tax
  - IL - Illinois
  - CA - California
  - NY - New York
  - In each case above, the sales tax is 6%.
  - All other states charge no tax.
- There are 2 kinds of shipping offered:
  - STANDARD - The cost of standard shipping is $10. If the raw purchase price is over $50, standard shipping is free.
  - NEXT_DAY - The cost of next day shipping is $25. There is no free NEXT_DAY shipping.

## Phase 1
Implement the application described above in Java. Create a GitHub private repository to store your project. You will need to make your Instructor (me) a Contributor to your repo so I can clone your project. 

The Initial Tech Setup slides in D2l/Content/Week1 gives instructions on how to create a free GitHub account, if you do not have one. It will also give you instructions on installing gh and git commands if you choose to use the CLI versions for source control. 

The Project Related Technology (Phase 1) Slides in D2l/Content/Week2 will help guide you on how to create a github repository for your project, make commits, and add your instructor as a contributor. 

***You must make a private repository for your project and you must not add any other contributors, except for your Instructor.***

After the due date I will review your code and add suggestions. It does not have to be perfect but it does have to attempt to demonstrate all of the functionality of project described above. Phase 2 will involve testing this program and updating the project to fix any defects.

If you made a reasonable attempt at the program and all of its requirements then you recieve full credit for this Phase. Phase 1 needs to be complete by Friday, end of the day, in Week 6 (see syllabus).

## Phase 2
Phase 2 will involve implementing unit, integration, system, and acceptance testing to your project using JUnit. I will be posting instuctions for installing JUnit soon in class. You will also need use a source code coverage tool and mutation testing tool to help find faults in your code. These faults should be corrected.

I will require screenshots of coverage results and mutation testing results. When completed you can add them directly to the root folder of your project.

***Mutation Testing***

If you are using Eclipse you can use PITEST for mutation testing, which is found as an extension for eclipse. If you prefer IntelliJ then there is the Pit Mutation Testing plugin that you can apply to your project. You should be able to add this to IntelliJ in Setting/Plugins.

***Code Coverage Tool***

Eclipse has ECLEMMA, as an extension, that you can use to analyze the code coverage of your JUnit tests.

IntelliJ has a built in ability to determine code coverage once you have JUnit added.

You need to write tests in way to complete these goals:
- The goal is to have enough tests across the four categories that cover all lines of code. Your tests in total should have 100% code coverage.
- The goal for mutation testing should be that you *kill all mutants*. Mutation testing generates mutated versions of your code, by introducing faults, and applies your testing to these mutants. If a mutant survives (passes) all your tests then your testing suite is not robust enough. You then need to add more tests so that every mutant fails some test. When a mutant fails a test then it is *killed*.

You will need to have this completed by the time and day of the final for this course in the finals week. This is posted in your Syllabus.