# TicketMonster - a JBoss example

TicketMonster is an online ticketing demo application that gets you started with JBoss technologies, in particular the [JBoss Developer Framework](http://jboss.org/jdf), and helps you learn and evaluate them.

Here are a few instructions for building and running it. You can learn more about the example from the [tutorial](http://www.jboss.org/jdf/examples/get-started).

## Updating the Performance dates

_NOTE: This step is optional. It is necessary only if you want to update the dates of the Performances in the `import.sql` script in an automated manner. Updating the performance dates ensure that they are always set to some timestamp in the future, and ensures that all performances are visible in the Monitor section of the TicketMonster application.

1. Run the `update_import_sql` Perl script. You'll need the `DateTime`, `DateTime::Format::Strptime` and `Tie::File` Perl modules. These are usually available by default in your Perl installation.
    
        $ perl update_import_sql.pl src/main/resources/import.sql

## Generating the administration site

_NOTE: This step is optional. The administration site is already present in the source code. If you want to regenerate it from Forge, and apply the changes outlined in the tutorial, you may continue to follow the steps outlined here. Otherwise, you can skip this step and proceed to build TicketMonster._

Before building and running TicketMonster, you must generate the administration site with Forge.

1. Ensure that you have [JBoss Forge](http://jboss.org/forge) installed. The current version of
   TicketMonster supports version 1.4.0.Final or higher of JBoss Forge.

2. Start JBoss Forge

        $ forge

3. Verify that the Forge plugin is installed by running

            $ forge list-plugins

   and verifying that `org.jboss.forge.angularjs-scaffoldx-plugin` is in the returned list.

4.  If the outcome of the previous step was that the AngularJS plugin was not installed, do that now

            $ forge install-plugin angularjs
	
5. From the JBoss Forge prompt, execute the script for generating the administration site
    
	    $ run admin_layer.fsh

Steps 3 and 4 need to be performed only once - after the plugin has been installed, it will be
available on any subsequent runs of Forge.

On step 5, answer _yes_ to all the the questions concerning patches. Deployment to JBoss EAP 6 or AS7 is optional.

## Building TicketMonster

TicketMonster can be built from Maven, by runnning the following Maven command:

    mvn clean package
	
### Building TicketMonster with tests
	
If you want to run the Arquillian tests as part of the build, you can enable one of the two available Arquillian profiles.

For running the tests in an _already running_ application server instance, use the `arq-jbossas-remote` profile.

    mvn clean package -Parq-jbossas-remote

If you want the test runner to _start_ an application server instance, use the `arq-jbossas-managed` profile. You must set up the `JBOSS_HOME` property to point to the server location, or update the `src/main/test/resources/arquillian.xml` file.

    mvn clean package -Parq-jbossas-managed
	
### Building TicketMonster with Postgresql (for OpenShift)

If you intend to deploy into [OpenShift](http://openshift.com), you can use the `postgresql-openshift` profile

    mvn clean package -Ppostgresql-openshift
	
## Running TicketMonster

You can run TicketMonster into a local JBoss AS7 instance or on OpenShift.

### Running TicketMonster locally

#### Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile


1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat
		
#### Deploy TicketMonster

1. Make sure you have started the JBoss Server as described above.
2. Type this command to build and deploy the archive into a running server instance.

        mvn clean package jboss-as:deploy
	
	(You can use the `arq-jbossas-remote` profile for running tests as well)

3. This will deploy `target/ticket-monster.war` to the running instance of the server.
4. Now you can see the application running at `http://localhost:8080/ticket-monster`

### Running TicketMonster in OpenShift

#### Create an OpenShift project

1. Make sure that you have an OpenShift domain and you have created an application using the `jbossas-7` cartridge (for more details, get started [here](https://openshift.redhat.com/app/getting_started)). If you want to use PostgreSQL, add the `postgresql-8.4` cartridge too.
2. Ensure that the Git repository of the project is checked out.

#### Building and deploying

1. Build TicketMonster using either: 
    * the default profile (with H2 database support)
    
            mvn clean package	
    * the `postgresql-openshift` profile (with PostgreSQL support) if the PostgreSQL cartrdige is enabled in OpenShift.
            
            mvn clean package -Ppostgresql-openshift
			
2. Copy the `target/ticket-monster.war`file in the OpenShift Git repository(located at `<root-of-openshift-application-git-repository>`).

	    cp target/ticket-monster.war <root-of-openshift-application-git-repository>/deployments/ROOT.war

3. Navigate to `<root-of-openshift-application-git-repository>` folder
4. Remove the existing `src` folder and `pom.xml` file. 

        git rm -r src
		git rm pom.xml

5. Add the copied file to the repository, commit and push to Openshift
        
		git add deployments/ROOT.war
		git commit -m "Deploy TicketMonster"
		git push

6. Now you can see the application running at `http://<app-name>-<domain-name>.rhcloud.com`

_NOTE: this version of TicketMonster uses the *binary* deployment style._ 




	
 



