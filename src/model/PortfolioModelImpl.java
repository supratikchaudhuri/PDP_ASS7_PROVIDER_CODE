package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import utils.DataFetcher;

/**
 * This class implements the PortfolioModel interface and outlines the functionality for parsing and
 * manipulating data.
 */
public class PortfolioModelImpl implements PortfolioModel {

  @Override
  public String getStock(String ticker) {
    String price = DataFetcher.fetchToday(ticker);

    return "Last price update for " + ticker + " is: $" + price;
  }

  @Override
  public User createNewUser(String username) {
    return new UserImpl(username);
  }

  @Override
  public Portfolio createUserPortfolio(Deque data, User user) {
    Portfolio newPortfolio = new PortfolioImpl(data);
    user.setUserPortfolio(newPortfolio);

    return newPortfolio;
  }

  @Override
  public String getPortfolio(User user) {
    return user.viewPortfolio();
  }

  @Override
  public String searchPortfolio(String ticker, User user) {
    return user.searchPortfolio(ticker);
  }

  @Override
  public void createNewUserFile(String username, String fileName)
          throws ParserConfigurationException {
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

    //Add root elements (username)
    Document doc = docBuilder.newDocument();
    Element rootElement = doc.createElement("UserData");
    doc.appendChild(rootElement);

    Element usernameElement = doc.createElement("User");
    usernameElement.setAttribute("username", username);
    rootElement.appendChild(usernameElement);

    Element count = doc.createElement("count");
    count.setTextContent("0");
    usernameElement.appendChild(count);

    try (FileOutputStream output = new FileOutputStream(fileName)) {
      writeXml(doc, output);
    } catch (IOException | TransformerException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void createNewPortfolio(String fileName) throws IOException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try (InputStream is = new FileInputStream(fileName)) {
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(is);

      //Trim whitespaces between tags when parsing.
      this.trimWhiteSpaces(doc);

      NodeList nodeList = doc.getElementsByTagName("User");
      Node node = nodeList.item(0);

      //Number of Portfolios
      Element eElement = (Element) node;
      int portfolioCount = Integer.parseInt(
              eElement.getElementsByTagName("count").item(0).getTextContent());
      portfolioCount += 1;

      NodeList childNodes = node.getChildNodes();

      Element portfolio = doc.createElement("Portfolio" + portfolioCount);
      node.appendChild(portfolio);

      //Update Portfolio count by 1 everytime createNewPortfolio is called.
      for (int i = 0; i < childNodes.getLength(); i++) {
        Node item = childNodes.item(i);
        if (item.getNodeType() == Node.ELEMENT_NODE) {
          if ("count".equalsIgnoreCase(item.getNodeName())) {
            item.setTextContent(String.valueOf(portfolioCount));
          }
        }
      }
      try (FileOutputStream output = new FileOutputStream(fileName)) {
        writeXml(doc, output);
      }
    } catch (ParserConfigurationException
             | SAXException
             | TransformerException
             | XPathExpressionException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeXml(Document doc, OutputStream output) throws TransformerException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();

    //pretty print XML
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");

    DOMSource source = new DOMSource(doc);
    StreamResult result = new StreamResult(output);

    transformer.transform(source, result);
  }

  @Override
  public void trimWhiteSpaces(Document doc) throws XPathExpressionException {
    XPath xp = XPathFactory.newInstance().newXPath();
    NodeList nl = (NodeList) xp.evaluate("//text()[normalize-space(.)='']", doc,
            XPathConstants.NODESET);
    for (int j = 0; j < nl.getLength(); ++j) {
      Node temp = nl.item(j);
      temp.getParentNode().removeChild(temp);
    }
  }

  @Override
  public int getPortfolioCount(String fileName) {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try (InputStream is = new FileInputStream(fileName)) {
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(is);

      NodeList nodeList = doc.getElementsByTagName("User");
      Node node = nodeList.item(0);

      //Number of Portfolios
      Element eElement = (Element) node;

      // Throwing error here.
      return Integer.parseInt(eElement.getElementsByTagName("count").item(0).getTextContent());
    } catch (IOException | SAXException | ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addStockToPortfolio(String filename, String choosePortfolio, String addStockTicker,
                                  String addStockQty, String addStockDate) {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try (InputStream is = new FileInputStream(filename)) {
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(is);
      this.trimWhiteSpaces(doc);
      NodeList nodeList = doc.getElementsByTagName("User");
      Node node = nodeList.item(0);
      NodeList childNodes = node.getChildNodes();

      Node item = childNodes.item(Integer.parseInt(choosePortfolio));
      if (item.getNodeType() == Node.ELEMENT_NODE) {
        Element stock = doc.createElement("ticker" + choosePortfolio);
        stock.setTextContent(addStockTicker);
        item.appendChild(stock);

        Element qty = doc.createElement("qty" + choosePortfolio);
        qty.setTextContent(addStockQty);
        item.appendChild(qty);

        Element purchaseDate = doc.createElement("PurchaseDate" + choosePortfolio);
        purchaseDate.setTextContent(addStockDate);
        item.appendChild(purchaseDate);
      }
      try (FileOutputStream output = new FileOutputStream(filename)) {
        this.writeXml(doc, output);
      }
    } catch (TransformerException
             | IOException
             | ParserConfigurationException
             | SAXException
             | XPathExpressionException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String currentDate() {
    return java.time.LocalDate.now().toString();
  }

  @Override
  public boolean dateValidation(String addStockDate) {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    formatter.setLenient(false);
    try {
      Date date = formatter.parse(addStockDate);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }

  @Override
  public boolean checkIfStockInPortfolio(String filename, String choosePortfolio,
                                         String sellStockTicker, String sellStockQty) {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try (InputStream is = new FileInputStream(filename)) {
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(is);
      this.trimWhiteSpaces(doc);
      NodeList nodeList = doc.getElementsByTagName("User");
      Node node = nodeList.item(0);
      NodeList childNodes = node.getChildNodes();

      Node item = childNodes.item(Integer.parseInt(choosePortfolio));

      Map<String, String> stockTable = new Hashtable<String, String>();

      if (item.getNodeType() == Node.ELEMENT_NODE) {
        for (int i = 0; i < (item.getChildNodes().getLength() / 3); i++) {
          Element element = (Element) node;
          String ticker = element.getElementsByTagName("ticker" + choosePortfolio).item(i)
                  .getTextContent();
          String qty = element.getElementsByTagName("qty" + choosePortfolio).item(i)
                  .getTextContent();

          if (stockTable.containsKey(ticker)) {
            String totalQty = String.valueOf(
                    Integer.parseInt(stockTable.get(ticker)) + Integer.parseInt(qty));
            stockTable.replace(ticker, totalQty);
          } else {
            stockTable.put(ticker, qty);
          }
        }
      }

      return stockTable.containsKey(sellStockTicker)
              && Integer.parseInt(stockTable.get(sellStockTicker)) >=
              Integer.parseInt(sellStockQty);

    } catch (IOException
             | XPathExpressionException
             | ParserConfigurationException
             | SAXException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void sellStockFromPortfolio(String filename, String choosePortfolio,
                                     String sellStockTicker, String sellStockQty) {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try (InputStream is = new FileInputStream(filename)) {
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(is);

      this.trimWhiteSpaces(doc);

      NodeList nodeList = doc.getElementsByTagName("User");
      Node node = nodeList.item(0);

      NodeList childNodes = node.getChildNodes();

      Node item = childNodes.item(Integer.parseInt(choosePortfolio));
      if (item.getNodeType() == Node.ELEMENT_NODE) {
        for (int j = 0; j < (item.getChildNodes().getLength() / 3); j++) {
          Element element = (Element) node;
          Node tickerItem = element.getElementsByTagName("ticker" + choosePortfolio).item(j);
          Node qtyItem = element.getElementsByTagName("qty" + choosePortfolio).item(j);
          Node pDateItem = element.getElementsByTagName("PurchaseDate" + choosePortfolio).item(j);
          String ticker = tickerItem.getTextContent();
          String qty = qtyItem.getTextContent();

          if (sellStockTicker.equals(ticker)) {
            if (Double.parseDouble(sellStockQty) > Double.parseDouble(qty)) {
              item.removeChild(tickerItem);
              item.removeChild(qtyItem);
              item.removeChild(pDateItem);
              sellStockQty = String.valueOf(Double.parseDouble(sellStockQty)
                      - Double.parseDouble(qty));
              break;
            } else if (Double.parseDouble(sellStockQty) < Double.parseDouble(qty)) {
              String updateQty = String.valueOf(
                      Double.parseDouble(qty) - Double.parseDouble(sellStockQty));
              qtyItem.setTextContent(updateQty);
              sellStockQty = String.valueOf(0);
            } else {
              item.removeChild(tickerItem);
              item.removeChild(qtyItem);
              item.removeChild(pDateItem);
              sellStockQty = String.valueOf(0);
            }
          }
        }
      }
      try (FileOutputStream output = new FileOutputStream(filename)) {
        writeXml(doc, output);
        if (Double.parseDouble(sellStockQty) > 0) {
          sellStockFromPortfolio(filename, choosePortfolio, sellStockTicker, sellStockQty);
        }
      }
    } catch (TransformerException | XPathExpressionException | ParserConfigurationException |
             IOException | SAXException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public int getStockCount(String filename, String choosePortfolio) {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try (InputStream is = new FileInputStream(filename)) {
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(is);
      this.trimWhiteSpaces(doc);
      NodeList nodeList = doc.getElementsByTagName("User");
      Node node = nodeList.item(0);
      NodeList childNodes = node.getChildNodes();

      Node item = childNodes.item(Integer.parseInt(choosePortfolio));
      return item.getChildNodes().getLength() / 3;
    } catch (IOException | XPathExpressionException | ParserConfigurationException |
             SAXException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getDailyPerformance(Portfolio portfolio, String startDate) {
    return portfolio.portfolioTimeline("daily", startDate, "");
  }

  @Override
  public String getMonthlyPerformance(Portfolio portfolio, String startDate, String endDate) {
    return portfolio.portfolioTimeline("monthly", startDate, endDate);
  }

  @Override
  public String costBasis(Portfolio portfolio, String costBasisDate, double commissionRate) {
    return portfolio.getPortfolioByDate(costBasisDate, commissionRate);
  }

  @Override
  public void portfolioDCA(HashMap<String, Integer> stockWeights, int dollars, String filename,
                           String choosePortfolio) {
    System.out.println("dollars " + dollars);
    stockWeights.forEach((stock, weight) -> {

      BigDecimal percentage = new BigDecimal(weight).multiply(BigDecimal.valueOf(.01));
      System.out.println("percentage: " + percentage);
      BigDecimal amountToInvest = new BigDecimal(dollars).multiply(percentage);
      System.out.println("amount to invest: " + amountToInvest);
      BigDecimal quote = new BigDecimal(DataFetcher.fetchQuote(stock));
      BigDecimal qtyToBuy = amountToInvest.divide(quote, 2, RoundingMode.FLOOR);

      System.out.println(
              "Ticker: " + stock + " Weight: " + weight.toString() + " Bought: " + qtyToBuy
                      + " Quoted at: " + quote);
      addStockToPortfolio(filename, choosePortfolio, stock, String.valueOf(qtyToBuy),
              currentDate());
    });
  }

  @Override
  public boolean validateTicker(String ticker) {
    return DataFetcher.validateTicker(ticker);
  }

  @Override
  public double getMonthlyTotalValue(Month month, String choosePortfolio, String filename) {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try (InputStream is = new FileInputStream(filename)) {
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(is);
      this.trimWhiteSpaces(doc);
      NodeList nodeList = doc.getElementsByTagName("User");
      Node node = nodeList.item(0);
      NodeList childNodes = node.getChildNodes();
      Node item = childNodes.item(Integer.parseInt(choosePortfolio));

      double totalValue = 0;

      if (item.getNodeType() == Node.ELEMENT_NODE) {
        for (int j = 0; j < (item.getChildNodes().getLength() / 3); j++) {
          Element element = (Element) node;
          Node tickerItem = element.getElementsByTagName("ticker" + choosePortfolio).item(j);
          Node qtyItem = element.getElementsByTagName("qty" + choosePortfolio).item(j);
          Node pDateItem = element.getElementsByTagName("PurchaseDate" + choosePortfolio).item(j);
          String ticker = tickerItem.getTextContent();
          String qty = qtyItem.getTextContent();
          LocalDate pDate = LocalDate.parse(pDateItem.getTextContent());

          LocalDate lastDay;
          if (month.toString().equals("DECEMBER")) {
            lastDay = LocalDate.of(LocalDate.now().minusYears(1).getYear(), month,
                    1);
          } else {
            lastDay = LocalDate.of(LocalDate.now().getYear(), month, 1);
          }

          lastDay = lastDay.plusMonths(1).minusDays(1);

          while (lastDay.getDayOfWeek().toString().equalsIgnoreCase("SATURDAY")
                  || lastDay.getDayOfWeek().toString().equalsIgnoreCase("SUNDAY")) {
            lastDay = lastDay.minusDays(1);
          }

          if (pDate.isBefore(lastDay)) {
            totalValue += Double.parseDouble(DataFetcher.fetchDate(ticker, lastDay))
                    * Double.parseDouble(qty);
          }
        }
      }
      return totalValue;
    } catch (IOException | ParserConfigurationException | XPathExpressionException |
             SAXException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public double getCurrentValue(Portfolio portfolio) {
    return portfolio.getCurrentValue();
  }

  @Override
  public String totalSoldOnDate(String ticker, String date, int qty) {
    String result = DataFetcher.fetchDate(ticker, LocalDate.parse(date));
    System.out.println(result);
    NumberFormat formatter = NumberFormat.getCurrencyInstance();

    BigDecimal total = new BigDecimal(result).multiply(new BigDecimal(qty));

    return formatter.format(total);
  }

  @Override
  public Map<String, Double> rebalance(Portfolio currentPortfolio,
                                       Map<String, BigDecimal> currStocks,
                                       Map<String, Double> weights, String fileName,
                                       String choosePortfolio, LocalDate date) {

    double totalW = 0.0;
    for (String ticker : weights.keySet()) {
      totalW += weights.get(ticker);
    }

    if (Math.abs(totalW - 100.0) > 0.01) {
      throw new IllegalArgumentException("Total weights of all stocks combines should be 100%");
    }

    double portfolioValue = this.getCurrentValue(currentPortfolio);

    Map<String, Double> expectedValuesMap = new HashMap<>();

    for (String ticker : currStocks.keySet()) {
      BigDecimal price = new BigDecimal(DataFetcher.fetchDate(ticker, date));
      double value = price.multiply(currStocks.get(ticker)).doubleValue();
      double expectedValue = (weights.get(ticker) / 100.0) * portfolioValue;
      expectedValuesMap.put(ticker, expectedValue);
      double delta = value - expectedValue;
      if (delta > 0) {
        String qty = Double.toString(delta / price.doubleValue());
        this.sellStockFromPortfolio(fileName, choosePortfolio, ticker, qty);
      } else if (delta < 0) {
        String qty = Double.toString(-delta / price.doubleValue());
        this.addStockToPortfolio(fileName, choosePortfolio, ticker, qty, date.toString());
      }
    }

    return expectedValuesMap;
  }
}
