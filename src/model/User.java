package model;

/**
 * This interface represents a User that can create and view a Portfolio containing their current
 * stock holdings.
 */
public interface User {

  /**
   * Gets the user's first and last name.
   *
   * @return a String representation of the user's first and last name.
   */
  String getUsername();

  /**
   * Shows the current holdings of the user's portfolio.
   *
   * @return a String representation of all the stocks within the user's portfolio.
   */
  String viewPortfolio();

  /**
   * Assigns a portfolio to the user.
   *
   * @param portfolio Portfolio object created through text interface.
   */
  void setUserPortfolio(Portfolio portfolio);

  /**
   * Search the user's portfolio for a specific stock.
   *
   * @param ticker the stock's ticker symbol.
   * @return a String representing the user's current position in the stock.
   */
  String searchPortfolio(String ticker);
}
