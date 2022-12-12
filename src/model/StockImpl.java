package model;

import java.math.BigDecimal;
import java.time.LocalDate;

import utils.DataFetcher;

/**
 * This class implements the Stock interface and represents a single stock in a Portfolio.
 */
public class StockImpl implements Stock {

  private final String ticker;
  private final BigDecimal quantity;
  private final String purchaseDate;

  /**
   * Creates a new StockImpl object.
   *
   * @param ticker   the stock's ticker symbol.
   * @param quantity the number of stocks purchased.
   * @throws IllegalArgumentException if there is a quantity of 0.
   */
  public StockImpl(String ticker, int quantity) throws IllegalArgumentException {
    if (quantity < 1) {
      throw new IllegalArgumentException(
              "Invalid quantity. You cannot purchase 0 or negative shares.");
    }

    this.ticker = ticker;
    this.quantity = new BigDecimal(quantity);
    this.purchaseDate = LocalDate.now().toString();
  }

  /**
   * Creates a new StockImpl object with a specific purchase date.
   *
   * @param ticker       the stock's ticker symbol.
   * @param quantity     the number of stocks purchased.
   * @param purchaseDate the date of purchase
   * @throws IllegalArgumentException if quantity is 0, or date is in the future.
   */
  public StockImpl(String ticker, double quantity, String purchaseDate)
          throws IllegalArgumentException {
    if (LocalDate.parse(purchaseDate).isAfter(LocalDate.now())) {
      throw new IllegalArgumentException(
              "Invalid date. Your purchase date cannot be in the future.");
    } else if (quantity < 1) {
      throw new IllegalArgumentException(
              "Invalid quantity. You cannot purchase 0 or negative shares.");
    }

    this.ticker = ticker;
    this.quantity = new BigDecimal(quantity);
    this.purchaseDate = purchaseDate;
  }

  /**
   * Creates a new StockImpl object with a specific purchase date.
   *
   * @param ticker       the stock's ticker symbol.
   * @param quantity     the number of stocks purchased.
   * @param purchaseDate the date of purchase
   * @throws IllegalArgumentException if quantity is 0, or date is in the future.
   */
  public StockImpl(String ticker, String quantity, String purchaseDate)
          throws IllegalArgumentException {
    if (LocalDate.parse(purchaseDate).isAfter(LocalDate.now())) {
      throw new IllegalArgumentException(
              "Invalid date. Your purchase date cannot be in the future.");
    } else if (Double.parseDouble(quantity) < 1) {
      throw new IllegalArgumentException(
              "Invalid quantity. You cannot purchase 0 or negative shares.");
    }

    this.ticker = ticker;
    this.quantity = new BigDecimal(quantity);
    this.purchaseDate = purchaseDate;
  }

  @Override
  public String getTicker() {
    return this.ticker;
  }

  @Override
  public BigDecimal getQuantity() {
    return this.quantity;
  }

  @Override
  public String getDate() {
    return this.purchaseDate;
  }

  @Override
  public String toString() {
    return "Ticker: " + this.ticker + " Quantity: " + this.quantity;
  }

  @Override
  public BigDecimal fetchTodaysPrice() {
    String value = DataFetcher.fetchToday(this.ticker);

    return new BigDecimal(value);
  }

  @Override
  public BigDecimal computePrice(LocalDate date, int qty) {
    if (date.isEqual(LocalDate.now())) {
      date = date.minusDays(1);
    }

    String value = DataFetcher.fetchDate(getTicker(), date);
    BigDecimal total = new BigDecimal(value).multiply(new BigDecimal(qty));
    return total;
  }

  @Override
  public StockImpl sell(int qty) {
    int currentQty = getQuantity().intValue();
    int updatedQty = currentQty - qty;

    return new StockImpl(getTicker(), updatedQty, getDate());
  }
}
