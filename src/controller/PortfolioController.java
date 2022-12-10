package controller;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This interface represents the functionality provided by the Controller of the application.
 */
public interface PortfolioController {

  /**
   * The application loop that initiates all other functionality.
   */
  void runApp() throws ParserConfigurationException, IOException;

  /**
   * Listens for client input before returning it as a string.
   *
   * @return a String representation of the user's input.
   */
  String scanInput();

  /**
   * Checks if the username has already been used.
   *
   * @param username the username entered by client.
   * @return a boolean representing whether the user is new.
   */
  Boolean checkIfNew(String username);

  /**
   * Retrieves the user's portfolio from the model.
   */
  void getPortfolio();

  /**
   * Present the user with a main menu where they can select what they'd like to do.
   */
  void userOptions() throws IOException;

  /**
   * Asks the client for a specific stock they'd like more information on before asking the model to
   * retrieve the information.
   */
  void getOneStock();
}
