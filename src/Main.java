import controller.PortfolioController;
import controller.PortfolioControllerGUI;
import controller.PortfolioControllerGUIImpl;
import controller.PortfolioControllerImpl;
import java.io.IOException;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import model.PortfolioModel;
import model.PortfolioModelImpl;
import view.PortfolioGUIView;
import view.PortfolioGUIViewImpl;
import view.PortfolioView;
import view.PortfolioViewImpl;

/**
 * The access point to all the application's functionality.
 */
public class Main {

  /**
   * The default operation for this program.
   *
   * @param args the arguments.
   * @throws ParserConfigurationException if file is not found.
   * @throws IOException                  if file is not found.
   */
  public static void main(String[] args) throws ParserConfigurationException, IOException {
    PortfolioModel model = new PortfolioModelImpl();

    System.out.println("Type '1' for Text-Based Interface");
    System.out.println("Type '2' for GUI-Based Interface");
    Scanner input = new Scanner(System.in);
    String choice = input.nextLine();

    if (choice.equals("1")) {
      PortfolioView view = new PortfolioViewImpl(System.out);
      PortfolioController controller = new PortfolioControllerImpl(model, view, System.in);
      controller.runApp();
    } else if (choice.equals("2")) {
      PortfolioGUIView guiView = new PortfolioGUIViewImpl();
      PortfolioControllerGUI controllerGUI = new PortfolioControllerGUIImpl(model, guiView);
      controllerGUI.runApp();
    }
  }
}
