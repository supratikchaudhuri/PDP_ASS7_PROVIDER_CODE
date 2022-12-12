package model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import utils.DataFetcher;

/**
 * This class implements the Portfolio interface and provides functionality for an individual user's
 * portfolio.
 */
public class PortfolioImpl implements Portfolio {

  private Deque<StockImpl> stocks;
  private final LocalDate creationDate;
  private double commissionRate;
  private double currentValue;

  /**
   * Creates a new PortfolioImpl object.
   *
   * @param stocks the list of stocks to be contained in the portfolio.
   */
  public PortfolioImpl(Deque<StockImpl> stocks) {
    this.stocks = stocks;
    this.creationDate = LocalDate.now();
  }

  /**
   * Creates a new PortfolioImpl object.
   *
   * @param stocks         the list of stocks to be contained in the portfolio.
   * @param commissionRate the percentage that the broker charges.
   */
  public PortfolioImpl(Deque<StockImpl> stocks, double commissionRate)
          throws IllegalArgumentException {
    if (commissionRate > 1) {
      throw new IllegalArgumentException("Commission Rate too high.");
    }

    this.stocks = stocks;
    this.creationDate = LocalDate.now();
    this.commissionRate = commissionRate;
  }

  @Override
  public String getPortfolio() {
    Deque<StockImpl> placeholder = new LinkedList<>();
    StringBuilder output = new StringBuilder();
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    double total = 0;

    output.append("Current Holdings \n");
    int length = this.stocks.size();

    for (int i = 0; i < length; i++) {
      StockImpl current = stocks.pop();
      BigDecimal qty = current.getQuantity();
      String ticker = current.getTicker();
      String price = DataFetcher.fetchToday(ticker);
      BigDecimal totalValue = new BigDecimal(price).multiply(qty);
      total += totalValue.doubleValue();
      String newLine =
              "Ticker symbol: " + ticker + " Quantity: " + qty + " Value: " + formatter.format(
                      totalValue) + "\n";
      output.append(newLine);
      placeholder.add(current);
    }

    currentValue = total;
    this.stocks = placeholder;
    output.append("Total value: " + formatter.format(currentValue));
    return output.toString();
  }

  @Override
  public String searchPortfolio(String ticker) {
    StringBuilder result = new StringBuilder();
    NumberFormat formatter = NumberFormat.getCurrencyInstance();

    this.stocks.forEach(stock -> {
      if (stock.getTicker().equals(ticker)) {
        BigDecimal value = new BigDecimal(DataFetcher.fetchToday(ticker))
                .multiply(stock.getQuantity());

        String toAppend = "You currently hold " + stock.getQuantity()
                + " shares of " + stock.getTicker() + ". Valued at: "
                + formatter.format(value);

        result.append(toAppend);
      }
    });

    return result.toString();
  }

  @Override
  public Map<String, BigDecimal> getComposition(LocalDate date) {
    Map<String, BigDecimal> composition = new HashMap<>();
    for (StockImpl next : this.stocks) {
      LocalDate nextDate = LocalDate.parse(next.getDate());
      System.out.println(nextDate + " " + date);
      if (nextDate.compareTo(date) <= 0) {
        System.out.println("true");
        BigDecimal qty = next.getQuantity();
        String ticker = next.getTicker();

        LocalDate d = date;
        while (date.getDayOfWeek().toString().equalsIgnoreCase("SATURDAY")
                || date.getDayOfWeek().toString().equalsIgnoreCase("SUNDAY")) {
          date = date.minusDays(1);
        }
        if (!composition.containsKey(ticker)) {
          composition.put(ticker, qty);
        } else {
          composition.put(ticker, composition.get(ticker).add(qty));
        }
        date = d;
      }
    }
    return composition;
  }

