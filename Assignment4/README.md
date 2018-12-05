# Assignment 4

## Dependencies
- NodeJS
- jQuery
- NPM


## Installation Instructions
1. Download the repository
2. Run `npm install` from the repository
3. Run `node app.js` from where node.js was downloaded to
4. Open `http://localhost:3000`

## Admin Functionality
All created accounts are assigned the 'user' role.  To access admin functionality, log in with the predefined admin information:
- username: admin
- password: admin

This will allow you to view and utilize the designated admin functionality - altering the style and design of the page among several presets.

## User Functionality
Any accounts created through registering will be instantiated as a 'user' role.  Both Users and Admins will have access to the basic user functionality - a simple guessing game that gives hints.

## Logout 'Feature'
Due to the fact that we whitelist appropriate methods of navigation through the application (form submit, clicking a link, refreshing with F5, etc.), any "invalid" methods of navigation (ones that aren't specifically whitelisted) will result in the session being destroyed.  It is impossible to interact with the "Reload this page" button to the left of the address bar with javascript (as it is outside the DOM and window of the page).  As a result, clicking that button to refresh the page will be treated as an invalid navigation and destroy the session.  Refreshing using F5, however, is whitelisted as valid navigation and will not destory the session.
