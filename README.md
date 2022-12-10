[//]: # (AlphaVantage API KEY: 2A8JJZOR8HOXNN33)
<!-- PROJECT LOGO -->
<br />
<a name="readme-top"></a>
<div align="center">
    <h3 align="center">Assignment #4: Stock Portfolio</h3>
</div>


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->

## About The Project

Many people are learning to invest in the stock market. Having the ability to mimic the actions of "
buying" and "selling" shares of a company
can prove a valuable lesson to investors of all levels. Users will be able to create and keep track
of various portfolios made
of the stocks of their choosing. They will also have the ability to load a pre-made file containing
their portfolio into the program.
<br>

#### Additional Resources:

[Class Diagram](res/classDiagram.png);
[Class Documentation](res/JavaDocs.zip);
[Design Choice](res/DESIGN-README.txt);
[Setup Instructions](res/SETUP-README.txt);


<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

[Java SE 11][Docs.oracle.com]
<br>
[AlphaVantage API](https://www.alphavantage.co/documentation/)


<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->

## Getting Started

1. Fork and clone this repo.
2. Run the `.jar` file.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->

## Usage

This application is intended to create a text-based interface which a user can create a portfolio
from 1 or more stocks.
<br>
The prices associated with each stock are fetched through
the [AlphaVantage API](https://www.alphavantage.co/documentation/).  
The prices represent the most recent closing cost of the stock. For example, if creating a portfolio
on a Monday, the price quotes will be from
Friday's closing price. The same applies for holidays.

#### Current Features:

- A User is able to create a new profile and load an existing profile from a `.xml` file.
- A User is able to create a portfolio made of 1+ stocks. Stocks are saved using the ticker symbol
  and the quantity held.
- A User is able to get a price for a specific stock.
- A User is able to see the total value of their portfolio.
- A User is able to save their portfolio into a `.xml` file.
  <br>

#### Current Limitations:

- Certain stocks cannot be fetched from the API

> NKLA
<br>

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- Importing User Files -->

#### Importing User Files:

This program allows user to load in an user data by placing the file `<Insert Username>.xml` into
the `Users` folder.
User data file should follow the format:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<UserData>
  <User username="kevins">
    <count>2</count>
    <Portfolio1>
      <ticker1>GOOG</ticker1>
      <qty1>2</qty1>
      <ticker1>AAPL</ticker1>
      <qty1>3</qty1>
      <ticker1>AMZN</ticker1>
      <qty1>4</qty1>
      <ticker1>TSLA</ticker1>
      <qty1>5</qty1>
    </Portfolio1>
    <Portfolio2>
      <ticker2>GOOG</ticker2>
      <qty2>2</qty2>
    </Portfolio2>
  </User>
</UserData>

```

`<count>` : Number of portfolios.

`<PortfolioX>` : X is the index of portfolios. This element should contain stock tickers and
quatities info.

`<tickerX>` : X here should be the same to the index of the portfolio it is in. This element should
have a text content of stock tickers.

`<qty>` : X here should be the same to the index of the portfolio it is in. This element should have
a text content of number of shares of the stock tickers above it.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Updates

#### Assignment 5:

DataFetcher & DataParser

1. Improved the `fetchToday` method to pull less data from the API using a different endpoint. It
   now
   calls on `fetchQuote` so that other classes would not need to be modified.
2. `fetchDate` method calls on `fetchDaily` to get data points for up to 20 years and can parse
   through to find the specified date.
3. `validateTicker` method will give the model a way to check if the input being provided is a
   valid ticker symbol.
4. `fetchMonthly` returns a list of the months with their respective closing prices if any data
   was returned from the API call.

Stock & StockImpl

1. Added the `getDate` method to the `Stock` interface.
2. `StockImpl` now has two constructors: One that assigns a default value of today as the date
   of purchase, and another that allows the client to specify a purchase date.
3. Validation of date and quantity in `StockImpl` constructors.

Portfolio & PortfolioImpl

1. `PortfolioImpl` now has a creation date field that is automatically set when it is created.
2. `getPortfolioByDate` method in `PortfolioImpl` goes through the stocks in the portfolio and
   fetches the price on the specified date. It totals it all together and presents it as a string.
3. `dailyTimeline` and `monthlyTimeline` methods implemented to retrieve and display portfolio
   performance over time.

PortfolioController & PortfolioControllerImpl

1. Added option to view portfolio performance in either daily or monthly timeframe.

PortfolioModel & PortfolioModelImpl

1. Implemented a `getDailyPerformance` method to show the daily timeline of the portfolio.
2. Implemented a `getMonthlyPerformance` method to show the monthly timeline of the portfolio.
3. Implemented a `addStockToPortfolio` method to add stocks to the existing portfolios.
4. Implemented a `sellStockFromPortfolio` method to sell stocks and update the xml.
5. Implemented a `checkIfStockInPortfolio` method to validate if there are enough shares of the
   stocks the user is trying to sell.
6. Implemented a `dateValidatoin` method to validate the date input is in format `yyyy-MM-dd`.
7. Implemented a `costBasis` method to show the cost basis on a specific date.

#### Assignment 6:
PortfolioGUIView & PortfolioGUIViewImpl

1. Implemented Graphical User Interface.
2. Implemented performance over time histogram.

PortfolioController & PortfolioControllerImpl

1. Users can now choose to input a specific date when selling stocks.
2. Fixed date validation (e.g. Buying stocks from the future) when writing to xmls.


<!-- LICENSE -->

## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->

## Contact

Kevin Lin - [Email](mailto:lin.kevin1@northeastern.edu) - [GitHub](https://github.com/kvl5517)
<br>
Kevin Heleodoro - [Email](mailto:heleodoro.k@northeastern.edu)

- [GitHub](https://github.com/Kevin-Heleodoro)

Project Link: [Assignment 4: Stock Portfolio](https://github.com/Kevin-Heleodoro/pdp_assignment_4)
*Must request access*

<p align="right">(<a href="#readme-top">back to top</a>)</p>




<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo_name.svg?style=for-the-badge

[contributors-url]: https://github.com/Kevin-Heleodoro/pdp_assignment_4/graphs/contributors

[forks-shield]: https://img.shields.io/github/forks/github_username/repo_name.svg?style=for-the-badge

[forks-url]: https://github.com/github_username/repo_name/network/members

[stars-shield]: https://img.shields.io/github/stars/github_username/repo_name.svg?style=for-the-badge

[stars-url]: https://github.com/github_username/repo_name/stargazers

[issues-shield]: https://img.shields.io/github/issues/github_username/repo_name.svg?style=for-the-badge

[issues-url]: https://github.com/github_username/repo_name/issues

[license-shield]: https://img.shields.io/github/license/github_username/repo_name.svg?style=for-the-badge

[license-url]: https://github.com/github_username/repo_name/blob/master/LICENSE.txt

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555

[linkedin-url]: https://linkedin.com/in/linkedin_username

[product-screenshot]: images/screenshot.png

[Docs.oracle.com]: https://docs.oracle.com/en/java/javase/11/docs/api/index.html 
