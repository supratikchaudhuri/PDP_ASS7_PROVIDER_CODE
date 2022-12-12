package view;

import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This interface represents this application's Graphical User Interface.
 */
public interface PortfolioGUIView {

  /**
   * Set the listener for any actions, and perform functions according to the action commands.
   *
   * @param listener listener.
   */
  void setListener(ActionListener listener);

  /**
   * Displays the Java Swing Objects including JLabels, JButtons, JTextField etc.
   */
  void display();

  /**
   * Return the username entered in the username text field.
   *
   * @return username string.
   */
  String getUsernameInputString();

  /**
   * Returns the weight assigned to a specific stock.
   *
   * @return the weight.
   */
  int getStockWeight();

  /**
   * Displays JLabels and JButtons that ask if the user would like to create an account under this
   * username.
   */
  void createNewAccountMessage();


  /**
   * Displays the default menu when a user logs in.
   */
  void loggingInMenu();

  /**
   * Displays a JLabel and a JTextField for the user to enter stock ticker.
   */
  void askForStockTicker();

  /**
   * Displays a JLabel and a JTextField for the user to enter stock quantity.
   */
  void askForStockQty();

  /**
   * Returns the stock ticker entered in the stock ticker text field.
   *
   * @return stock ticker string.
   */
  String getStockTickerInputString();

  /**
   * Returns the stock quantity entered in the stock quantity text field.
   *
   * @return stock quantity string.
   */
  String getStockQtyInputString();

  /**
   * Displays options when a user declines creating a new account.
   */
  void reEnterUsername();

  /**
   * Displays a JLabel and yes/no JButtons to decide if the user would like to add a stock purchase
   * date.
   */
  void askForStockDate();

  /**
   * Displays a JLabel and a JTextField for the user to enter stock purchase date.
   */
  void addDate();

  /**
   * Returns the stock purchase date entered in the stock purchase date text field.
   *
   * @return stock purchase date string.
   */
  String getStockDateInputString();

  /**
   * Displays options for adding another stock.
   */
  void addAnotherStock();

  /**
   * Displays the portfolios the user has.
   *
   * @param portfolioCount number of portfolios.
   */
  void displayPortfolios(int portfolioCount);

  /**
   * Gets the highlighted selection from the dropdown menu.
   *
   * @return the highlighted selection.
   */
  String getDropDownSelection();

  /**
   * Displays the selected portfolio and options including add stock(s), sell stock(s), and view
   * histograms for performance over time.
   *
   * @param portfolio portfolio string.
   */
  void viewPortfolioOptions(String portfolio);

  /**
   * Shows all the stocks available to sell.
   *
   * @param stockList a HashMap of all the stocks in the portfolio.
   */
  void sellStockSelection(HashMap<String, BigDecimal> stockList);

  /**
   * Shows the total value of the sold stocks.
   *
   * @param value the value.
   */
  void confirmSold(String value);

  /**
   * Shows the cost basis breakdown of the portfolio.
   *
   * @param portfolio the portfolio cost.
   */
  void displayCostBasisConfirmation(String portfolio);

  /**
   * Displays a JLabel and a JTextField for the user to enter the stock quantity the user wishes to
   * sell.
   */
  void askForQtySell();

  /**
   * Displays a JLabel and a JTextField for the user to enter the stock sell date the user wishes to
   * sell.
   */
  void askForDateSell();

  /**
   * Displays a dropdown menu for the user to select time frames for the performance histograms.
   */
  void performanceChartAskForTimeframe();

  /**
   * Displays the performance histogram within a specified timeframe.
   *
   * @param performance monthly performance in the timeframe.
   * @param timeframe   timeframe.
   */
  void performanceChart(double[] performance, String timeframe);

  /**
   * Begins the cost basis loop and asks user for a date.
   */
  void costBasis();

  /**
   * Shows the user the cost basis.
   *
   * @param cost the cost.
   */
  void displayCostBasis(String cost);

  /**
   * Collects the stocks and their weight for a fund.
   */
  void fundBreakdown();

  /**
   * Adds another stock and weight selection option for the user.
   */
  void addStockToPanel();

  /**
   * Requests how much money to invest in the fund.
   */
  void amountToInvest();

  /**
   * Returns the value of the dollar amount field.
   *
   * @return the dollar amount.
   */
  int getDollarAmount();

  /**
   * Asks the user for the weightages of the stocks for portfolio balancing.
   *
   * @param tickers tickers whose weightage is asked
   */
  void getRebalanceWeightage(Set<String> tickers);

  /**
   * Returns the weightages of stocks in string format.
   *
   * @return weightages of the stock.
   */
  String getRebalanceWeightage();

  /**
   * Shows Shows a popup message in the GUI.
   *
   * @param msg   Message to be displayed.
   * @param title title of dialog box.
   */
  void showPopupMsg(String msg, String title);

  /**
   * This method displays the new values of stocks in portfolio after re-balancing.
   *
   * @param expectedValuesMap map of tickers and their balanced values.
   */
  void displayNewWeightedValues(Map<String, Double> expectedValuesMap);
}
