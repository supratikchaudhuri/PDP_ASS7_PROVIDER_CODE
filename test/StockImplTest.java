import static org.junit.Assert.assertEquals;

import model.Stock;
import model.StockImpl;
import org.junit.Before;
import org.junit.Test;
import utils.DataFetcher;

/**
 * A JUnit test for the StockImpl class.
 */
public class StockImplTest {

  Stock stock;
  String output;

  @Before
  public void setup() {
    stock = new StockImpl("GOOG", 10);
  }

  @Test
  public void testGetTicker() {
    String expectedOutput = "GOOG";
    output = stock.getTicker();

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testGetQuantity() {
    String expectedOutput = "10";
    output = String.valueOf(stock.getQuantity());

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testToString() {
    String expectedOutput = "Ticker: GOOG Quantity: 10";
    output = stock.toString();

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testFetchTodaysPrice() {
    String expectedOutput = DataFetcher.fetchToday(stock.getTicker());
    output = String.valueOf(stock.fetchTodaysPrice());

    assertEquals(expectedOutput, output);
  }
}
