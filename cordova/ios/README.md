# What is this?

This the Cordova-iOS project for TicketMonster.

## Importing and running the project

Prerequisites
-------------

To build this project you'll need Mac OS X 10.7 (Lion) or later. You'll also need XCode 4.5 (or later), and the iOS 6 SDK or higher. Finally, you will need an iOS 5.0 simulator or higher, to run this project.

With the prerequisites out of the way, you're ready to build and deploy.

If you need more detailed instruction to setup a iOS Development Environment with Apache Cordova, you can take a look at [Setting up your development enivronment to use Apache Cordova](http://aerogear.org/docs/guides/CordovaSetup/)

Import the Ticket-monster Code
--------------------------

First we need to import the existing iOS code to XCode.

1. Open Finder and navigate to `$TICKET-MONSTER_HOME/kitchensink-cordova/ios/`
2. Right click on *TicketMonster.xcodeproj* and select *Open With XCode*
3. Change the Target in the Scheme menu to TicketMonster and select a device
4. Make sure that your `$TICKET-MONSTER_HOME/cordova/ios/www` is a symbolic link to `../../demo/src/main/webapp/`


Run and Access the application
------------------------------

If the toolbar in XCode is visible, click on the *Run* button. This will start the iOS Simulator with this project running in it. If your toolbar is not visible, click `View -> Show Toolbar` to show it.

