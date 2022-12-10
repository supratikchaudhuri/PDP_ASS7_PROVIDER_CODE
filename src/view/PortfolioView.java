package view;

import model.User;

/**
 * This interface represents the View of the application's MVC design.
 */
public interface PortfolioView {

  /**
   * The initial greeting on startup.z
   */
  void initialGreeting();

  /**
   * Ask if the user wants to create a new account.
   */
  void createNewAccountMessage();

  /**
   * Message when a new account is created.
   */
  void newAccountCreatedMessage();

  /**
   * The main menu being presented to the user.
   *
   * @param user the current user.
   */
  void displayUserOptions(User user);

  /**
   * Ask the user for the ticker symbol.
   */
  void askForTickerSymbol();

  /**
   * Ask the user for the quantity of shares.
   */
  void askForQuantity();

  /**
   * Displays the portfolio holdings of a user.
   *
   * @param portfolio a String representation of all the stocks in a user's portfolio.
   */
  void displayPortfolio(String portfolio);

  /**
   * Notify the user of an invalid response.
   */
  void invalidResponse();

  /**
   * Ask the user if they'd like to add another stock to their portfolio.
   */
  void addAnother();

  /**
   * Goodbye message before terminating program.
   */
  void goodBye();

  /**
   * Shows the user the requested stock's information.
   *
   * @param info a String containing all of the stock's data.
   */
  void displayStockInfo(String info);

  /**
   * Shows the user a custom message.
   *
   * @param message a String with a custom message.
   */
  void displayCustom(String message);

  /**
   * Logging in message.
   */
  void logIn();

  /**
   * Shows a menu that includes all the portfolios this user has created.
   */
  void viewPortfolio(int count);

  /**
   * Shows an options menu after viewing the portfolio.
   */
  void viewPortfolioOptions();

  /**
   * Shows an options menu for adding stocks to the portfolio.
   */
  void addStocksOptions();

  /**
   * Asks for stock ticker when adding a new stock into an existing portfolio.
   */
  void addStockTicker();

  /**
   * Asks for stock quantity when adding a new stock into an existing portfolio.
   */
  void addStockQty();

  /**
   * Asks for purchase date when adding a new stock into an existing portfolio.
   */
  void addStockDate();

  /**
   * Asks for stock ticker when selling a stock from an existing portfolio.
   */
  void sellStockTicker();

  /**
   * Asks for stock quantity when selling a stock from an existing portfolio.
   */
  void sellStockQty();

  /**
   * Displays a message when stocks are sold successfully.
   *
   * @param sellStockTicker the company's ticker symbol.
   * @param sellStockQty    the quantity of stocks sold.
   */
  void soldStocks(String sellStockTicker, String sellStockQty);

  /**
   * Asks if the user would like to sell another stock.
   */
  void sellAnother();

  /**
   * Shows the user there are no more stocks in this portfolio.
   */
  void emptyPortfolio();

  /**
   * Asks for the cost basis date.
   */
  void costBasisDate();

  /**
   * Shows a menu when user creates a portfolio.
   */
  void createPortfolioMenu();
}
