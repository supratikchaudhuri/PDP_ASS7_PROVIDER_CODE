import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;
import model.Portfolio;
import model.PortfolioImpl;
import model.StockImpl;
import org.junit.Before;
import org.junit.Test;
import utils.DataFetcher;

/**
 * A JUnit test for the PortfolioImpl Class. The following tests must be run one at a time due to
 * the call limitations of the AlphaVantage API.
 */
public class PortfolioImplTest {

  Portfolio portfolio;
  String output;
  StockImpl google;
  StockImpl apple;

  @Before
  public void setup() {
    Deque<StockImpl> stocks = new LinkedList<>();
    google = new StockImpl("GOOG", 10);
    apple = new StockImpl("AAPL", 5);
    stocks.push(google);
    stocks.push(apple);

    portfolio = new PortfolioImpl(stocks);
  }

  @Test
  public void testGetPortfolio() {
    String gTicker = google.getTicker();
    int gQty = google.getQuantity().intValue();
    BigDecimal gTotal = new BigDecimal(DataFetcher.fetchToday(gTicker)).multiply(
        new BigDecimal(gQty));

    String aTicker = apple.getTicker();
    int aQty = apple.getQuantity().intValue();
    BigDecimal aTotal = new BigDecimal(DataFetcher.fetchToday(aTicker)).multiply(
        new BigDecimal(aQty));

    int portfolioTotal = gTotal.intValue() + aTotal.intValue();

    String expectedOutput = "Current Holdings \n"
        + "Ticker symbol: " + aTicker + " Quantity: " + aQty + " Value: $" + aTotal + "\n"
        + "Ticker symbol: " + gTicker + " Quantity: " + gQty + " Value: $" + gTotal + "\n"
        + "Total value: $" + portfolioTotal;

    output = portfolio.getPortfolio();

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testSearchPortfolio() {
    String ticker = google.getTicker();
    int qty = google.getQuantity().intValue();
    String price = DataFetcher.fetchToday(ticker);
    BigDecimal value = new BigDecimal(price).multiply(new BigDecimal(qty));

    String expectedOutput = "You currently hold " + qty
        + " shares of " + ticker + ". Valued at: $"
        + value;

    output = portfolio.searchPortfolio(ticker);

    assertEquals(expectedOutput, output);
  }
}
