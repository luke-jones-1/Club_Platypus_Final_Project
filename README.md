### CGI-Platypi

[![Build Status](https://travis-ci.org/luke-jones-1/Club_Platypus_Final_Project.svg?branch=master)](https://travis-ci.org/luke-jones-1/Club_Platypus_Final_Project)
[![codecov](https://codecov.io/gh/luke-jones-1/Club_Platypus_Final_Project/branch/master/graph/badge.svg)](https://codecov.io/gh/luke-jones-1/Club_Platypus_Final_Project)

#### Running the application

 - Make sure postgres is running
 - `brew install maven`
 - `mvn install`
 - Open project in intellij 
 - Click the run button
 - Hit https://desolate-shelf-99763.herokuapp.com/
 
 #### Running the unit tests
  - Make sure postgres is running
  - `createdb cgi_platypi_test`
  - Open project in intellij 
  - Go to tests and hit run button

## User Stories
  `As a CGI Platypi user, So that I can start chatting to other users, I want to be able to send messages into a chat room.`
  `As a CGI Platypi user, So that I can see what other users are saying, I want to be able to view messages that other users have sent in a chat room.`
  `As a CGI Platypi user, So that I can look back on messages I’ve sent and received, I want messages to be saved in a chat log through a database.`
  `As a CGI Platypi user, So that I can tell other users who I am, I want to be able to create a user account for myself.`
  `As a CGI Platypi user, So that I can differentiate myself from other users, I want to be able to customise my user account with an avatar (ideally a platypus).`
  `As a CGI Platypi user, So that I can talk to different groups of people, I want to be able to switch between different chat rooms.`