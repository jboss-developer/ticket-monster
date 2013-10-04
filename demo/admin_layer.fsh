@/* Forge Script - Generates the administration view */;

@/* Detect Forge version */;
@v = SHELL.getEnvironment().getRuntimeVersion();

@/* Clear the screen */;
clear;

@/* This means less typing. If a script is automated, or is not meant to be interactive, use this command */; 
set ACCEPT_DEFAULTS true;

@/* Create REST resources from entities */;
rest endpoint-from-entity --contentType application/json org.jboss.jdf.example.ticketmonster.model.* --strategy ROOT_AND_NESTED_DTO;

@/* Enable scaffolding from entities */;

scaffold-x setup --scaffoldType angularjs --targetDir admin;


@/* Scaffold CRUD views for the entities that an admin would start drilling down into the data model from */;

scaffold-x from src/main/java/org/jboss/jdf/example/ticketmonster/model/* --targetDir admin --overwrite;

@/* Ask the user whether they want to patch in the changes described in the tutorial */;

set ACCEPT_DEFAULTS false;

if ( SHELL.promptBoolean("Apply manual functional changes described in tutorial?") ) {
    if (v.startsWith("1.4.0")) {
      echo Applying manual changes described in tutorial based on admin_layer_functional.patch;
      git apply -v --ignore-whitespace --ignore-space-change patches/admin_layer_functional.patch;
    } else if (v.startsWith("1.4.1")) {
      echo Applying manual changes described in tutorial based on admin_layer_functional.patch;
      git apply -v --ignore-whitespace --ignore-space-change patches/admin_layer_functional.patch;
    } else {
      @SHELL.println("The version " + v + " is not supported yet, attempting to apply latest patch");
      git apply -v --ignore-whitespace --ignore-space-change patches/admin_layer_functional.patch;
    } 
}

if ( SHELL.promptBoolean("Apply manual visual changes?") ) {
    if (v.startsWith("1.4.0.")) {
      echo Applying manual visual changes based on admin_layer_graphics.patch;
      git apply -v --ignore-whitespace --ignore-space-change patches/admin_layer_graphics.patch;
    } else if (v.startsWith("1.4.1")) {
      echo Applying manual visual changes based on admin_layer_graphics.patch;
      git apply -v --ignore-whitespace --ignore-space-change patches/admin_layer_graphics.patch;
    } else {
      @SHELL.println("The version " + v + " is not supported yet, attempting to apply latest patch");
      git apply -v --ignore-whitespace --ignore-space-change patches/admin_layer_graphics.patch;
    }
}

if ( SHELL.promptBoolean("Deploy to JBoss AS 7?") ) {
    @/* Deploy this to JBoss AS 7 to see the result */;
    build clean package jboss-as:deploy;

    echo Examine the app so far at http://localhost:8080/ticket-monster/admin/index.html;

} else {
    build clean package;
}
