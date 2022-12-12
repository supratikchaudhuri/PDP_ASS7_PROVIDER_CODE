package model;

import org.w3c.dom.Document;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.Month;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

/**
 * This interface represents the Model of the application's MVC design. It conducts all the
 * calculations and API handling for the Controller.
 */
public interface PortfolioModel {

  /**
   * Creates a new User object.
   *
   * @param username username of the user.
   * @return the newly created User object.
   */
  User createNewUser(String username);

  /**
   * Fetches the current user's Portfolio holding.
   *
   * @param user the current user.
   * @return a String representing the current holdings of the portfolio.
   */
  String getPortfolio(User user);

  /**
   * Create a new Portfolio for the current user.
   *
   * @param data a LinkedList of the StockImpl objects to be contained in the portfolio.
   * @param user the current user.
   * @return the newly created Portfolio.
   */
  Portfolio createUserPortfolio(Deque data, User user);

  /**
   * Searches through the current user's portfolio for a specific stock.
   *
   * @param ticker the stock's ticker symbol.
   * @param user   the current user.
   * @return a String representing the current position of the stock within the portfolio.
   */
  String searchPortfolio(String ticker, User user);

  /**
   * Calls on the AlphaVantage API to fetch the latest price of a stock.
   *
   * @param ticker the stock's ticker symbol.
   * @return a String representing the current value of the stock.
   */
  String getStock(String ticker);

  /**
   * Create a new user data file.
   *
   * @param username the current user's username.
   * @param fileName file path.
   * @throws ParserConfigurationException when file is not found.
   */
  void createNewUserFile(String username, String fileName) throws ParserConfigurationException;

  /**
   * Create a new portfolio under user's data file.
   *
   * @param fileName file path.
   * @throws IOException when file is not found.
   */
  void createNewPortfolio(String fileName) throws IOException;

  /**
   * Write document data to xml.
   *
   * @param doc    data containing nodes.
   * @param output FileOutputStream file path.
   * @throws TransformerException when it's not possible to create a new transformer instance.
   */
  void writeXml(Document doc, OutputStream output) throws TransformerException;

  /**
   * Trim white spaces between tags when parsing xml data.
   *
   * @param doc data containing nodes.
   * @throws XPathExpressionException if XPathFactory.newInstance().newXPath can't be evaluated.
   */
  void trimWhiteSpaces(Document doc) throws XPathExpressionException;

  /**
   * Returns the number of portfolios in this user's data file.
   *
   * @param fileName file path.
   * @return the number of portfolios as an integer.
   */
  int getPortfolioCount(String fileName);

  /**
   * Add stocks to an existing portfolio of user's choice.
   *
   * @param choosePortfolio the portfolio of user's choice.
   * @param addStockTicker  stock symbol.
   * @param addStockQty     stock quantity.
   * @param addStockDate    purchase date.
   */
  void addStockToPortfolio(String filename, String choosePortfolio, String addStockTicker,
                           String addStockQty, String addStockDate);

  /**
   * Returns the current date.
   *
   * @return the date.
   */
  String currentDate();

  /**
   * Validate the input date.
   *
   * @param addStockDate purchase date.
   * @return true if valid, false otherwise.
   */
  boolean dateValidation(String addStockDate);

  /**
   * Check if the stock user is selling is in the portfolio.
   *
   * @param filename        file path.
   * @param choosePortfolio Portfolio selection.
   * @param sellStockTicker the stock user is selling.
   * @param sellStockQty    the quantity user is selling.
   */
  boolean checkIfStockInPortfolio(String filename, String choosePortfolio, String sellStockTicker,
                                  String sellStockQty);

  /**
   * Sell stocks from the portfolio.
   *
   * @param filename        file path.
   * @param choosePortfolio Portfolio selection.
   * @param sellStockTicker the stock user is selling.
   * @param sellStockQty    the quantity user is selling.
   */
  void sellStockFromPortfolio(String filename, String choosePortfolio, String sellStockTicker,
                              String sellStockQty);

  /**
   * Get stock count.
   *
   * @param filename        file path.
   * @param choosePortfolio Portfolio selection.
   * @return number of stocks in this portfolio.
   */
  int getStockCount(String filename, String choosePortfolio);

  /**
   * Gets the daily performance over the course of a specified month.
   *
   * @param portfolio the portfolio to get the progress of.
   * @param startDate the start date of the timeline.
   * @return a String representing the overall performance over a time period.
   */
  String getDailyPerformance(Portfolio portfolio, String startDate);

  /**
   * Gets the monthly performance over the course of a timeframe.
   *
   * @param portfolio the portfolio to get the progress of.
   * @param startDate the start date of the timeline.
   * @param endDate   the end date of the timeline.
   * @return a String representing the overall performance over a time period.
   */
  String getMonthlyPerformance(Portfolio portfolio, String startDate, String endDate);

  /**
   * Get the cost basis before a specific date.
   *
   * @param portfolio     the portfolio to get the progress of.
   * @param costBasisDate the date for determine cost basis.
   * @return cost basis before and including this date.
   */
  String costBasis(Portfolio portfolio, String costBasisDate, double commissionRate);

  /**
   * Purchases stocks based on the percentages of the total dollars.
   *
   * @param stockWeights    a HashMap of stocks and their quantities.
   * @param dollars         the total dollar limit.
   * @param filename        the user's file.
   * @param choosePortfolio the portfolio number.
   */
  void portfolioDCA(HashMap<String, Integer> stockWeights, int dollars, String filename,
                    String choosePortfolio);

  boolean validateTicker(String ticker);

  /**
   * Return the monthly portfolio value.
   *
   * @param month           target month.
   * @param choosePortfolio Portfolio selection.
   * @param filename        file path.
   * @return the monthly portfolio value.
   */
  double getMonthlyTotalValue(Month month, String choosePortfolio, String filename);

  /**
   * Returns the current value of the given portfolio.
   *
   * @param portfolio the portfolio.
   * @return the current value.
   */
  double getCurrentValue(Portfolio portfolio);

  /**
   * Get the total value of the stocks sold on a certain date.
   *
   * @param ticker the company's ticker symbol.
   * @param date   the date sold.
   * @param qty    the number of stocks sold.
   * @return the value.
   */
  String totalSoldOnDate(String ticker, String date, int qty);

  void rebalance(Portfolio currentPortfolio, Map<String, BigDecimal> currStocks,
                 Map<String, Double> weights, String fileName, String choosePortfolio);
}
