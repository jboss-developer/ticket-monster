# What is this?

This is the Hybrid Mobile project for TicketMonster.

## Importing and running the project

Prerequisites
-------------

* JBoss Developer Studio (JBDS) 8.0.0 GA
* The JBoss Hybrid Mobile Tools + CordovaSim feature is installed in JBDS.

For running on an Android emulator:

* The Android Developer Tools plug-in must be installed in JBDS.
* An Android Virtual Device (AVD) having a minimum API level 10 must be available. The recommended API level is 19 (KitKat).

For running on an iOS simulator:

* Mac OS X 10.7 (Lion) or higher.
* XCode 6.0 or higher, with iOS 6 SDK or higher.
* An iOS 5.x or higher simulator for the iPhone or iPad.

If you need more detailed instruction to setup a iOS Development Environment with Apache Cordova, you can take a look at [Setting up your development environment to use Apache Cordova](https://aerogear.org/docs/guides/aerogear-cordova/CordovaSetup/)

Import the ticket-monster Code
------------------------------

First we need to import the existing Hybrid Mobile project code to JBDS.

1. In JBDS, click File then Import.
2. Select *Import Cordova Project* and click *Next*.
3. On Root Directory, click on *Browse...* button and navigate to the `$TICKET-MONSTER_HOME/cordova/` directory on your filesystem.
4. After selecting the TicketMonster-Cordova project, you can click on *Finish* button to start the project import.
5. Make sure that `$TICKET-MONSTER_HOME/cordova/www` is a linked folder that resolves to `../demo/src/main/webapp`.

#### Troubleshooting Windows Operating Systems

As Windows doesn't support symbolic links you must copy `$TICKET-MONSTER_HOME/demo/src/main/webapp` folder to `$TICKET-MONSTER_HOME/cordova/www`


Deploy the application on an Android Emulator
--------------------------------------------

1. Select your project in JBDS.
2. Click on `Run`, then `Run As` and `Run on Android Emulator`.


Deploy the application on an iOS Simulator
--------------------------------------------

1. Select your project in JBDS.
2. Click on `Run`, then `Run As` and `Run on iOS Simulator`.
