@/* Forge Script - Generates the administration view */;

@/* Detect Forge version */;
@v = SHELL.getEnvironment().getRuntimeVersion();

@/* Clear the screen */;
clear;

@/* This means less typing. If a script is automated, or is not meant to be interactive, use this command */; 
set ACCEPT_DEFAULTS true;

@/* Enable scaffolding from entities */;

scaffold setup --targetDir admin;

@/* Enable RichFaces for wizzy widgets from entities */;

richfaces setup;


@/* Scaffold CRUD views for the entities that an admin would start drilling down into the data model from */;

scaffold from-entity org.jboss.jdf.example.ticketmonster.model.* --targetDir admin;

@/* Add in the version of Guava that works with Errai until a version of RichFaces has a more current version */;

project add-dependency com.google.guava:guava:12.0:compile

@/* Ask the user whether they want to patch in the changes described in the tutorial */;

set ACCEPT_DEFAULTS false;

if ( SHELL.promptBoolean("Apply manual functional changes described in tutorial?") ) {
    if (v.startsWith("1.0.")) {
      echo Applying manual changes described in tutorial based on admin_layer_functional_1.0.patch;
      git apply -v --ignore-whitespace --ignore-space-change patches/admin_layer_functional_1.0.patch;
      cp src/main/webapp/index.xhtml src/main/webapp/admin/index.xhtml;
      rm --force src/main/webapp/index.xhtml;
    } else if (v.startsWith("1.1.") || v.startsWith("1.2") || v.startsWith("1.3.0")) {
      echo Applying manual changes described in tutorial based on admin_layer_functional.patch;
      git apply -v --ignore-whitespace --ignore-space-change patches/admin_layer_functional.patch;
    } else if (v.startsWith("1.3.1")) {
      echo Applying manual changes described in tutorial based on admin_layer_functional_1.3.1.patch;
      git apply -v --ignore-whitespace --ignore-space-change patches/admin_layer_functional_1.3.1.patch;
    } else {
      @SHELL.println("The version " + v + " is not supported yet, attempting to apply latest patch");
      git apply -v --ignore-whitespace --ignore-space-change patches/admin_layer_functional_1.3.1.patch;
    } 
}

if ( SHELL.promptBoolean("Apply manual visual changes?") ) {
    if (v.startsWith("1.0.")) {
       echo Applying manual visual changes based on admin_layer_graphics_1.0.patch;
       git apply --ignore-whitespace --ignore-space-change patches/admin_layer_graphics_1.0.patch;
    } else if (v.startsWith("1.1.") || v.startsWith("1.2") || v.startsWith("1.3.0")) {
      echo Applying manual visual changes based on admin_layer_graphics.patch;
      git apply --ignore-whitespace --ignore-space-change patches/admin_layer_graphics.patch;
    } else if (v.startsWith("1.3.1")) {
      echo Applying manual visual changes based on admin_layer_graphics_1.3.1.patch;
      git apply --ignore-whitespace --ignore-space-change patches/admin_layer_graphics_1.3.1.patch;
    } else {
       @SHELL.println("The version " + v + " is not supported yet, attempting to apply latest patch");
       git apply --ignore-whitespace --ignore-space-change patches/admin_layer_graphics_1.3.1.patch;
    }
}

if ( SHELL.promptBoolean("Deploy to JBoss AS 7?") ) {
    @/* Deploy this to JBoss AS 7 to see the result */;
    build clean package jboss-as:deploy;

    echo Examine the app so far at http://localhost:8080/ticket-monster/faces/admin/index.xhtml;

} else {
    build clean package;
}


