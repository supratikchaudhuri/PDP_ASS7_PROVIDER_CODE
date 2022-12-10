package model;

/**
 * This class implements the User interface and represents the functionality of an individual user
 * using this application.
 */
public class UserImpl implements User {

  private final String username;
  private Portfolio userPortfolio;

  /**
   * Create a new UserImpl object.
   *
   * @param username the username.
   */
  public UserImpl(String username) {
    this.username = username;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String viewPortfolio() {
    return userPortfolio.getPortfolio();
  }

  @Override
  public String searchPortfolio(String ticker) {
    return this.userPortfolio.searchPortfolio(ticker);
  }

  @Override
  public void setUserPortfolio(Portfolio portfolio) {
    this.userPortfolio = portfolio;
  }
}
