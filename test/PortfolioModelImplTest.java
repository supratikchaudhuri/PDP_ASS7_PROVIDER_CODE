import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import model.Portfolio;
import model.PortfolioImpl;
import model.PortfolioModel;
import model.PortfolioModelImpl;
import model.StockImpl;
import model.User;
import model.UserImpl;
import utils.DataFetcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
    BigDecimal total = new BigDecimal(DataFetcher.fetchToday(ticker))
            .multiply(new BigDecimal(qty));
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
    BigDecimal total = new BigDecimal(DataFetcher.fetchToday(ticker))
            .multiply(new BigDecimal(qty));
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

  @Test
  public void testRebalance() {
    String expected = "MSFT: 13.461372 shares worth $3,303.00 purchased on 2022-12-12 \n" +
            "MSFT: 11.000000 shares worth $2,699.00 purchased on 2022-12-12 \n" +
            "GOOG: 64.503170 shares worth $6,003.00 purchased on 2022-12-12 \n" +
            "Total value: $12,005.00\n" +
            "Commision: $0.00\n" +
            "Raw cost: $12,005.00";

    String name = "test";

    User testUser = new UserImpl(name);
    String fileName = "Users/" + name + ".xml";
    String choosePortfolio = "1";

    Portfolio portfolio = this.showPortfolio(choosePortfolio, fileName, testUser);

    Map<String, BigDecimal> currStocks = portfolio.getListOfStocks();

    Map<String, Double> weights = new HashMap<>();
    weights.put("GOOG", 50.0);
    weights.put("MSFT", 50.0);

    this.model.rebalance(portfolio, currStocks, weights, fileName, choosePortfolio,
            LocalDate.now());

    portfolio = this.showPortfolio(choosePortfolio, fileName, testUser);
    String value = portfolio.getPortfolioByDate(LocalDate.now().toString(), 0);
    assertEquals(expected, value);
  }

  @Test
  public void testRebalanceInvalid() {
    String name = "test";

    User testUser = new UserImpl(name);
    String fileName = "Users/" + name + ".xml";
    String choosePortfolio = "1";

    Portfolio portfolio = this.showPortfolio(choosePortfolio, fileName, testUser);

    Map<String, BigDecimal> currStocks = portfolio.getListOfStocks();

    Map<String, Double> weights = new HashMap<>();
    weights.put("GOOG", 50.0);
    weights.put("MSFT", 150.0);

    try {
      this.model.rebalance(portfolio, currStocks, weights, fileName, choosePortfolio,
              LocalDate.now());
      fail("Should fail for invalid weights but did not");
    } catch (IllegalArgumentException e) {
      weights.put("GOOG", 25.0);
      weights.put("MSFT", 10.0);

      try {
        this.model.rebalance(portfolio, currStocks, weights, fileName, choosePortfolio,
                LocalDate.now());
        fail("Should fail for invalid weights but did not");
      } catch (IllegalArgumentException e2) {
        // pass
      }
    }
  }

  @Test
  public void testRebalanceUnequalWeightage() {
    String expected = "VZ: 495.923422 shares worth $18,547.00 purchased on 2022-12-12 \n" +
            "AAPL: 220.671581 shares worth $31,370.00 purchased on 2022-12-12 \n" +
            "JNJ: 167.247661 shares worth $29,392.00 purchased on 2022-12-12 \n" +
            "VZ: 28.000000 shares worth $1,047.00 purchased on 2022-12-12 \n" +
            "MSFT: 39.920821 shares worth $9,797.00 purchased on 2022-12-12 \n" +
            "AAPL: 55.000000 shares worth $7,818.00 purchased on 2022-12-12 \n" +
            "Total value: $97,971.00\n" +
            "Commision: $0.00\n" +
            "Raw cost: $97,971.00";
    String name = "idk";

    User testUser = new UserImpl(name);
    String fileName = "Users/" + name + ".xml";
    String choosePortfolio = "1";

    Portfolio portfolio = this.showPortfolio(choosePortfolio, fileName, testUser);

    Map<String, BigDecimal> currStocks = portfolio.getListOfStocks();

    Map<String, Double> weights = new HashMap<>();
    weights.put("AAPL", 40.0);
    weights.put("JNJ", 30.0);
    weights.put("MSFT", 10.0);
    weights.put("VZ", 20.0);

    this.model.rebalance(portfolio, currStocks, weights, fileName, choosePortfolio,
            LocalDate.now());
    portfolio = this.showPortfolio(choosePortfolio, fileName, testUser);
    String value = portfolio.getPortfolioByDate(LocalDate.now().toString(), 0);
    assertEquals(expected, value);
  }

  private Portfolio showPortfolio(String choosePortfolio, String fileName, User currentUser) {
    Deque<StockImpl> stockData = new LinkedList<>();
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try (InputStream is = new FileInputStream(fileName)) {
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(is);

      this.model.trimWhiteSpaces(doc);

      NodeList nodeList = doc.getElementsByTagName("User");
      Node node = nodeList.item(0);

      NodeList childNodes = node.getChildNodes();

      Node item = childNodes.item(Integer.parseInt(choosePortfolio));
      if (item.getNodeType() == Node.ELEMENT_NODE) {
        for (int j = 0; j < (item.getChildNodes().getLength() / 3); j++) {
          Element element = (Element) node;
          String ticker = element.getElementsByTagName("ticker" + choosePortfolio).item(j)
                  .getTextContent();
          String qty = element.getElementsByTagName("qty" + choosePortfolio).item(j)
                  .getTextContent();
          String pDate = element.getElementsByTagName("PurchaseDate" + choosePortfolio).item(j)
                  .getTextContent();

          stockData.push(new StockImpl(ticker, Double.parseDouble(qty), pDate));
        }
      }
    } catch (XPathExpressionException
             | ParserConfigurationException
             | IOException
             | SAXException e) {
      throw new RuntimeException(e);
    }
    return this.model.createUserPortfolio(stockData, currentUser);
  }
}
