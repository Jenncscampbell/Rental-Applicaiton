# cpsc304project
How to Run:

To run our code, all that is necessary is to clone the repository into an IntelliJ IDE
and run one of the respective UIs. PLEASE ENSURE YOU ARE ON BRANCH FINAL_COPY.
Clerk and Customer interactions have been separated and will need to be run
separately (though they are in the same project).

The database connection should be automatically made upon clicking run, and
the sql files used will be the ones on the student server account
hard-coded into either of the UI's main runnable functions. These
files will be identical to the ones we turn in, however, 
if you would like to add the SQL files (project_tables.sql) to your own student account 
on the server to test our code, ensure that in the main runnable 
functions of SuperRentUI and SuperRentUIClerk, you  change the 
hard-coded username and password to your own.

Expected Behaviour:

 - Unless specified, any and all years need to be specified YYYY-MM-DD.

 - For RentalReports, if report is too large, it will cut off bottom of the screen. So this does not happen, please test with November 1st, 2019.

 - For both Branch and Company Return reports: we could not integrate with UI, however our SQL scripts provided in the the lower half (below large indent) of report.sql  work locally.
 
 - When searching for vehicle availability, for the date, it is not compatible with the reservation date table because of java date formatting so it cannot check if a vehicle is already reserved. Please see the check_availability.sql file to see correct sql code.

 - For check availability, in the Customer UI (SuperRentUI), once you've clicked submit, the amount of available vehicles will display in a box in the top right corner. Please click directly on the number to list available vehicles.
