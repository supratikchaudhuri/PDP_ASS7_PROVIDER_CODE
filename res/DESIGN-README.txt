The overall design pattern of this application follows the MVC protocol. The interfaces that create
the user objects are User, Portfolio, and Stock. The Main class serves as the starting point of the
application.

-> Main
The Main class is the entry point of the program and upon startup, it initializes a new View, Model,
and Controller. The program loop is contained within the controller.go() method.

-> View
The view consists of methods that display a specific message or string to the user within the console.
The view does not contain any logic except for displaying messages. It takes all direction from the
Controller.

-> Model
The model consists of methods that are performing the heavy lifting of the application. The model holds
the logic for user creation, portfolio creation, and stock creation. It also has access to two
utility classes: DataFetcher and DataParser. These two classes are responsible for calling on the
AlphaVantage API and then subsequently parsing through the response data and sending the information
back to the model to perform the logic needed to add the stocks to a portfolio. The model is also
the access point between the application and .xml files that contain user/portfolio information.

-> Controller
The controller consists of methods that are relaying information between the view and model, based on
user input. The "flow" of the application is contained within the controller. Based on a user's
selection, the controller will request information from the model, and pass the result of that request
to the view for the user to see.

-> User
The user interface and class implementation dictates the methods available to an instance of a user.
These methods allow for the creation and reading of portfolios composed of selected stocks. A user
instance will only have one "Active" portfolio at a time. The user can choose to switch to a different
portfolio they create or read from an .xml file.

-> Portfolio
The portfolio interface and class implementation controls how the stocks within it are processed by
the model. Each portfolio contains a Linked List of stock objects. Once created, portfolios are
immutable.

-> Stock
The stock interface and class implementation is fairly simple, containing a constructor and several
getter methods for returning private field values to the portfolio. Once a stock is instantiated, it
is also immutable.