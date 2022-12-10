import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;
import model.Portfolio;
import model.PortfolioImpl;
import model.StockImpl;
import model.User;
import model.UserImpl;
import org.junit.Before;
import org.junit.Test;
import utils.DataFetcher;

/**
 * A JUnit test for the UserImpl class.
 */
public class UserImplTest {

  User user;
  String output;

  @Before
  public void setup() {
    user = new UserImpl("testDummy");
  }

  @Test
  public void testGetUserName() {
    output = user.getUsername();

    String expectedOutput = "testDummy";
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testSetAndViewPortfolio() {
    Deque<StockImpl> stocks = new LinkedList<>();
    StockImpl google = new StockImpl("GOOG", 10);
    StockImpl apple = new StockImpl("AAPL", 5);
    stocks.push(google);
    stocks.push(apple);
    Portfolio portfolio = new PortfolioImpl(stocks);
    user.setUserPortfolio(portfolio);

    String gTicker = google.getTicker();
    int gQty = google.getQuantity().intValue();
    BigDecimal gTotal = new BigDecimal(DataFetcher.fetchToday(gTicker)).multiply(
        new BigDecimal(gQty));

    String aTicker = apple.getTicker();
    int aQty = apple.getQuantity().intValue();
    BigDecimal aTotal = new BigDecimal(DataFetcher.fetchToday(aTicker)).multiply(
        new BigDecimal(aQty));

    int portfolioTotal = gTotal.intValue() + aTotal.intValue();

    output = user.viewPortfolio();

    String expectedOutput = "Current Holdings \n"
        + "Ticker symbol: " + aTicker + " Quantity: " + aQty + " Value: $" + aTotal + "\n"
        + "Ticker symbol: " + gTicker + " Quantity: " + gQty + " Value: $" + gTotal + "\n"
        + "Total value: $" + portfolioTotal;
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testSearchPortfolio() {
    Deque<StockImpl> stocks = new LinkedList<>();
    StockImpl google = new StockImpl("GOOG", 10);
    StockImpl apple = new StockImpl("AAPL", 5);
    stocks.push(google);
    stocks.push(apple);
    Portfolio portfolio = new PortfolioImpl(stocks);
    user.setUserPortfolio(portfolio);

    String gTicker = google.getTicker();
    int gQty = google.getQuantity().intValue();
    BigDecimal gTotal = new BigDecimal(DataFetcher.fetchToday(gTicker)).multiply(
        new BigDecimal(gQty));

    output = user.searchPortfolio(gTicker);

    String expectedOutput = "You currently hold " + gQty
        + " shares of " + gTicker + ". Valued at: $"
        + gTotal;
    assertEquals(expectedOutput, output);
  }
}
