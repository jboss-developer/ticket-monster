Import the ticket-monster Code
---------------------------

First we need to import the existing Android code to JBDS or Eclipse

1. On Eclipse, click File then import.
2. Select *Existing Android Code Into Workspace* and click *Next*.
3. On Root Directory, click on *Browse...* button and navigate to the `TICKET-MONSTER_HOME/cordova/android/TicketMonster` project on your filesystem.
4. After selecting the TicketMonster  project, you can click on *Finish* button to start the project import.
5. Make sure that your `TICKET-MONSTER_HOME/cordova/android/TicketMonster/assets/www` is a symbolic link to `../../../../demo/src/main/webapp`

#### Troubleshooting Windows Operating Systems

As Windows doesn't support symbolic links you must copy `TICKET-MONSTER_HOME/demo/src/main/webapp` folder to `TICKET-MONSTER_HOME/cordova/android/TicketMonster/assets/www`


Start the Emulator and Deploy the application
--------------------------------------------

1. Start the emulator on Eclipse by clicking *Window* and select *AVD Manager*.
2. On Android Virtual Device Manager window, select the appropriate AVD and click on *Start* button.
3. On Lauch Options window click on *Lauch* button.
4. After Emulator started, select your project on Eclipse
5. Click on *Run*, then *Run As* and *Android Application*
