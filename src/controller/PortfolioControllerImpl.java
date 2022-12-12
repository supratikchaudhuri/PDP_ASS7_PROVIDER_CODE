package controller;

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
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import model.Portfolio;
import model.PortfolioModel;
import model.StockImpl;
import model.User;
import utils.DataFetcher;
import view.PortfolioView;

/**
 * This class implements the PortfolioController interface and provides the functionality between
 * the view and model.
 */
public class PortfolioControllerImpl implements PortfolioController {

  private final PortfolioView view;
  private final PortfolioModel model;
  private final InputStream input;
  private User currentUser;
  private String fileName;

  /**
   * Creates a new PortfolioControllerImpl instance.
   *
   * @param model the model to be used.
   * @param view  the view to be used.
   * @param input the input method to be used.
   */
  public PortfolioControllerImpl(PortfolioModel model, PortfolioView view, InputStream input) {
    this.view = view;
    this.model = model;
    this.input = input;
  }

  @Override
  public void runApp() throws ParserConfigurationException, IOException {
    this.view.initialGreeting();
    String username = this.scanInput();
    Boolean ifNew = this.checkIfNew(username);
    this.currentUser = this.model.createNewUser(username);
    this.fileName = "Users/" + this.currentUser.getUsername() + ".xml";

    if (ifNew) {
      this.view.createNewAccountMessage();
      String createAccount = this.scanInput();
      if (createAccount.equals("y")) {
        this.model.createNewUserFile(username, fileName);
        this.view.newAccountCreatedMessage();
        this.userOptions();
      } else if (createAccount.equals("n")) {
        this.runApp();
      } else {
        this.view.invalidResponse();
        this.runApp();
      }
    } else {
      this.view.logIn();
      this.userOptions();
    }
  }

  @Override
  public String scanInput() {
    Scanner input = new Scanner(this.input);
    return input.nextLine();
  }

