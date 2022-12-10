import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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
import view.PortfolioView;
import view.PortfolioViewImpl;

/**
 * A JUnit test for the PortfolioViewImpl Class.
 */
public class PortfolioViewImplTest {

  PortfolioView view;
  OutputStream output;

  @Before
  public void startup() {
    output = new ByteArrayOutputStream();
    view = new PortfolioViewImpl(new PrintStream(output));
  }

  @Test
  public void testInitialGreeting() {
    view.initialGreeting();
    String expectedOutput = "Welcome to your Portfolio.\n"
        + "Enter username: \n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testCreateNewAccountMessage() {
    view.createNewAccountMessage();
    String expectedOutput = "No account was found under this username.\n"
        + "Would you like to create a new account using the username entered? (y/n)\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testNewAccountCreatedMessage() {
    view.newAccountCreatedMessage();
    String expectedOutput = "Account created.\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testDisplayUserOptions() {
    User testUser = new UserImpl("test");
    view.displayUserOptions(testUser);
    String expectedOutput = "Hello, " + testUser.getUsername() + "!\n"
        + "What would you like to do today?\n"
        + "Type the number for the option you would like to choose.\n"
        + "1. Create a Portfolio\n"
        + "2. View your Portfolio\n"
        + "3. Check a specific stock\n"
        + "*Enter any other characters to exit the program.*\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testAskForTickerSymbol() {
    view.askForTickerSymbol();
    String expectedOutput = "Please enter the company's ticker symbol.\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testAskForQuantity() {
    view.askForQuantity();
    String expectedOutput = "Please enter the number of shares. Whole numbers only.\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testDisplayPortfolio() {
    Deque<StockImpl> stocks = new LinkedList<>();
    StockImpl google = new StockImpl("GOOG", 10);
    stocks.push(google);
    Portfolio testPortfolio = new PortfolioImpl(stocks);

    String ticker = google.getTicker();
    int qty = google.getQuantity().intValue();
    BigDecimal totalValue = new BigDecimal(DataFetcher.fetchToday(ticker)).multiply(
        new BigDecimal(qty));
    int currentValue = totalValue.intValue();

    String holdings = testPortfolio.getPortfolio();
    view.displayPortfolio(holdings);
    String expectedOutput = "Current Holdings \n"
        + "Ticker symbol: " + ticker + " Quantity: " + qty + " Value: $" + totalValue + "\n"
        + "Total value: $" + currentValue + "\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testInvalidResponse() {
    view.invalidResponse();
    String expectedOutput = "That was an invalid response.\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testAddAnother() {
    view.addAnother();
    String expectedOutput = "Would you like to add another stock? yes/no\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testGoodBye() {
    view.goodBye();
    String expectedOutput = "Have a great day!\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testDisplayStockInfo() {
    String ticker = "GOOG";
    String price = DataFetcher.fetchToday(ticker);
    String stockInfo = "Last price update for " + ticker + "is: $" + price;
    String expectedOutput = "Last price update for " + ticker + "is: $" + price + "\n";
    view.displayStockInfo(stockInfo);

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testDisplayCustom() {
    String custom = "This is a test";
    String expectedOutput = "This is a test\n";
    view.displayCustom(custom);

    assertEquals(expectedOutput, output.toString());
  }
}
