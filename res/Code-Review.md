## 1. Design critique

- The code has not utilized the benefits of dynamic dispatch. This makes the presence of interface
  class non-existent.
  Currently, each interface has one implementation class. See suggestions section for more details
  on this.


- Data persistence: In the XML file, it can be seen that every buy adds a new log to the portfolio,
  while sell method replaces the old value with the new updated value irrespective of the date of
  purchase in the xml file. This seems inconsistent design since one method adds a log to the
  portfolio while the other method (which is performing something similar) is doing the exact
  opposite. Would be better to choose one of these methodologies and stick with it throughout the
  project.

## 2. implementation critique

- In the alphaVantage API, the endpoint can accept different function as query. Throughout this
  project, just using the `TIMESERIES_DAILY`
  would have sufficed. But the provider's code uses `montly` in some instances, `global` in others.
  This leads to inconsistencies.
  This also causes bugs as the `global` function does not have data for all the tickers (ex - PNG's
  price data is not available in the `global` function
  of the alphaVantage API).


- Choice of data-type to represent stocks in portfolio: The provider's code uses `BigDecimal`
  type to store the stock's quantity.
  This is not a problem as in some instances the quantity may even reach quadrillions (in penny
  stocks). However, while buying stocks, you convert the value from big decimal to double, which
  defeats its purpose since big decimal can also have numbers that might exceed double's max value.
  Also, selling in the provided code is done in integer, which again defeats the purpose of nig
  decimal and leads to inconsistencies in implementation. Would be better to just stick to 1 data
  type like double throughout the project.

Similarly while selling stocks, the user can only choose to sell quantity as an integer, which is
then converted to BigDecimal by the model.
This means that if the portfolio has 50.5 shares of company X, they can only sell atmost 50 shares
of X. It would have been better to accept sell quantity as double/BigDecimal.
`sellStockFromPortfolio` method in `PortfolioModelImp.java`


- Buy and sell methods accepts quantity to buy/sell stock as `String` data types which is then
  converted to double/Integer/BigDecimal in model for processing.
  It should've been the controller's job to do this conversion and feed BigDecimal as argument to
  the methods in the model.
  This is because the model processing power should be (as limited as possible) to perform actions
  it was designed for, i.e. a portfolio (buy, sell, cost-basis, etc) in this case.


- Wastage of API calls: In the GUI after selecting a portfolio, it always displays the current
  composition of the portfolio and their values (as of the latest data).
  This leads to usage of AlphaVantage API to fetch data when not asked. In cases the portfolio
  consists of high number of stocks of different companies, each render on the "View Portfolio"
  page would take a long time. This can be avoided by adding a separate menu option of viewing
  portfolio contents/value to preserve API calls.


- If you purchase a stock on two different date, the home page displays same stock with tow
  different quantity. This
  would've been fine if it also printed the purchase date, but it doesn't. It makes the UI
  cluttered. Moreover, it calls the API to
  fetch the price details of the stocks watch and everytime it encounters a ticker (doesn't matter
  if we've already fetched the data for that ticker once).
  Thus, it makes the rendering very slow at times.


- In GUI, you cannot sell partial stocks quantities. The program provides a dropdown that displays (
  1...n) where n is number of stocks
  of a company in our portfolio. You can only choose an integer amount to sell.

## 3. Documentation critique

- Every method has been documented well for the customer's to understand their purpose.
  It was easy for us to use these methods just by reading the documentation from the interface file
  alone and
  not having to see the under the hood implementation in a handful of cases.


- The buy and sell chronology explanation is missing from your readme files. Currently, the
  portfolio is able to sell stocks even before it has
  purchased them. If we sell stocks on current date, it doesn't adjust the quantity for the future
  transactions (that have already gone through).
  Was the portfolio suppose to do this or was it a bug was unclear to us since there was no
  clarification about this in the ReadMe files.

## 4. Design/code strengths 

- The code has well-defined interface and their implementation classes. This made adding new
  features to the portfolio fairly simple.


- Code was clean and not cluttered at all. It was easy to navigate through function and understand
  their working.


- Persisting the portfolio in XML format is a smart move. It made manually reading the raw data
  files very easy while testing our methods.

## 5. Design/code limitations

- There is no Inflexible portfolio.


- Not handling invalid data and edge cases. In some cases, the sell function fails when the user
  tries to
  sell stocks on a holiday/weekend. In some instances they work.
  If a user stops midway in the portfolio creation process, then the portfolio is still created (no
  contents inside). However, when you try to read this empty portfolio,
  the xml module fails and throws IO exception.


- No go back option in the middle of an operation. If the user wants to go back to a previous
  option, they will have to complete the current operation first.
  Also, in some cases, you have to re-run the entire application to get the main menu, since there
  is no `go back` / `get main menu` button on certain panels. **Suggestion** : Instead of resetting
  the
  panel before every method in gui executes, you can use card layout to store multiple panels in the
  same jframe and display the previous/next panel from the layout depending on the user's choice.


- There is no feature in the program to get the contents of the portfolio, or its value on a
  specific date. The absence of this function makes
  testing other methods such as buy, sell, re-balance on specific date a tedious task since you have
  to manually check the .xml file to see the changes on a particular date.

## 6. Suggestions on how to address them.

- Purpose of interfaces is to have multiple implementations. In your case, each interface has one
  implementation class.
  Use dynamic dispatch's benefit. Use abstract class for similar function and extend specific
  changes on them through concrete implementation class.
  This could've been done in `PortfolioControllerGUI.java` and `PortfolioController.java` interfaces
  which share same function signatures.


- Do all the data processing in the controller (eg- converting string to BigDecimal, string to
  double, etc.) rather than in the model.
  Model should only consist of business logic that is closely tied to the purpose it was built for.


- The models send the contents of the portfolio in a statement format (String). This severely limits
  the code re-usability aspect this method. Models methods might need to be called in several places
  as a pre-requisite but returning a string from the methods limit its re-usability when another
  function might require a HashMap or Deque like classes.
  `PortfolioImpl.java Line 71: String newLine = "Ticker symbol: " + ticker + " Quantity: " + qty + " Value: " + formatter.format(totalValue) + "\n";`
  Instead of the line above, you should return a hashmap and then let the controller form statements
  off of the data that is received.
  Doing this will allow other methods to fetch the portfolio contents without having to extract
  required pieces of information from the string (by doing split or other string manipulation
  methods).


- In GUI, you provide a dropdown to choose the quantity of stock we want to sell. In case user want
  to sell 10 million stocks, he'll have to scroll
  from 1 to all the down to 10,000,000. This is highly inconvenient, not to mention you cannot sell
  partial stocks. (Eve though you pass the quantity to sell in BIgDecimal, you take its floor by
  getting its intValue() which defeats the purpose of BigDecimal). Your code would face problems
  when the intValue of the BigDecimal exceeds the max value that int data type ca store. Hence, it
  would be better to have consistent design accross all classes in MVC and use BigDecimal
  everywhere.
  Accept the quantity of the stocks to sell in a JTextField. This will allow user to sell large and
  partial quantities with ease.

## 7. Some major bugs in the program

- Unable to read portfolio if the API uses a function (`GLOBAL_QUOTE`) in the endpoint for some
  tickers, as described
  above. This cause the code to break.


- Code going to infinite loop when trying to exit program in text-based interface.


- Sell method fails when we try to sell stock on a date that is a holiday/weekend.


- Sometimes creating a portfolio fails, but on re-running the program and following the same steps
  as before, it works fine. We were not able to figure out why this is happening.


**Overall, it was fun exploring your code. Hope you receive our critique in a positive manner.** 

