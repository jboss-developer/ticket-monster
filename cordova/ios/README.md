System requirements
-------------------

All you need to build this project is Mac OS X Lion (10.7) (or later) and XCode 4.5 or later and the appropriate iOS SDK for your needs.

With the prerequisites out of the way, you're ready to build and deploy.

If you need more detailed instruction to setup a iOS Development Environment with Apache Cordova, you can take a look at [Setting up your development enivronment to use Apache Cordova](http://aerogear.org/docs/guides/CordovaSetup/)

Import the Ticket-monster Code
--------------------------

First we need to import the existing iOS code to XCode.

1. Open Finder and navigate to `TICKET-MONSTER_HOME/kitchensink-cordova/ios/`
2. Right click on *TicketMonster.xcodeproj* and select *Open With XCode*
3. Change the Target in the Scheme menu to TicketMonster and select a device
4. Make sure that your `TICKET-MONSTER_HOME/cordova/ios/www` is a symbolic link to `../../demo/src/main/webapp/`


Run and Access the application
------------------------------

If the toolbar in XCode is visible, click on the *Run* button. This will start the iOS Simulator with this quickstart running in it. If your toolbar is not visible, click `View -> Show Toolbar` to show it.

