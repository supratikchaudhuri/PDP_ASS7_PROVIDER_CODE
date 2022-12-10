SETUP INSTRUCTIONS

Prerequisites:

1. Create a folder named 'Users' in the same directory as the teamKevinStocks.jar.
2. To load a user/portfolio from a file, create the TEMPLATE.xml file and replace the placeholder
values with the pertinent information, and move it into the Users folder created in step 1.
3. Import both libraries "jcommon-1.0.0.jar" and "jfreechart-1.0.1.jar" to the project by going
into File>Project Structures>Libraries>+ and add BOTH libraries to the project. The libraries are
included in the res folder. Performance over time in GUI can only work when both of these libraries
are imported.

__________________________________________________________________________________________________
Run the Program:
1. Open a terminal at the root of this directory.
2. Run the following command in the terminal: -jar teamKevinStocks.jar
3. Enter number in the console to select 1. CLI or 2. GUI.
4. Enjoy creating a user account and portfolio.

To create a portfolio:
    CLI:
    1. Log in to an existing account/Create a new account by entering a username.
    2. Select "1. Create a Portfolio" by inputting 1 to the terminal.
    3. Add stocks to the portfolio following instructions.

    GUI:
    1. Log in to an existing account/Create a new account by entering a username.
    2. Press the "1. Create a Portfolio" button.
    3. Add stocks to the portfolio by following the prompts.

To view the portfolios:
    CLI:
    1. Log in to an existing account/Create a new account and create a portfolio.
    2. Select "2. View your Portfolio." by inputting 2 to the terminal.
    3. The system will display all the portfolios that were created under this username.
    4. Select a portfolio, system will display the current holdings and total value of this
    portfolio. Following with operations including adding stock(s) to the portfolio, selling
    stock(s) from the portfolio, view the performance over time etc.

    GUI:
    1. Log in to an existing account/Create a new account and a new portfolio.
    2. Press the "2. View your Portfolios" button.
    3. The GUI will display all the portfolios that are associated with this account. Select the
    portfolio you wish to view.
    4. System will display the current holdings and total value of this portfolio. Following with
    operations including adding stock(s) to the portfolio, selling  stock(s) from the portfolio,
    view the performance over time, and checking the cost basis on a specified date.

