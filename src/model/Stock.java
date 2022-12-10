package model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This interface represents a Stock object that is part of a Portfolio belonging to a user.
 */
public interface Stock {

  /**
   * Fetches the latest price for the Stock object.
   *
   * @return a BigDecimal representation of the stock price.
   */
  BigDecimal fetchTodaysPrice();

  /**
   * Returns the ticker symbol of the Stock object.
   *
   * @return a String representation of the stock's ticker symbol.
   */
  String getTicker();

  /**
   * Returns the quantity of the Stock being held.
   *
   * @return the number of shares held.
   */
  BigDecimal getQuantity();

  /**
   * Return the date of purchase of the stock.
   *
   * @return a String representation of the stock's purchase date.
   */
  String getDate();

  /**
   * Computes the total value of the stock by multiplying the price on the specified date by the
   * quantity.
   *
   * @param date a String representing the date to get the stock's price.
   * @param qty  the number os stocks to compute.
   * @return a BigDecimal representing the total value of the stock on that day.
   */
  BigDecimal computePrice(LocalDate date, int qty);

  /**
   * Sells the specified number of shares of this Stock object.
   *
   * @param qty the number of shares to sell.
   * @return the Stock object with the updated quantity.
   */
  Stock sell(int qty);
}
