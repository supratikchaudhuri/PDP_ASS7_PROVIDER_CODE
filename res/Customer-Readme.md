# How to Run the JAR File

1. cd "res/Jar Files" in terminal

2. Type `java -jar ass7.jar` to run the JAR file.\
   The program should start in the terminal. Follow the instruction displayed to use text / GUI
   interfaces.

If you encounter any problem, follow the steps below: 

1. The project provided to us mandates having a "Users" folder inside the Jar Files folder.

2. Our jar file includes this folder but since we have developed it using macOS, Windows users might
   not have this folder in the jar. In that case, please create a new Jar with a "Users" folder
   inside the "Jar Files" folder of res. Also place the jar in this folder to run.

3. This was something that came with the provider's code and was out of our control. Sorry for the
   inconvenience and hope you understand.

If the above method does not work.


1. Please run the `Main.java` file in the `/src`. \
We were not able to fix this as we did not get much help from our providers. The jar files that is created
is unable to access other files. Hence, it crashes in the first step i.e, cannot create or read existing portfolios.


# Features that we were able to implement

### What we were able to do

1. We implemented the re-balancing of the portfolio on today's date based on the weightages supplied
   by the client.


2. Tested the model for re-balancing on today's date for equals as well as unequal weightages.

### What we were not able to do

1. We were not able to re-balance portfolio on a specific date. This was because of the following
   reasons:

    - The providers have not implemented a method where we can fetch the contents of the portfolio
      on a given date. We can only get contents as of the latest date.
      As a result, there is no way for us to figure out what stocks should be allowed for balancing
      the portfolio.

    - There is a function to get the value of portfolio on a particular date, it does not give the correct value
   based on the quantity. As a result, we weren't able to mimic it to build our own composition function. 

    - It was also not possible to get the composition since they add a new entry into the xml for
      buying stock on a date but for selling, simply update the first occurrence of the ticker in the
      file irrespective of the date hence not persisting how many stocks were present on which date.
      For example, buying 10 GOOG on 2022-10-10 and selling 5 GOOG on 2021-10-10 just reduces the
      value in xml to 5 GOOG on 2022-10-10, so we have no way of getting the correct composition of
      portfolio for 2021-10-10 (since it is not persisted).

    - Although we did write the code for re-balancing on a specific date, we have commented it out (
      in `PortfolioControllerImpl` class) since we can show our algorithm works correctly for
      today's date only. If they fix their sell method, we can uncomment that part of the code and
      re-balancing on a particular date will start working.


2. Not complete accuracy in re-balancing. Fractional inaccuracy persists.

    - Our providers use `BigDecimal` data types handle quantity of a stock. When we buy a stock, the
      view accepts quality as string.
      The controller than sends this string to the model, which then convert this string to a
      double, and then converts it to a BigDecimal

    - While selling stocks from the, the model can sell whole values of stocks (integers not
      doubles/BigDecimals). As a result, the quantity to be sold
      by our re-balancing algorithm (which is accurate) would round down that value and miss out on
      a small fraction of stocks that is supposed to be sold.

    - We have changed the data types from int to double/BigDecimal to the best of our knowledge but
      the problem still persists in some places.


3. We were not able to test the re-balancing function. This is due to the following reasons:

    - The providers have not written any test for their controllers and views. As a result we were
      not able to add controller and view
      tests.

    - We tried to create new test classes for the controllers and view, however, we were not able to
      do it properly
      as it was hard to understand the coupling between model view and controllers in their program.

### Changes we made to the provider's code

- Added a method to get list of all stocks in the portfolio on today's date in `PortfolioImpl`
  class. This is used to get all current stock in portfolio that need to be rebalanced. (Line 357).


- Added option number 6 to "Re-balance a portfolio" in `PortfolioViewImpl` class. (Line 112).


- The code we received did not run with text-based interface since their file can save decimal
  stocks but text-based reads only integer values. Hence changed all data types from int to double
  wherever applicable to make the code work before implementing re-balance.


- `PortfolioControllerImpl` line 415 :  Added logic to handle portfolio re-balancing option chosen
  by the user from the view. Performed all validations and passed th data to model for rebalancing.


- Added balance method in `PortfolioModelImpl` class to calculate which stock to buy/sell to match
  the specified weightage. (Line 485)


- Added Panel to display stocks in current portfolio and get their required weightage
  in `PortfolioGUIViewImpl` class (Line 647).


- Added action listener in `PortfolioControllerGuiImpl` that binds click to the controller's
  balance method. Performed all validation here and passed the processed data to model for doing
  the actual re-balancing. (Line 393).


- Added tests for verifying working of re-balancing on today's date in `PortfolioModelImpl` class (
  Line 142 onwards).


- After balancing is done, we have displayed the rebalanced portfolio contents to the user using
  our own implemented method since their existing method requires to go back and choose this
  portfolio again which reads the xml file again, leading to redundant api calls and taking more
  time to display the result to the user. Their existing method also does api calls for same ticker
  more than once increasing processing time even more.

### Help from Providers

- We received little to no help from our providers. They sent us code that does not follow the code
  styling as per the handing server. We had to spend a lot of time fixing the java style.


- We requested them to take a look at fixing bugs that we were facing in their code, but they took
  too long to respond and ultimately did not address problems that were pre-requisites for this
  assignment (like buying and selling functionality) and pointed us in the direction of tweaking the
  code on our own.


- We did not receive the questionnaire that was supposed to be sent along with the code.

