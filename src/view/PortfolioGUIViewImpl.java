package view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.time.Month;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * This class is an implementation of the PortfolioGUIView and offers the user a visual
 * representation of the information being sent from the controller.
 */
public class PortfolioGUIViewImpl implements PortfolioGUIView {

  private final int mainPanelHeight = 500;
  private final int buttonPanelHeight = 200;
  private final JFrame window;
  private ActionListener listener;
  private JPanel panel;
  private JPanel buttonPanel;
  private JComboBox<String> dropDownMenu;
  private JLabel message;
  private JTextField usernameTextField;
  private JTextField stockTicker;
  private JTextField stockQty;
  private JTextField stockDate;
  private JTextField stockWeight;
  private JTextField dollarAmount;
  private JTextField rebalanceWeightages;
  private JButton login;
  private int quantity;
  private String ticker;

  /**
   * Instantiates a new PortfolioGUIViewImpl object.
   */
  public PortfolioGUIViewImpl() {
    window = new JFrame("Portfolio Tracker");
    window.setSize(700, 600);
    window.setLocationRelativeTo(null);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 50));

    // Login Panel
    panel = panelCreator(mainPanelHeight);
    message = new JLabel("Welcome to your Portfolio Tracker!");
    panel.add(message);
    userLogin(panel);

    // Add to frame
    window.getContentPane().add(panel);
  }

  @Override
  public void display() {
    window.setVisible(true);
  }

  @Override
  public void setListener(ActionListener listener) {
    this.listener = listener;
    login.addActionListener(listener);
  }

  /**
   * Resets the main panel and removes from window.
   */
  private void resetPanel() {
    panel.removeAll();
    panel.setVisible(false);
    window.remove(panel);
  }

  /**
   * Creates a new a panel.
   *
   * @param height the height of the panel.
   * @return a new panel.
   */
  private JPanel panelCreator(int height) {
    JPanel newPanel = new JPanel(new GridLayout(0, 1));
    newPanel.setSize(500, height);
    return newPanel;
  }

  /**
   * Adds the login UI to a panel.
   *
   * @param panel the modified panel.
   */
  public void userLogin(JPanel panel) {
    panel.add(new JLabel("Enter your username: "));
    usernameTextField = new JTextField(10);
    panel.add(usernameTextField);
    login = new JButton("Login");
    login.setActionCommand("Login Button");
    login.addActionListener(this.listener);
    panel.add(login);
  }

  @Override
  public void reEnterUsername() {
    resetPanel();

    JPanel newPanel = panelCreator(mainPanelHeight);
    userLogin(newPanel);

    window.add(newPanel);
    panel = newPanel;
  }

  @Override
  public void createNewAccountMessage() {
    resetPanel();

    JPanel newPanel = panelCreator(mainPanelHeight);
    newPanel.add(new JLabel("<html>" + "No account was found under this username." + "<br> <br>" +
            "Would you like create a new account with this username? </html>"));

    buttonPanel = panelCreator(buttonPanelHeight);

    JButton yesButton = buttonCreator("Yes", "Yes New Account");
    buttonPanel.add(yesButton);

    JButton noButton = buttonCreator("No", "No New Account");
    buttonPanel.add(noButton);

    newPanel.add(buttonPanel);
    window.add(newPanel);
    panel = newPanel;
    display();
  }

  @Override
  public void loggingInMenu() {
    resetPanel();

    JPanel newPanel = panelCreator(mainPanelHeight);
    newPanel.add(new JLabel("<html> Hello, " + this.getUsernameInputString() + "! <br> <br>" +
            "What would you like to do today? </html>"));

    buttonPanel = panelCreator(buttonPanelHeight);

    JButton createPortfolioBtn = buttonCreator("Create a portfolio.", "Create a portfolio");
    buttonPanel.add(createPortfolioBtn);

    JButton viewPortfolioBtn = buttonCreator("View a portfolio.", "View a portfolio");
    buttonPanel.add(viewPortfolioBtn);

    JButton createFundBtn = buttonCreator("Set up investment plan.", "DCA Plan");
    buttonPanel.add(createFundBtn);

    newPanel.add(buttonPanel);
    window.add(newPanel);
    panel = newPanel;
  }

  @Override
  public String getUsernameInputString() {
    return usernameTextField.getText();
  }

  @Override
  public void askForStockTicker() {
    resetPanel();

    JPanel newPanel = panelCreator(mainPanelHeight);
    stockTicker = new JTextField(5);

    buttonPanel = panelCreator(buttonPanelHeight);
    JButton enterButton = buttonCreator("Enter", "Submit Ticker");
    buttonPanel.add(enterButton);

    newPanel.add(new JLabel("Please enter ticker: "));
    newPanel.add(stockTicker);
    newPanel.add(buttonPanel);

    window.remove(panel);
    window.add(newPanel);
    panel = newPanel;
  }

  @Override
  public String getStockTickerInputString() {
    return stockTicker.getText();
  }

  @Override
  public void askForStockQty() {
    panel.remove(buttonPanel);

    stockQty = new JTextField(4);

    buttonPanel = panelCreator(buttonPanelHeight);
    JButton enterButton = buttonCreator("Enter", "Submit Quantity");
    buttonPanel.add(enterButton);

    panel.add(new JLabel("Please enter quantity: "));
    panel.add(stockQty);
    panel.add(buttonPanel);
    display();
  }

  @Override
  public String getStockQtyInputString() {
    return stockQty.getText();
  }

  @Override
  public void askForStockDate() {
    panel.remove(buttonPanel);

    buttonPanel = panelCreator(buttonPanelHeight);

    JButton yesButton = buttonCreator("Yes", "Add Custom Date");
    buttonPanel.add(yesButton);

    JButton noButton = buttonCreator("No", "Add Default Date");
    buttonPanel.add(noButton);

    panel.add(new JLabel("Would you like to add a purchase date?"));
    panel.add(buttonPanel);
    display();
  }

  @Override
  public void addDate() {
    panel.remove(buttonPanel);

    buttonPanel = panelCreator(buttonPanelHeight);

    JButton submitButton = buttonCreator("Submit", "Submit Date");
    buttonPanel.add(submitButton);

    stockDate = new JTextField(10);

    panel.add(new JLabel("Enter date in YYYY-MM-DD format:"));
    panel.add(stockDate);
    panel.add(buttonPanel);
    display();
  }

  @Override
  public String getStockDateInputString() {
    return stockDate.getText();
  }

  @Override
  public void addAnotherStock() {
    resetPanel();

    panel = panelCreator(mainPanelHeight);

    message = new JLabel("<html>Successfully added " + getStockQtyInputString()
            + " shares of " + getStockTickerInputString().toUpperCase() + "!"
            + "<br><br>Would you like to add another stock?</html>");

    buttonPanel = panelCreator(buttonPanelHeight);

    JButton yesButton = buttonCreator("Yes", "Add Another Stock");
    buttonPanel.add(yesButton);

    JButton noButton = buttonCreator("No", "Main Menu");
    buttonPanel.add(noButton);

    panel.add(message);
    panel.add(buttonPanel);
    window.add(panel);
  }

  @Override
  public void displayPortfolios(int portfolioCount) {
    resetPanel();
    panel = panelCreator(mainPanelHeight);

    message = new JLabel("<html>You have " + portfolioCount + " portfolios.<br><br>"
            + "Please select one.</html>");

    dropDownMenu = new JComboBox<>();

    for (int i = 1; i <= portfolioCount; i++) {
      dropDownMenu.addItem("Portfolio #" + i);
    }

    buttonPanel = panelCreator(buttonPanelHeight);
    JButton selectButton = buttonCreator("Select", "Select Portfolio");
    buttonPanel.add(selectButton);

    panel.add(message);
    panel.add(dropDownMenu);
    panel.add(buttonPanel);
    window.add(panel);
  }

  @Override
  public String getDropDownSelection() {
    return dropDownMenu.getItemAt(dropDownMenu.getSelectedIndex());
  }

  @Override
  public void viewPortfolioOptions(String portfolio) {
    resetPanel();
    panel = panelCreator(mainPanelHeight);

    message = new JLabel("<html>" + portfolio.replaceAll("\n", "<br>")
            + "</html>");

    buttonPanel = panelCreator(buttonPanelHeight);

    JButton buyStockButton = buttonCreator("1. Buy stock(s)", "Buy Stock");
    buttonPanel.add(buyStockButton);

    JButton sellStockButton = buttonCreator("2. Sell stock(s)", "Sell Stock Start");
    buttonPanel.add(sellStockButton);

    JButton costBasisButton = buttonCreator("3. Cost Basis", "Cost Basis");
    buttonPanel.add(costBasisButton);

    JButton performanceChartButton = buttonCreator("4. Performance Chart", "Performance Chart");
    buttonPanel.add(performanceChartButton);

    JButton rebalanceButton = buttonCreator("5. Rebalance Portfolio", "Rebalance Portfolio");
    buttonPanel.add(rebalanceButton);

    JButton menuButton = buttonCreator("6. Return to Main Menu", "Main Menu");
    buttonPanel.add(menuButton);

    panel.add(message);
    panel.add(buttonPanel);
    window.add(panel);
  }

  @Override
  public void sellStockSelection(HashMap<String, BigDecimal> stockList) {
    resetPanel();
    panel = panelCreator(mainPanelHeight);

    message = new JLabel("Select a stock to sell:");

    dropDownMenu = new JComboBox<>();
    stockList.forEach((ticker, qty) -> {
      dropDownMenu.addItem(ticker + ": " + qty);
    });

    buttonPanel = panelCreator(buttonPanelHeight);
    JButton selectButton = buttonCreator("Select", "Select Stock to Sell");
    buttonPanel.add(selectButton);

    panel.add(message);
    panel.add(dropDownMenu);
    panel.add(buttonPanel);
    window.add(panel);
  }


  @Override
  public void askForQtySell() {
    resetPanel();
    panel = panelCreator(mainPanelHeight);

    ticker = getDropDownSelection().split(":")[0];
    BigDecimal maxQty = new BigDecimal(getDropDownSelection().split(": ")[1]);
    message = new JLabel("How many shares of " + ticker + " would you like to sell?");

    dropDownMenu = new JComboBox<>();

    for (int i = 1; i <= maxQty.doubleValue(); i++) {
      dropDownMenu.addItem(String.valueOf(i));
    }

    buttonPanel = panelCreator(buttonPanelHeight);
    JButton selectButton = buttonCreator("Select", "Sell Qty Selection");
    buttonPanel.add(selectButton);

    panel.add(message);
    panel.add(dropDownMenu);
    panel.add(buttonPanel);
    window.add(panel);
  }

  @Override
  public void askForDateSell() {
    resetPanel();
    panel = panelCreator(mainPanelHeight);

    quantity = Integer.parseInt(getDropDownSelection());
    message = new JLabel("Enter sell date in YYYY-MM-DD format: ");
    stockDate = new JTextField(10);

    buttonPanel = panelCreator(buttonPanelHeight);
    JButton submitButton = buttonCreator("Submit", "Sell Stock Finish");
    buttonPanel.add(submitButton);

    panel.add(message);
    panel.add(stockDate);
    panel.add(buttonPanel);
    window.add(panel);
  }

  @Override
  public void confirmSold(String value) {
    resetPanel();
    panel = panelCreator(mainPanelHeight);

    message = new JLabel("Successfully sold " + quantity + " shares of "
            + ticker + " for a total of " + value);

    buttonPanel = panelCreator(buttonPanelHeight);

    JButton sellAnotherButton = buttonCreator("Sell Another", "Sell Another Stock");
    buttonPanel.add(sellAnotherButton);

    JButton mainMenuButton = buttonCreator("Return to Menu", "Main Menu");
    buttonPanel.add(mainMenuButton);

    panel.add(message);
    panel.add(buttonPanel);
    window.add(panel);
  }

  @Override
  public void performanceChartAskForTimeframe() {
    resetPanel();
    panel = panelCreator(mainPanelHeight);

    message = new JLabel("Select a Time Frame:");

    String[] timeframe = {"3", "6", "9", "12"};
    dropDownMenu = new JComboBox<>(timeframe);

    buttonPanel = panelCreator(buttonPanelHeight);
    JButton submitButton = buttonCreator("Submit", "Submit Timeframe");
    buttonPanel.add(submitButton);

    panel.add(message);
    panel.add(dropDownMenu);
    panel.add(buttonPanel);
    window.add(panel);
  }

  @Override
  public void performanceChart(double[] performance, String timeframe) {
    resetPanel();
    panel = panelCreator(mainPanelHeight);

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    for (int i = 1; i <= performance.length; i++) {
      Month month = Month.of(12 - (performance.length - i));
      dataset.addValue(performance[i - 1], "Row", month.toString().substring(0, 3).toUpperCase());
    }

    JFreeChart barChart = ChartFactory.createBarChart("Performance over "
                    + timeframe + " Months",
            "Month",
            "Performance ($USD)",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false);

    ChartPanel chartPanel = new ChartPanel(barChart);
    chartPanel.setDomainZoomable(true);

    buttonPanel = panelCreator(buttonPanelHeight);
    buttonPanel.setLayout(new FlowLayout());

    JButton viewAnotherButton = buttonCreator("View Another", "Performance Chart");
    buttonPanel.add(viewAnotherButton);

    JButton menuButton = buttonCreator("Main Menu", "Main Menu");
    buttonPanel.add(menuButton);

    panel.add(chartPanel);
    panel.add(buttonPanel);
    window.add(panel);
  }

  @Override
  public void costBasis() {
    resetPanel();
    panel = panelCreator(mainPanelHeight);

    message = new JLabel("Please enter date in YYYY-MM-DD format: ");
    stockDate = new JTextField(10);

    buttonPanel = panelCreator(buttonPanelHeight);
    JButton submitDateButton = buttonCreator("Submit", "Cost Basis Date");
    buttonPanel.add(submitDateButton);

    panel.add(message);
    panel.add(stockDate);
    panel.add(buttonPanel);
    window.add(panel);
  }

  @Override
  public void displayCostBasis(String cost) {
    resetPanel();
    panel = panelCreator(mainPanelHeight);

    message = new JLabel("<html>"
            + "Cost Basis: <br>"
            + cost.replaceAll("\n", "<br>")
            + "</html>");

    buttonPanel = panelCreator(buttonPanelHeight);
    JButton mainMenuButton = buttonCreator("Main Menu", "Main Menu");
    buttonPanel.add(mainMenuButton);

    panel.add(message);
    panel.add(buttonPanel);
    window.add(panel);
  }

  @Override
  public void fundBreakdown() {
    resetPanel();
    panel = panelCreator(mainPanelHeight);

    message = new JLabel("<html>Please choose a stock and assign a % weight to it:"
            + "<br><br>**Total weight needs to equal 100%**</html>");

    stockTicker = new JTextField(5);
    stockTicker.setText("Enter ticker ...");
    textFieldFocusListenerCreator(stockTicker);

    stockWeight = new JTextField(3);
    stockWeight.setText("Enter weight ...");
    textFieldFocusListenerCreator(stockWeight);

    buttonPanel = panelCreator(buttonPanelHeight);

    JButton addMoreButton = buttonCreator("Add Another", "Add Stock to Panel");
    buttonPanel.add(addMoreButton);

    JButton submitButton = buttonCreator("Submit", "Finish DCA Stock Collect");
    buttonPanel.add(submitButton);

    panel.add(message);
    panel.add(stockTicker);
    panel.add(stockWeight);
    panel.add(buttonPanel);
    window.add(panel);
  }

  @Override
  public void addStockToPanel() {
    panel.remove(buttonPanel);

    panel.add(new JLabel("Ticker: " + this.getStockTickerInputString().toUpperCase()
            + "  Weight: " + this.getStockWeight()));

    panel.remove(stockTicker);
    panel.remove(stockWeight);

    stockTicker = new JTextField(5);
    stockTicker.setText("Enter ticker ...");
    textFieldFocusListenerCreator(stockTicker);

    stockWeight = new JTextField(3);
    stockWeight.setText("Enter weight ...");
    textFieldFocusListenerCreator(stockWeight);

    buttonPanel = panelCreator(buttonPanelHeight);

    JButton addMoreButton = buttonCreator("Add Another", "Add Stock to Panel");
    buttonPanel.add(addMoreButton);

    JButton submitButton = buttonCreator("Submit", "Finish DCA Stock Collect");
    buttonPanel.add(submitButton);

    panel.add(stockTicker);
    panel.add(stockWeight);
    panel.add(buttonPanel);
    display();
  }

  @Override
  public void amountToInvest() {
    resetPanel();
    panel = panelCreator(mainPanelHeight);

    message = new JLabel("How much would you like to invest? (in dollars $)");
    dollarAmount = new JTextField(6);

    buttonPanel = panelCreator(buttonPanelHeight);
    JButton submitButton = buttonCreator("Submit", "DCA Dollar Amount");
    buttonPanel.add(submitButton);

    panel.add(message);
    panel.add(dollarAmount);
    panel.add(buttonPanel);
    window.add(panel);
  }

  @Override
  public void displayCostBasisConfirmation(String portfolio) {
    resetPanel();
    panel = panelCreator(mainPanelHeight);

    message = new JLabel("<html>"
            + portfolio.replaceAll("\n", "<br>")
            + "</html>");

    buttonPanel = panelCreator(buttonPanelHeight);
    JButton menuButton = buttonCreator("Main Menu", "Main Menu");
    buttonPanel.add(menuButton);

    panel.add(message);
    panel.add(buttonPanel);
    window.add(panel);
  }

  @Override
  public int getStockWeight() {
    return Integer.parseInt(this.stockWeight.getText());
  }

  @Override
  public int getDollarAmount() {
    return Integer.parseInt(this.dollarAmount.getText());
  }

  @Override
  public void getRebalanceWeightage(Set<String> tickers) {
    resetPanel();
    panel = panelCreator(mainPanelHeight);

    message = new JLabel("Please enter new weightages for stocks in the same order as " +
            "they are displayed.");
    panel.add(message);

    message = new JLabel("Please enter amount separated by comma. Total must add up to 100%.");
    panel.add(message);

    message = new JLabel("Ex- if it shows: 'Stocks: MSFT, AAPL', then enter '25.5,74.4' " +
            "for 25.5% MSFT and 74.5% AAPL");
    panel.add(message);

    StringBuilder stocks = new StringBuilder(" ");
    for (String s : tickers) {
      stocks.append(", ").append(s);
    }
    message = new JLabel("Stocks: " + stocks.substring(2));
    panel.add(message);

    rebalanceWeightages = new JTextField(50);

    rebalanceWeightages.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent key) {
        rebalanceWeightages.setEditable((key.getKeyChar() >= '0' && key.getKeyChar() <= '9')
                || key.getKeyCode() == 8 || key.getKeyChar() == ',' || key.getKeyChar() == '.');
      }
    });

    panel.add(rebalanceWeightages);

    JButton rebalanceBtn = buttonCreator("Rebalance", "Rebalance");
    panel.add(rebalanceBtn);

    JButton goBackBtn = buttonCreator("Go Back", "Main Menu");
    panel.add(goBackBtn);

    window.add(panel);

  }

  @Override
  public String getRebalanceWeightage() {
    return this.rebalanceWeightages.getText();
  }

  /**
   * Creates a JButton with a label and action.
   *
   * @param label  the button label.
   * @param action the action command.
   * @return a new JButton.
   */
  private JButton buttonCreator(String label, String action) {
    JButton button = new JButton(label);
    button.addActionListener(this.listener);
    button.setActionCommand(action);
    return button;
  }

  /**
   * Adds a focus listener to a JTextField.
   *
   * @param field a JTextField.
   */
  private void textFieldFocusListenerCreator(JTextField field) {
    String text = field.getText();
    field.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (field.getText().equals(text)) {
          field.setText("");
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        if (field.getText().isEmpty()) {
          field.setText(text);
        }
      }
    });
  }
}
