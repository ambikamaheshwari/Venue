# Venue

In VenueMenu workspace, 
VenueMain/src/venue/VenueData.java is class used to calcualte haversine function, extracting all the output from the database,
displaying of all the outputs and main menu selection from user.

VenueMain/src/venue/VenueMain.java is the main class consist of all code for connecting to database, switch case for doing 
appropriate function of option selected by user.

VenueMain/Test Data.txt consist of some sample test data


Informal Summary Report 

Abstract:

This document is a summary of ideas used and challenges faced while completing the given task. It gives a very brief overview 
of software used and the approach to solve some of the interesting challenges encountered during the task 

Software:

Eclipse IDE for Java EE Developers, Java SE 8u45, MongoDB 3.0.3 were used to complete the task.

Database: NOSql should have been no problem (pun intended!!)

Having had no prior experience with NOSql I looked online for the various solutions. I learnt mainly there are four variants 
for NOSql database. Among those column based looked familiar to MySQL that I had worked as well as a good alternative for my 
current task. It took me whole two days of trying to install and after crashing my computer to realize that Cassandra and 
Hbase aren’t working as well as expected. 

MongoDB: Third time is the charm!

I decided to install it. Installation was much simpler and involved just a double click and changing a single config file.
Running the database and importing the CSV involved just a one-line command and no need to create a schema unlike Cassandra.

Haversine Formula

I have used uber and yelp many times but till date never gave a thought how did they display data in the app given my position. Thanks to Haversine Formula I was able to 
solve a the challenge to find the distance given the longitude and latitude  

Query Parser 

MongoDB query outputs the data in BSON format. I used JSON parser to extract key value pair from the output

Added Feature:

User supplied input (not all) are validated against a regex for input validation. Currently for zipcode,city and venue name.

Source:

https://www.digitalocean.com/community/tutorials/a-comparison-of-nosql-database-management-systems-and-models

http://rosettacode.org/wiki/Haversine_formula