  @Override
  public void userOptions() throws IOException {
    this.view.displayUserOptions(this.currentUser);
    String choice = this.scanInput();

    switch (choice) {

      // Logic for creating a new portfolio.
      case "1": {
        this.model.createNewPortfolio(fileName);
        int portfolioCount = this.model.getPortfolioCount(fileName);
        this.view.createPortfolioMenu();
        String createPortfolioOption = this.scanInput();
        while (Integer.parseInt(createPortfolioOption) > 2
                || Integer.parseInt(createPortfolioOption) < 1) {
          this.view.invalidResponse();
          this.view.viewPortfolioOptions();
          createPortfolioOption = this.scanInput();
        }
        if (createPortfolioOption.equals("1")) {
          boolean addStock = true;
          while (addStock) {
            this.view.addStocksOptions();
            String addStockOptions = this.scanInput();
            while (Integer.parseInt(addStockOptions) > 3 || Integer.parseInt(addStockOptions) < 1) {
              this.view.invalidResponse();
              this.view.addStocksOptions();
              addStockOptions = this.scanInput();
            }
            if (addStockOptions.equals("1")) {
              this.view.addStockTicker();
              String addStockTicker = this.scanInput();
              this.view.addStockQty();
              String addStockQty = this.scanInput();

              while (Integer.parseInt(addStockQty) <= 0) {
                this.view.invalidResponse();
                this.view.addStockQty();
                addStockQty = this.scanInput();
              }

              this.view.addStockDate();
              String addStockDate = this.scanInput();

              boolean valid = this.model.dateValidation(addStockDate);
              while (!valid) {
                this.view.invalidResponse();
                this.view.addStockDate();
                addStockDate = this.scanInput();
                valid = this.model.dateValidation(addStockDate);
              }

              this.model.addStockToPortfolio(fileName, String.valueOf(portfolioCount),
                      addStockTicker, addStockQty, addStockDate);
            }
            if (addStockOptions.equals("2")) {
              this.view.addStockTicker();
              String addStockTicker = this.scanInput();
              this.view.addStockQty();
              String addStockQty = this.scanInput();

              while (Integer.parseInt(addStockQty) <= 0) {
                this.view.invalidResponse();
                this.view.addStockQty();
                addStockQty = this.scanInput();
              }

              String addStockDate = this.model.currentDate();

              this.model.addStockToPortfolio(fileName, String.valueOf(portfolioCount),
                      addStockTicker, addStockQty, addStockDate);
            }
            if (addStockOptions.equals("3")) {
              this.userOptions();
            }
            this.view.addAnother();
            String addAnother = this.scanInput();
            if (addAnother.equals("no")) {
              addStock = false;
            }
          }
          this.userOptions();
        }
        if (createPortfolioOption.equals("2")) {
          this.userOptions();
        }
        break;
      }

      // View a portfolio.
      case "2": {
        int portfolioCount = this.model.getPortfolioCount(fileName);
        this.view.viewPortfolio(portfolioCount);
        String choosePortfolio = this.scanInput();
        while (Integer.parseInt(choosePortfolio) > portfolioCount) {
          this.view.invalidResponse();
          this.view.displayCustom("\n");
          this.view.viewPortfolio(portfolioCount);
          choosePortfolio = this.scanInput();
        }
        Portfolio currentPortfolio = this.showPortfolio(choosePortfolio);
        this.getPortfolio();
        this.view.displayCustom("\n");
        this.view.viewPortfolioOptions();
        String viewPortfolioOption = this.scanInput();
        while (Integer.parseInt(viewPortfolioOption) > 7
                || Integer.parseInt(viewPortfolioOption) < 1) {
          this.view.invalidResponse();
          this.view.viewPortfolioOptions();
          viewPortfolioOption = this.scanInput();
        }
        if (viewPortfolioOption.equals("1")) {
          boolean addStock = true;
          while (addStock) {
            this.view.addStocksOptions();
            String addStockOptions = this.scanInput();
            while (Integer.parseInt(addStockOptions) > 3 || Integer.parseInt(addStockOptions) < 1) {
              this.view.invalidResponse();
              this.view.addStocksOptions();
              addStockOptions = this.scanInput();
            }
            if (addStockOptions.equals("1")) {
              this.view.addStockTicker();
              String addStockTicker = this.scanInput();
              this.view.addStockQty();
              String addStockQty = this.scanInput();

              while (Integer.parseInt(addStockQty) <= 0) {
                this.view.invalidResponse();
                this.view.addStockQty();
                addStockQty = this.scanInput();
              }

              this.view.addStockDate();
              String addStockDate = this.scanInput();

              boolean valid = this.model.dateValidation(addStockDate);
              while (!valid) {
                this.view.invalidResponse();
                this.view.addStockDate();
                addStockDate = this.scanInput();
                valid = this.model.dateValidation(addStockDate);
              }

              this.model.addStockToPortfolio(fileName, choosePortfolio, addStockTicker, addStockQty,
                      addStockDate);
            }
            if (addStockOptions.equals("2")) {
              this.view.addStockTicker();
              String addStockTicker = this.scanInput();
              this.view.addStockQty();
              String addStockQty = this.scanInput();

              while (Integer.parseInt(addStockQty) <= 0) {
                this.view.invalidResponse();
                this.view.addStockQty();
                addStockQty = this.scanInput();
              }

              String addStockDate = this.model.currentDate();

              this.model.addStockToPortfolio(fileName, choosePortfolio, addStockTicker, addStockQty,
                      addStockDate);
            }
            if (addStockOptions.equals("3")) {
              this.userOptions();
            }
            this.view.addAnother();
            String addAnother = this.scanInput();
            if (addAnother.equals("no")) {
              addStock = false;
            }
          }
          this.userOptions();
        }
        if (viewPortfolioOption.equals("2")) {
          if (this.model.getStockCount(fileName, choosePortfolio) == 0) {
            this.view.emptyPortfolio();
            this.view.displayCustom("\n");
            this.userOptions();
          }
          boolean sellStock = true;
          while (sellStock) {
            this.view.sellStockTicker();
            String sellStockTicker = this.scanInput();
            this.view.sellStockQty();
            String sellStockQty = this.scanInput();
            while (Integer.parseInt(sellStockQty) <= 0) {
              this.view.invalidResponse();
              this.view.sellStockQty();
              sellStockQty = this.scanInput();
            }
            boolean valid = this.model.checkIfStockInPortfolio(fileName, choosePortfolio,
                    sellStockTicker, sellStockQty);
            while (!valid) {
              this.view.invalidResponse();
              this.view.sellStockTicker();
              sellStockTicker = this.scanInput();
              this.view.sellStockQty();
              sellStockQty = this.scanInput();
              valid = this.model.checkIfStockInPortfolio(fileName, choosePortfolio, sellStockTicker,
                      sellStockQty);
            }

            this.view.displayCustom("Would you like to add a sell date? (y/n)");
            String sellDateOption = this.scanInput();
            String sellDate = "";
            String price = "";
            if (sellDateOption.equals("y")) {
              this.view.displayCustom("Enter the sell date in following format yyyy-mm-dd: ");
              sellDate = this.scanInput();
              boolean validSellDate = this.model.dateValidation(sellDate);
              while (!validSellDate) {
                this.view.invalidResponse();
                this.view.displayCustom("Enter the sell date in following format yyyy-mm-dd: ");
                sellDate = this.scanInput();
                validSellDate = this.model.dateValidation(sellDate);
              }

              price = DataFetcher.fetchDate(sellStockTicker, LocalDate.parse(sellDate));
            } else if (sellDateOption.equals("n")) {
              price = DataFetcher.fetchToday(sellStockTicker);
            }

            this.view.displayCustom("Enter the commission rate: ");
            double commissionRate = Double.parseDouble(this.scanInput());

            this.model.sellStockFromPortfolio(fileName, choosePortfolio, sellStockTicker,
                    sellStockQty);

            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            BigDecimal total = new BigDecimal(price).multiply(new BigDecimal(sellStockQty));
            double commission = total.doubleValue() * commissionRate;

            this.view.displayCustom("Total gain: " + formatter.format(total));
            this.view.displayCustom("Commission: " + formatter.format(commission));
            this.view.displayCustom("After commission: " + formatter.format(
                    total.subtract(BigDecimal.valueOf(commission))));
            this.view.soldStocks(sellStockTicker, sellStockQty);
            int stockCount = this.model.getStockCount(fileName, choosePortfolio);
            if (stockCount > 0) {
              this.view.sellAnother();
              String sellAnother = this.scanInput();
              if (sellAnother.equals("no")) {
                sellStock = false;
              }
            } else {
              this.view.emptyPortfolio();
              this.view.displayCustom("\n");
              break;
            }
          }
          this.userOptions();
        }

        //Daily performance
        if (viewPortfolioOption.equals("3")) {
          this.view.displayCustom("Type in the month in YYYY-MM-DD format.");
          String date = this.scanInput();

          boolean valid = this.model.dateValidation(date);
          while (!valid) {
            this.view.invalidResponse();
            this.view.addStockDate();
            date = this.scanInput();
            valid = this.model.dateValidation(date);
          }

          this.view.displayCustom(this.model.getDailyPerformance(currentPortfolio, date));
          this.userOptions();
        }

        //Monthly performance
        if (viewPortfolioOption.equals("4")) {
          this.view.displayCustom("Type in the start date in YYYY-MM-DD format.");
          String startDate = this.scanInput();

          boolean valid = this.model.dateValidation(startDate);
          while (!valid) {
            this.view.invalidResponse();
            this.view.addStockDate();
            startDate = this.scanInput();
            valid = this.model.dateValidation(startDate);
          }

          this.view.displayCustom("Type in the end date in YYYY-MM-DD format.");
          String endDate = this.scanInput();

          valid = this.model.dateValidation(endDate);
          while (!valid) {
            this.view.invalidResponse();
            this.view.addStockDate();
            endDate = this.scanInput();
            valid = this.model.dateValidation(endDate);
          }

          this.view.displayCustom(
                  this.model.getMonthlyPerformance(currentPortfolio, startDate, endDate));
          this.userOptions();
        }

        //Cost Basis
        if (viewPortfolioOption.equals("5")) {
          this.view.costBasisDate();
          String costBasisDate = this.scanInput();

          boolean valid = this.model.dateValidation(costBasisDate);
          while (!valid) {
            this.view.invalidResponse();
            this.view.addStockDate();
            costBasisDate = this.scanInput();
            valid = this.model.dateValidation(costBasisDate);
            this.userOptions();
          }

          this.view.displayCustom("Enter the commission rate: ");
          double commissionRate = Double.parseDouble(this.scanInput());

          this.view.displayCustom(
                  this.model.costBasis(currentPortfolio, costBasisDate, commissionRate));
        }

        // re-balance
        if (viewPortfolioOption.equals("6")) {

          // boolean isValidDate;
          LocalDate date;
           // do {
           // isValidDate = true;
           // try {
              // this.view.displayCustom("Date for re-balance (YYYY-MM-DD) : ");
              // date = LocalDate.parse(scanInput());
              // if(date.compareTo(LocalDate.now()) > 0) {
                // isValidDate = false;
                // this.view.displayCustom("\nCannot re-balance in future\n");
              // }
            // } catch(DateTimeParseException e) {
              // this.view.displayCustom("\nInvalid date format\n");
            // }
          // } while(!isValidDate);
          date = LocalDate.now();

          Map<String, BigDecimal> currStocks = currentPortfolio.getListOfStocks();
          // Map<String, BigDecimal> currStocks = currentPortfolio.getComposition(date);
          Map<String, Double> weights;
          boolean is100;
          do {
            is100 = true;
            weights = new HashMap<>();
            double totalW = 0.0;
            this.view.displayCustom("Stocks in this portfolio = " + currStocks.keySet());
            for (String ticker : currStocks.keySet()) {
              this.view.displayCustom("Enter weightage for " + ticker + " : ");
              double weightage = Double.parseDouble(scanInput());
              while (weightage <= 0.0) {
                this.view.displayCustom("\nWeightage should be > 0\n");
                this.view.displayCustom("\nEnter weightage for " + ticker + " : ");
                weightage = Double.parseDouble(scanInput());
              }
              totalW += weightage;
              weights.put(ticker, weightage);
            }
            if (totalW != 100.0) {
              this.view.displayCustom("\nTotal weightage has to be 100%. Enter again\n");
              is100 = false;
            }
          }
          while (!is100);
          while (date.getDayOfWeek().toString().equalsIgnoreCase("SATURDAY")
                  || date.getDayOfWeek().toString().equalsIgnoreCase("SUNDAY")) {
            date = date.minusDays(1);
          }
          this.model.rebalance(currentPortfolio, currStocks, weights,
                  fileName, choosePortfolio, date);
          currentPortfolio = this.showPortfolio(choosePortfolio);
          String ans = currentPortfolio.getPortfolioByDate(date.toString(), 0);
          this.view.displayCustom(ans);
        }

        if (viewPortfolioOption.equals("7")) {
          this.userOptions();
        }
        break;
      }

      case "3": {
        this.getOneStock();
        this.view.displayCustom("\n");
        this.userOptions();
        break;
      }

      default: {
        this.view.displayCustom("\n");
        this.view.goodBye();
        break;
      }
    }
  }

  @Override
  public void getPortfolio() {
    String portfolio = this.model.getPortfolio(this.currentUser);
    this.view.displayPortfolio(portfolio);
  }

  @Override
  public Boolean checkIfNew(String username) {
    File userData = new File("Users/" + username + ".xml");
    return !userData.exists();
  }

  @Override
  public void getOneStock() {
    this.view.askForTickerSymbol();
    String ticker = this.scanInput();
    String stockInfo = this.model.getStock(ticker);
    this.view.displayStockInfo(stockInfo);
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

          stockData.push(new StockImpl(ticker, Double.parseDouble(qty), pDate));
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