  @Override
  public String getPortfolioByDate(String date, double commissionRate)
          throws IllegalArgumentException {
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    LocalDate dateParam = LocalDate.parse(date);

    if (dateParam.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Cannot look into the future.");
    }

    StringBuilder output = new StringBuilder();
    int totalValue = 0;

    Iterator<StockImpl> stocks = this.stocks.iterator();

    while (stocks.hasNext()) {
      StockImpl next = stocks.next();
      LocalDate nextDate = LocalDate.parse(next.getDate());
      if (nextDate.isBefore(dateParam) || nextDate.isEqual(dateParam)) {
        double qty = next.getQuantity().doubleValue();
        String ticker = next.getTicker();
        while (dateParam.getDayOfWeek().toString().equalsIgnoreCase("SATURDAY")
                || dateParam.getDayOfWeek().toString().equalsIgnoreCase("SUNDAY")) {
          dateParam = dateParam.minusDays(1);
        }

        String value = DataFetcher.fetchDate(ticker, dateParam);
        BigDecimal total = new BigDecimal(value).multiply(new BigDecimal(qty));
        totalValue = totalValue + total.intValue();

        String newLine = String.format("%s: %f shares worth %s purchased on %s \n", ticker, qty,
                formatter.format(total.intValue()), next.getDate());
        output.append(newLine);
      }
    }
    double commision = commissionRate * totalValue;
    output.append("Total value: " + formatter.format(totalValue));
    output.append("\nCommision: " + formatter.format(commision));
    output.append("\nRaw cost: " + formatter.format(totalValue + commision));

    return output.toString();
  }

  @Override
  public String sellStocks(String ticker, int qty) throws IllegalArgumentException {
    if (qty < 1) {
      return "Invalid quantity";
    }

    StringBuilder result = new StringBuilder();
    AtomicBoolean sold = new AtomicBoolean(false);
    AtomicReference<BigDecimal> total = new AtomicReference<>(new BigDecimal(0));

    stocks.forEach(stock -> {
      if (stock.getTicker().equals(ticker)) {
        if (stock.getQuantity().intValue() > qty) {
          StockImpl oldQty = stock;
          StockImpl newQty = stock.sell(qty);
          stocks.remove(oldQty);
          stocks.add(newQty);
          confirmSold(ticker, qty, result, sold, total, stock);
        } else if (stock.getQuantity().intValue() == qty) {
          stocks.remove(stock);
          confirmSold(ticker, qty, result, sold, total, stock);
        } else {
          result.append("You do not have enough shares to sell the requested amount");
          sold.set(true);
        }
      }
    });

    if (!sold.get()) {
      result.append("You do not hold that stock in your portfolio.");
    }

    return result.toString();
  }

  /**
   * Refactoring of selling confirmation.
   *
   * @param ticker the company's ticker symbol.
   * @param qty    the qty to sell.
   * @param result the StringBuilder object to append to.
   * @param sold   the boolean to break the loop.
   * @param total  the total value of stocks being sold.
   * @param stock  the current StockImpl from the stocks in this portfolio.
   */
  private void confirmSold(String ticker, int qty, StringBuilder result,
                           AtomicBoolean sold, AtomicReference<BigDecimal> total, StockImpl stock) {

    NumberFormat formatter = NumberFormat.getCurrencyInstance();

    total.set(stock.computePrice(LocalDate.now(), qty));
    BigDecimal commission = total.get().multiply(new BigDecimal(commissionRate));
    total.set(total.get().subtract(commission));

    result.append("Sold " + qty + " shares of " + ticker + " for a price of: " + formatter.format(
            total.get()));
    result.append(
            "\nCommission fee of " + (this.commissionRate * 100) + "% totaling: "
                    + formatter.format(
                    commission));

    sold.set(true);
  }

  @Override
  public String portfolioTimeline(String timeframe, String startDate, String endDate) {
    if (timeframe.equalsIgnoreCase("monthly")) {
      return monthlyTimeline(startDate, endDate);
    } else if (timeframe.equalsIgnoreCase("daily")) {
      return dailyTimeline(startDate);
    }

    return "Invalid timeframe.";
  }

