package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * This interface represents a portfolio for an individual user. It contains methods that provide
 * the user with the ability to view/create their current stock holdings.
 */
public interface Portfolio {

  Map<String, BigDecimal> getComposition(LocalDate date);

  /**
   * Returns the given User's portfolio holdings.
   *
   * @return the portfolio object belonging to the user.
   */
  String getPortfolio();

  /**
   * Searches the user's portfolio for the specified stock ticker.
   *
   * @param ticker the stock that the user wants to search for.
   * @return a String representation of the current holdings of the provided ticker.
   */
  String searchPortfolio(String ticker);

  /**
   * Gives the value of the portfolio on a certain date.
   *
   * @param date a YYYY-MM-DD formatted string representing the day to search by.
   * @return a String with the total value on the specified date.
   */
  String getPortfolioByDate(String date, double commissionRate);

  /**
   * Sells the desired amount of stocks from the portfolio.
   *
   * @param ticker the company's stock symbol.
   * @param qty    the amount of shares to sell.
   * @return a String with a confirmation that the stocks were sold, or a warning that there was an
   *     error.
   */
  String sellStocks(String ticker, int qty);

  /**
   * Shows the user a bar graph of their portfolio's performance over a time period.
   *
   * @param timeframe a String of either yearly, monthly, or weekly.
   * @param startDate a String representing the start date of the timeline.
   * @param endDate   a String representing the end date of the timeline.
   * @return a String representation of the portfolio's value over time.
   */
  String portfolioTimeline(String timeframe, String startDate, String endDate);

  /**
   * Get the current value of the portfolio.
   *
   * @return current value of a portfolio.
   */
  double getCurrentValue();

  /**
   * Converts the stocks within the portfolio into a HashMap.
   *
   * @return a HashMap containing the stocks and the quantities.
   */
  HashMap<String, BigDecimal> getListOfStocks();
}
