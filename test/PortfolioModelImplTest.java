import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;
import javax.xml.parsers.ParserConfigurationException;
import model.Portfolio;
import model.PortfolioImpl;
import model.PortfolioModel;
import model.PortfolioModelImpl;
import model.StockImpl;
import model.User;
import model.UserImpl;
import org.junit.Before;
import org.junit.Test;
import utils.DataFetcher;

/**
 * A JUnit test for the PortfolioModelImpl Class.
 */
public class PortfolioModelImplTest {

  PortfolioModel model;
  String output;

  @Before
  public void setup() {
    model = new PortfolioModelImpl();
  }

  @Test
  public void testGetStock() {
    String ticker = "GOOG";
    String price = DataFetcher.fetchToday(ticker);

    output = model.getStock(ticker);

    String expectedOutput = "Last price update for " + ticker + " is: $" + price;
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testCreateNewUser() {
    output = model.createNewUser("testDummy").getUsername();

    String expectedOutput = "testDummy";
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testCreateAndGetUserPortfolio() {
    User testUser = new UserImpl("testDummy");
    Deque<StockImpl> stocks = new LinkedList<>();

    StockImpl google = new StockImpl("GOOG", 1);
    String ticker = "GOOG";
    int qty = 1;
    BigDecimal total = new BigDecimal(DataFetcher.fetchToday(ticker)).multiply(new BigDecimal(qty));
    stocks.push(google);

    output = model.createUserPortfolio(stocks, testUser).getPortfolio();

    String expectedOutput = "Current Holdings \n"
        + "Ticker symbol: " + ticker + " Quantity: " + qty + " Value: $" + total + "\n"
        + "Total value: $" + total.intValue();
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testSearchPortfolio() {
    User testUser = new UserImpl("testDummy");
    Deque<StockImpl> stocks = new LinkedList<>();

    StockImpl google = new StockImpl("GOOG", 1);
    String ticker = "GOOG";
    int qty = 1;
    BigDecimal total = new BigDecimal(DataFetcher.fetchToday(ticker)).multiply(new BigDecimal(qty));
    stocks.push(google);

    Portfolio portfolio = new PortfolioImpl(stocks);
    testUser.setUserPortfolio(portfolio);

    output = model.searchPortfolio(ticker, testUser);

    String expectedOutput = "You currently hold " + qty
        + " shares of " + ticker + ". Valued at: $"
        + total;
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testCreateNewUserFile() {
    String username = "testDummy";
    String filename = "Users/" + username + ".xml";
    try {
      model.createNewUserFile(username, filename);
      testGetNewUserFile();
    } catch (ParserConfigurationException e) {
      fail();
    }
  }

  @Test
  public void testGetNewUserFile() {
    File testFile = new File("Users/testDummy.xml");

    assertEquals(true, testFile.exists());
  }

  @Test
  public void testCreateNewPortfolio() {
    String filename = "Users/testDummy.xml";
    try {
      model.createNewPortfolio(filename);
    } catch (IOException e) {
      fail();
    }
  }
}