  /**
   * Creates a timeline representation of a portfolio's value by month.
   *
   * @param start the start date.
   * @param end   the end date.
   * @return a text based timeline of the portfolio's value.
   */
  private String monthlyTimeline(String start, String end) {
    System.out.println("Standby, fetching data...");
    StringBuilder result = new StringBuilder();
    LocalDate currentMonth = LocalDate.parse(start);

    while (currentMonth.isBefore(LocalDate.parse(end)) || currentMonth.isEqual(
            LocalDate.parse(end))) {
      LocalDate firstOfMonth = currentMonth.withDayOfMonth(1);
      LocalDate lastOfMonth = firstOfMonth.plusMonths(1).minusDays(1);
      AtomicInteger total = new AtomicInteger();

      stocks.forEach(stock -> {
        int qty = stock.getQuantity().intValue();
        String endOfMonthValue = DataFetcher.fetchMonthly(stock.getTicker(),
                firstOfMonth.toString(), lastOfMonth.toString());
        List<String> currentVal = List.of(endOfMonthValue.split(": "));
        BigDecimal stockVal = new BigDecimal(currentVal.get(1).strip()).multiply(
                new BigDecimal(qty));
        total.addAndGet(stockVal.intValue());
      });
      int monthlyTotal = total.intValue();
      result.append(
              currentMonth.getMonth().toString().substring(0, 3) + " "
                      + currentMonth.getYear() + ": ");
      result.append("*".repeat(monthlyTotal / 1000) + "\n");
      currentMonth = currentMonth.plusMonths(1);
    }

    String startMonthAndYear =
            LocalDate.parse(start).getMonth().toString() + " " + LocalDate.parse(start).getYear();
    String endMonthAndYear =
            LocalDate.parse(end).getMonth().toString() + " " + LocalDate.parse(end).getYear();
    result.insert(0,
            "Monthly Performance of Portfolio from " + startMonthAndYear + " to "
                    + endMonthAndYear
                    + "\n");

    result.append("\nScale: * = $1,000");
    return result.toString();
  }

  /**
   * Creates a timeline representation of a portfolio's daily value over a month.
   *
   * @param startDate the start date.
   * @return a text based timeline of the portfolio's value.
   */
  private String dailyTimeline(String startDate) {
    System.out.println("Standby, fetching data...\n");
    StringBuilder result = new StringBuilder();
    LocalDate firstOfMonth = LocalDate.parse(startDate).withDayOfMonth(1);
    LocalDate endOfMonth = LocalDate.parse(startDate).withDayOfMonth(1).plusMonths(1);
    LocalDate current = firstOfMonth;

    while (current.isBefore(endOfMonth)) {
      AtomicInteger total = new AtomicInteger();

      while (current.getDayOfWeek().toString().equalsIgnoreCase("SATURDAY")
              || current.getDayOfWeek().toString().equalsIgnoreCase("SUNDAY")) {
        current = current.plusDays(1);
      }

      LocalDate finalCurrent = current;

      stocks.forEach(stock -> {
        int qty = stock.getQuantity().intValue();
        String eodValue = DataFetcher.fetchDate(stock.getTicker(), finalCurrent);
        if (!eodValue.equalsIgnoreCase("No data for specified date.")) {
          BigDecimal stockValue = new BigDecimal(eodValue).multiply(new BigDecimal(qty));
          total.addAndGet(stockValue.intValue());
        }
      });

      if (total.intValue() != 0) {
        result.append(
                current.getMonth().toString().substring(0, 3) + " "
                        + current.getDayOfMonth() + ": ");
        result.append("*".repeat(total.intValue() / 1000) + "\n");
      }

      current = current.plusDays(1);
    }

    result.insert(0,
            "Daily Performance of Portfolio from " + firstOfMonth + " to "
                    + endOfMonth.minusDays(1)
                    + "\n");
    result.append("\nScale: * = $1,000");
    return result.toString();
  }

  @Override
  public double getCurrentValue() {
    getPortfolio();
    return currentValue;
  }

  @Override
  public HashMap<String, BigDecimal> getListOfStocks() {
    HashMap<String, BigDecimal> stockList = new HashMap<>();

    stocks.forEach(stock -> {
      String ticker = stock.getTicker();

      if (stockList.containsKey(ticker)) {
        stockList.put(ticker, stockList.get(ticker).add(stock.getQuantity()));
      } else {
        stockList.put(ticker, stock.getQuantity());
      }
    });

    return stockList;
  }
}
