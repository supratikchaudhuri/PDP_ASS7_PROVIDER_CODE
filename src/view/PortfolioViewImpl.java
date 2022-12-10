package view;

import java.io.PrintStream;
import model.User;

/**
 * This class implements the PortfolioView interface and outlines what messages and information are
 * being displayed to the user at all times.
 */
public class PortfolioViewImpl implements PortfolioView {

  private final PrintStream output;

  /**
   * Creates a new PortfolioViewImpl.
   *
   * @param output the desired output.
   */
  public PortfolioViewImpl(PrintStream output) {
    this.output = output;
  }

  @Override
  public void initialGreeting() {
    this.output.println("Welcome to your Portfolio.");
    this.output.println("Enter username: ");
  }

  @Override
  public void createNewAccountMessage() {
    this.output.println("No account was found under this username.");
    this.output.println("Would you like to create a new account using the username entered? (y/n)");
  }

  @Override
  public void newAccountCreatedMessage() {
    this.output.println("Account created.");
  }

  @Override
  public void displayUserOptions(User user) {
    this.output.println("Hello, " + user.getUsername() + "!");
    this.output.println("What would you like to do today?");
    this.output.println("Type the number for the option you would like to choose.");
    this.output.println("1. Create a Portfolio");
    this.output.println("2. View your Portfolio");
    this.output.println("3. Check a specific stock");
    this.output.println("*Enter any other characters to exit the program.*");
  }

  @Override
  public void askForTickerSymbol() {
    this.output.println("Please enter the company's ticker symbol.");
  }

  @Override
  public void askForQuantity() {
    this.output.println("Please enter the number of shares. Whole numbers only.");
  }

  @Override
  public void displayPortfolio(String portfolio) {
    this.output.println(portfolio);
  }

  @Override
  public void invalidResponse() {
    this.output.println("That was an invalid response.");
  }

  @Override
  public void addAnother() {
    this.output.println("Would you like to add another stock? yes/no");
  }

  @Override
  public void goodBye() {
    this.output.println("Have a great day!");
  }

  @Override
  public void displayStockInfo(String info) {
    this.output.println(info);
  }

  @Override
  public void displayCustom(String str) {
    this.output.println(str);
  }

  @Override
  public void logIn() {
    this.output.println("Logging you in...");
  }

  @Override
  public void viewPortfolio(int count) {
    this.output.println("Select the portfolio you want to view.");
    for (int i = 1; i <= count; i++) {
      this.output.println(i + ". " + "Portfolio" + i);
    }
  }

  @Override
  public void viewPortfolioOptions() {
    this.output.println("Would you like to: ");
    this.output.println("1. Add stocks to this portfolio.");
    this.output.println("2. Sell stocks from this portfolio.");
    this.output.println("3. View the daily performance.");
    this.output.println("4. View the monthly performance. ");
    this.output.println("5. View the cost basis by a specific date.");
    this.output.println("6. Return back to the main menu");
  }

  @Override
  public void addStocksOptions() {
    this.output.println("Would you like to: ");
    this.output.println("1. Purchase stocks on a specified date.");
    this.output.println("2. Purchase today's stocks.");
    this.output.println("3. Return back to the main menu.");
  }

  @Override
  public void addStockTicker() {
    this.output.println("Enter the stock ticker: ");
  }

  @Override
  public void addStockQty() {
    this.output.println("Enter the quantity: ");
  }

  @Override
  public void addStockDate() {
    this.output.println("Enter the purchase date in yyyy-mm-dd: ");
  }

  @Override
  public void sellStockTicker() {
    this.output.println("Enter the stock ticker you wish to sell: ");
  }

  @Override
  public void sellStockQty() {
    this.output.println("Enter the quantity you wish to sell: ");
  }

  @Override
  public void soldStocks(String sellStockTicker, String sellStockQty) {
    this.output.println(
        "You have successfully sold " + sellStockQty + " shares of " + sellStockTicker);
  }

  @Override
  public void sellAnother() {
    this.output.println("Would you like to sell another stock? yes/no");
  }

  @Override
  public void emptyPortfolio() {
    this.output.println("There are no more stocks in this portfolio.");
  }

  @Override
  public void costBasisDate() {
    this.output.println("Enter the date in yyyy-mm-dd: ");
  }

  @Override
  public void createPortfolioMenu() {
    this.output.println("Would you like to: ");
    this.output.println("1. Add stocks to this portfolio.");
    this.output.println("2. Return back to the main menu");
  }
}
