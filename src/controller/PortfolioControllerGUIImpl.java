package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import model.Portfolio;
import model.PortfolioModel;
import model.StockImpl;
import model.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import view.PortfolioGUIView;

/**
 * This class is an implementation of the PortfolioControllerGUI and the ActionListener interface.
 * It controls the GUI and model.
 */
public class PortfolioControllerGUIImpl implements PortfolioControllerGUI, ActionListener {

  private final PortfolioModel model;
  private final PortfolioGUIView GUIView;
  private User currentUser;
  private double currentValue;
  private String fileName;
  private String portfolioChoice;
  private int portfolioCount;
  private HashMap<String, Integer> stockWeights;
  private int maxWeight;
  private double maxQty;
  private String username;
  private String ticker;
  private String qty;
  private String date;
  private String tickerSell;
  private String qtySell;

  /**
   * Instantiates a new PortfolioControllerGUIImpl.
   *
   * @param model   the model.
   * @param guiView the GUI.
   */
  public PortfolioControllerGUIImpl(PortfolioModel model, PortfolioGUIView guiView) {
    this.model = model;
    this.GUIView = guiView;
    GUIView.setListener(this);
  }

  @Override
  public void runApp() {
    GUIView.display();
  }

  @Override
  public Boolean checkIfNew(String username) {
    File userData = new File("Users/" + username + ".xml");
    return !userData.exists();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
//    HashMap<String, Integer> stockWeights = new HashMap<>();
//    int maxWeight = 100;
//    double maxQty = 0;
//    String username = "";
//    String ticker = "";
//    String qty = "";
//    String date = "";
//    String tickerSell = "";
//    String qtySell = "";

    System.out.println("Action: " + e.getActionCommand());
    switch (e.getActionCommand()) {

      // Checks to see if user entered a new or existing username.
      case "Login Button":
        username = this.GUIView.getUsernameInputString();

        if (username.isEmpty()) {
          break;
        }

        Boolean ifNew = this.checkIfNew(username);
        this.currentUser = this.model.createNewUser(username);
        this.fileName = "Users/" + this.currentUser.getUsername() + ".xml";

        if (ifNew) {
          this.GUIView.createNewAccountMessage();
        } else {
          this.GUIView.loggingInMenu();
        }

        break;

      // Creates a new user account.
      case "Yes New Account":
        try {
          this.model.createNewUserFile(username, fileName);
        } catch (ParserConfigurationException ex) {
          throw new RuntimeException(ex);
        }
        this.GUIView.loggingInMenu();

        break;

      // Sends user back to login screen.
      case "No New Account":
        this.GUIView.reEnterUsername();

        break;

      // Creates a new portfolio attached to the user and asks for first stock.
      case "Create a portfolio":
        try {
          this.model.createNewPortfolio(this.fileName);
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
        portfolioCount = this.model.getPortfolioCount(fileName);
        this.GUIView.askForStockTicker();

        break;

      // Shows the user a list of all available portfolios.
      case "View a portfolio":
        portfolioCount = this.model.getPortfolioCount(fileName);
        this.GUIView.displayPortfolios(portfolioCount);
        break;

      // Creates a new portfolio to be used as a fund.
      case "DCA Plan":
        stockWeights = new HashMap<>();
        maxWeight = 100;

        try {
          this.model.createNewPortfolio(this.fileName);
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
        portfolioCount = this.model.getPortfolioCount(fileName);
        this.GUIView.fundBreakdown();

        break;

      // Stock Collection for DCA.
      case "Add Stock to Panel":
        // Checks if ticker and weight have been entered and if the weight is valid.
        if (this.GUIView.getStockTickerInputString().equals("Enter ticker ...")
            || String.valueOf(this.GUIView.getStockWeight()).equals("Enter weight ...")) {
          break;
        }
        if (this.GUIView.getStockWeight() > maxWeight
            || this.GUIView.getStockWeight() == 0) {
          break;
        }

        // Then continues to collect stock/weight information and add it to the hashmap.
        maxWeight = maxWeight - this.GUIView.getStockWeight();

        stockWeights.put(this.GUIView.getStockTickerInputString().toUpperCase(),
            this.GUIView.getStockWeight());
        this.GUIView.addStockToPanel();

        break;

      // Performs final check and prompts user for dollar amount to invest.
      case "Finish DCA Stock Collect":
        if (!this.GUIView.getStockTickerInputString().equals("Enter ticker ...")
            && !String.valueOf(this.GUIView.getStockWeight()).equals("Enter weight ...")
            && this.GUIView.getStockWeight() != 0) {
          maxWeight = maxWeight - this.GUIView.getStockWeight();

          stockWeights.put(this.GUIView.getStockTickerInputString().toUpperCase(),
              this.GUIView.getStockWeight());
        }
        // Will not finish unless 100% weight is reached.
        AtomicInteger max = new AtomicInteger(100);
        stockWeights.forEach((s, w) -> {
          max.addAndGet(-w);
        });
        if (max.get() > 0) {
          break;
        }
        // Get dollar amount of investment.
        this.GUIView.amountToInvest();
        break;

      // Collects dollar amount and sends to model along with the stock/weight.
      case "DCA Dollar Amount":
        int dollars = this.GUIView.getDollarAmount();
        portfolioCount = this.model.getPortfolioCount(fileName);
        this.model.portfolioDCA(stockWeights, dollars, fileName, String.valueOf(portfolioCount));
        this.showPortfolio(String.valueOf(portfolioCount));
        String newPortfolio = this.model.getPortfolio(this.currentUser);
        this.GUIView.displayCostBasisConfirmation(newPortfolio);
        break;

      // Checks if ticker entered is valid and then asks for quantity.
      case "Submit Ticker":
        ticker = this.GUIView.getStockTickerInputString().toUpperCase();
        if (this.model.validateTicker(ticker)) {
          this.GUIView.askForStockQty();
          break;
        }
        // Alert user of invalid ticker submission.
        System.out.println("Invalid Ticker: " + ticker);
        break;

      // Checks is quantity is a valid integer and then asks for date.
      case "Submit Quantity":
        qty = this.GUIView.getStockQtyInputString();
        try {
          Integer.parseInt(qty);
          this.GUIView.askForStockDate();
          break;
        } catch (NumberFormatException ignored) {
          // Alert user of invalid quantity submission.
          System.out.println("Invalid Quantity: " + qty);
          break;
        }

        // Prompts the user to add a specific date to the stock purchase.
      case "Add Custom Date":
        this.GUIView.addDate();
        break;

      // Defaults the stock purchase date to today.
      case "Add Default Date":
        this.model.addStockToPortfolio(fileName, String.valueOf(portfolioCount), ticker, qty,
            LocalDate.now().toString());
        this.GUIView.addAnotherStock();
        break;

      // Validates date and adds stock to portfolio.
      case "Submit Date":
        date = this.GUIView.getStockDateInputString();
        if (this.model.dateValidation(date)) {
          this.model.addStockToPortfolio(fileName, String.valueOf(portfolioCount), ticker, qty,
              date);
          this.GUIView.addAnotherStock();
          break;
        }
        // Alert user to invalid date.
        System.out.println("Invalid Date: " + date);
        break;

      // Restarts the stock adding procedure.
      case "Add Another Stock":
        this.GUIView.askForStockTicker();
        break;

      // Sends the user back to the main menu.
      case "Main Menu":
        this.GUIView.loggingInMenu();
        break;

      // View the selected portfolio.
      case "Select Portfolio":
        portfolioChoice = this.GUIView.getDropDownSelection().substring(11);
        portfolioCount = Integer.parseInt(portfolioChoice);
        this.showPortfolio(portfolioChoice);
        String portfolioContents = this.model.getPortfolio(this.currentUser);
        Portfolio currentPortfolio = this.showPortfolio(portfolioChoice);
        currentValue = this.model.getCurrentValue(currentPortfolio);
        this.GUIView.viewPortfolioOptions(portfolioContents);
        break;

      // Runs the user through the add stock loop.
      case "Buy Stock":
        this.GUIView.askForStockTicker();
        break;

      // Gets a list of the stocks within the current portfolio and prompts the user to select one.
      case "Sell Stock Start":
        this.GUIView.sellStockSelection(
            this.showPortfolio(String.valueOf(portfolioCount)).getListOfStocks());
        break;

      // Gets the selected stock and then prompts user to select a quantity.
      case "Select Stock to Sell":
        String selectedStock = this.GUIView.getDropDownSelection();
        maxQty = Double.parseDouble(selectedStock.split(": ")[1]);
        tickerSell = selectedStock.split(": ")[0];
        this.GUIView.askForQtySell();
        break;

      // After user selects how many to sell, it asks for a date, if any.
      case "Sell Qty Selection":
        qtySell = this.GUIView.getDropDownSelection();
        this.GUIView.askForDateSell();
        break;

      // Submit stock, qty, and date to model to sell stock.
      case "Sell Stock Finish":
        if (this.model.dateValidation(this.GUIView.getStockDateInputString())) {
          this.model.sellStockFromPortfolio(fileName, portfolioChoice, tickerSell, qtySell);
          String sellValue = this.model.totalSoldOnDate(tickerSell,
              this.GUIView.getStockDateInputString(),
              Integer.parseInt(qtySell));
          this.GUIView.confirmSold(sellValue);
          break;
        }
        // Alert user to invalid date.
        System.out.println("Invalid date: " + this.GUIView.getStockDateInputString());
        break;

      // Prompts user for a date to determine cost basis.
      case "Cost Basis":
        this.GUIView.costBasis();
        break;

      // Asks user for date and then shows holdings based on date.
      case "Cost Basis Date":
        String inputDate = this.GUIView.getStockDateInputString();
        if (this.model.dateValidation(inputDate)) {
          this.GUIView.displayCostBasis(
              this.model.costBasis(
                  this.showPortfolio(portfolioChoice),
                  date,
                  0
              )
          );
          break;
        }
        // Alert user to invalid date.
        System.out.println("Invalid Date: " + inputDate);
        break;

      // Prompts the user for a time frame.
      case "Performance Chart":
        this.GUIView.performanceChartAskForTimeframe();
        break;

      // Creates the graph and submits it to view.
      case "Submit Timeframe":
        String timeframe = this.GUIView.getDropDownSelection();
        double[] performance = new double[Integer.parseInt(timeframe)];

        int i = Integer.parseInt(timeframe) - 1;
        while (i >= 1) {
          LocalDate temp = LocalDate.now().minusMonths(i);
          performance[Math.abs(i - Integer.parseInt(timeframe) + 1)] = this.model
              .getMonthlyTotalValue(temp.getMonth(), portfolioChoice, fileName);
          i -= 1;
        }
        performance[Integer.parseInt(timeframe) - 1] = currentValue;
        this.GUIView.performanceChart(performance, timeframe);
        break;

      case "Rebalance Portfolio":
        currentPortfolio = this.showPortfolio(portfolioChoice);
        Map<String, BigDecimal> currentStocks= currentPortfolio.getListOfStocks();
        for(String ticker: currentStocks.keySet()) {
          System.out.println(ticker + "   " + currentStocks.get(ticker));
        }
        this.GUIView.getRebalanceWeightage(currentStocks.keySet());


      case "Rebalance":
        String weightageString = this.GUIView.getRebalanceWeightage();
        if(weightageString.length() > 0) {
          System.out.println(weightageString);
          currentPortfolio = this.showPortfolio(portfolioChoice);
          currentStocks = currentPortfolio.getListOfStocks();
          String[] weightages = weightageString.replaceAll(" ", "")
                  .split(",");


          int idx = 0;  //idx for weightages
          Map<String, Double> expectedWeightage = new HashMap<>();
          for(String ticker: currentStocks.keySet()) {
            expectedWeightage.put(ticker, Double.parseDouble(weightages[idx]));
            idx++;
          }

          for(String ticker: expectedWeightage.keySet()) {
            System.out.print(ticker + "  " + expectedWeightage.get(ticker));
          }

          int portfolioValue = this.model.getCurrentValue(currentPortfolio);
          System.out.println("total value: " + portfolioValue);

          Map<String, Double> currentStocksValue = new HashMap<>();
          for(String ticker: currentStocks.keySet()) {
            System.out.println("value" + this.model.getStock(ticker));
            System.out.println("quantity: " + currentStocks.get(ticker));
          }
        }



      default:
        // Do nothing.
        break;
    }
  }

  /**
   * Tells the model to create a new user portfolio to be displayed.
   *
   * @param choosePortfolio the portfolio name.
   */
  private Portfolio showPortfolio(String choosePortfolio) {
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

          stockData.push(new StockImpl(ticker, qty, pDate));
        }
      }
    } catch (XPathExpressionException
             | ParserConfigurationException
             | IOException
             | SAXException e) {
      throw new RuntimeException(e);
    }
    return this.model.createUserPortfolio(stockData, this.currentUser);
  }
}
