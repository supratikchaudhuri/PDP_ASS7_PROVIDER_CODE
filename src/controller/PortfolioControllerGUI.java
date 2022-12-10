package controller;

/**
 * This interface represents the functionality between the model and the GUI.
 */
public interface PortfolioControllerGUI {

  /**
   * The GUI application loop that initiates all other functionality.
   */
  void runApp();

  /**
   * Checks if the username has already been used.
   *
   * @param username the username entered by client.
   * @return a boolean representing whether the user is new.
   */
  Boolean checkIfNew(String username);
}