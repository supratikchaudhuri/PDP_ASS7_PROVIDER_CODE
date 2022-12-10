package utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

/**
 * This Utility class is used to make API calls to the AlphaVantage API.
 */
public class DataFetcher {

  static String apiKey = "CV4O3KUIMS9TVCLR";
  static URL url = null;

  /**
   * Validates the given ticker using the SEARCH endpoint of the AlphaVantage API.
   *
   * @param ticker the company's stock symbol.
   * @return true if the ticker is valid, false if it is not.
   */
  public static boolean validateTicker(String ticker) {
    try {
      url = new URL("https://www.alphavantage"
          + ".co/query?function=SYMBOL_SEARCH"
          + "&keywords="
          + ticker
          + "&apikey="
          + apiKey
          + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("The AlphaVantage API is not working: " + e.getMessage());
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {
      in = url.openStream();
      int b;

      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No data found for " + ticker + ": " + e.getMessage());
    }

    return DataParser.parseValidation(output, ticker);
  }

  /**
   * Fetches the monthly values for a given company in a specified timeframe.
   *
   * @param ticker    the company's ticker symbol.
   * @param startDate the start date.
   * @param endDate   the end date.
   * @return a String containing the closing values for each month in the timeframe.
   */
  public static String fetchMonthly(String ticker, String startDate, String endDate) {
    try {
      url = new URL("https://www.alphavantage"
          + ".co/query?function=TIME_SERIES_MONTHLY"
          + "&symbol="
          + ticker
          + "&apikey="
          + apiKey
          + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("The AlphaVantage API is not working: " + e.getMessage());
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {
      in = url.openStream();
      int b;

      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "No price data found for " + ticker + ": " + e.getMessage());
    }

    return DataParser.parseMonthly(output, startDate, endDate);
  }

  /**
   * Fetches a list of daily price points for the provided stock for up to 20 years.
   *
   * @param ticker the company's stock symbol.
   * @return a StringBuilder object with all the data for the specified company.
   */
  private static StringBuilder fetchDaily(String ticker) {
    try {
      url = new URL("https://www.alphavantage"
          + ".co/query?function=TIME_SERIES_DAILY"
          + "&outputsize=full"
          + "&symbol="
          + ticker
          + "&apikey="
          + apiKey
          + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("The AlphaVantage API is not working: " + e.getMessage());
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {
      in = url.openStream();
      int b;

      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "No price data found for " + ticker + ": " + e.getMessage());
    }

    return output;
  }

  /**
   * Fetches the latest price data for the given stock.
   *
   * @param ticker The stock ticker to be fetched.
   * @return The price for the latest quote of the stock.
   */
  public static String fetchQuote(String ticker) {
    try {
      url = new URL("https://www.alphavantage"
          + ".co/query?function=GLOBAL_QUOTE"
          + "&symbol="
          + ticker
          + "&apikey="
          + apiKey
          + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("The AlphaVantage API is not working: " + e.getMessage());
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {
      in = url.openStream();
      int b;

      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "No price data found for " + ticker + ": " + e.getMessage());
    }

    return DataParser.parseTodaysPrice(output);
  }

  /**
   * Calls on fetchQuote to get the latest price for a stock.
   *
   * @param ticker the company's stock symbol.
   * @return the resulting String representation of the price from the fetchQuote method.
   */
  public static String fetchToday(String ticker) {
    return fetchQuote(ticker);
  }

  /**
   * Sends the results from fetchDaily to DataParser.parseChosenDate and returns the price on the
   * given date.
   *
   * @param ticker the company's stock symbol.
   * @param date   the specified date.
   * @return a String representation of the price on the chose date.
   */
  public static String fetchDate(String ticker, LocalDate date) {
    StringBuilder output = fetchDaily(ticker);
    return DataParser.parseChosenDate(output, date);
  }
}
