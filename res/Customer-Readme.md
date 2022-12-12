# How to Run the JAR File

1. cd to the res folder of the submission

2. Type `java -jar ass7.jar` to run the JAR file.\
The program should start in the terminal. Follow the instruction displayed to use text / GUI interfaces.


# Features that we were able to implement

### What we were able to do

1. We implemented the re-balancing of the portfolio on today's date based on the weightages supplied by the client.

2. 

### What we were not able to do

1. We were not able to re-balance portfolio on a specific date.

This was because of the following reasons: 
- The providers have not implemented a method where we can fetch the contents of the portfolio on a given date. We can only get contents as of the latest date.
As a result, there is no way for us to figure out what stocks should be allowed for rebalancing the portfolio.

- Even if we are able to get composition on a specific date, their sell method does not work correctly. 
They just find the ticker that is present in their portfolio and just reduce their quantity by a certain amount. It doesn't matter if it invalidates the past or future transactions 
or not. 


2. We were not able to test the re-balancing function.
This is due to the following reasons:

- The providers have not written any test for their controllers and views. As a result we were not able to add controller and view 
tests.

- We tried to create new test classes for the controllers and view, however, we were not able to do it properly 
as it was hard to understand the coupling between model view and controllers in their program. 

# Changes we made to the provider's code