package utils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This utility class is used to parse through the stock data being fetched from the AlphaVantage
 * API.
 */
public class DataParser {

  /**
   * Formats the given StringBuilder into an iterable list.
   *
   * @param input a StringBuilder object.
   * @return a List containing all the data from the input.
   */
  private static List<String> inputFormatter(StringBuilder input) {
    Stream<String> lines = input.toString().lines();
    return lines.collect(Collectors.toList());
  }

  /**
   * Parses today's price from the input.
   *
   * @param input a StringBuilder object.
   * @return the closing price.
   */
  public static String parseTodaysPrice(StringBuilder input) {
    List<String> list = inputFormatter(input);

    String latestEntry = list.get(1);
    List<String> splitEntry = List.of(latestEntry.split(","));

    return splitEntry.get(4);
  }

  /**
   * Parses through the input to return the value on the specified date.
   *
   * @param input a StringBuilder object.
   * @param date  the date to be searched for.
   * @return the value at the chosen date.
   */
  public static String parseChosenDate(StringBuilder input, LocalDate date) {
    List<String> list = inputFormatter(input);
    list.remove(0);

    for (String current : list) {
      List<String> currentSplit = List.of(current.split(","));

      if (currentSplit.get(0).equals(date.toString()) || currentSplit.get(0).compareTo(date.toString()) <= 0) {
        return currentSplit.get(4).toString();
      }
    }

    return "No data for specified date.";
  }

  /**
   * Validates that the ticker is within the input.
   *
   * @param input  a StringBuilder object.
   * @param ticker the company's ticker symbol.
   * @return a boolean representing whether the ticker is in the input.
   */
  public static boolean parseValidation(StringBuilder input, String ticker) {
    List<String> list = inputFormatter(input);

    if (list.size() <= 1) {
      return false;
    }

    String firstEntry = list.get(1);
    List<String> splitEntry = List.of(firstEntry.split(","));
    String validatedTicker = splitEntry.get(0);

    return ticker.equals(validatedTicker);
  }

  /**
   * Parses the input to return the monthly values of the stock over a time period.
   *
   * @param input     a StringBuilder object.
   * @param startDate the start date.
   * @param endDate   the end date.
   * @return a String with all the values over the time period.
   */
  public static String parseMonthly(StringBuilder input, String startDate, String endDate) {
    List<String> list = inputFormatter(input);

    if (list.size() <= 1) {
      return "No available data.";
    }

    list.remove(0);
    StringBuilder output = new StringBuilder();
    LocalDate start = LocalDate.parse(startDate);
    LocalDate end = LocalDate.parse(endDate);

    for (String current : list) {
      List<String> currentSplit = List.of(current.split(","));
      LocalDate currentDate = LocalDate.parse(currentSplit.get(0));

      if (!currentDate.isAfter(end) && !currentDate.isBefore(start)) {
        output.append(currentSplit.get(0) + ": ");
        output.append(currentSplit.get(4) + "\n");
      }
    }
    return output.toString();
  }

  /**
   * Parses the input to return the daily values of the stock over a time period.
   *
   * @param input     a StringBuilder object.
   * @param startDate the start date.
   * @param endDate   the end date.
   * @return a String with all the values over the time period.
   */
  public static String parseDaily(StringBuilder input, String startDate, String endDate) {
    List<String> list = inputFormatter(input);

    if (list.size() <= 1) {
      return "No available data.";
    }

    list.remove(0);
    StringBuilder output = new StringBuilder();
    LocalDate start = LocalDate.parse(startDate);
    LocalDate end = LocalDate.parse(endDate);

    for (String current : list) {
      List<String> currentSplit = List.of(current.split(","));
      LocalDate currentDate = LocalDate.parse(currentSplit.get(0));

      if (!currentDate.isAfter(end) && !currentDate.isBefore(start)) {
        output.append(currentSplit.get(0) + ": ");
        output.append(currentSplit.get(4) + "\n");
      }
    }

    return output.toString();
  }
}
