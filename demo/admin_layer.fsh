clear;

echo "Generating the scaffold.";
scaffold-generate --provider AngularJS --webRoot /admin --targets org.jboss.examples.ticketmonster.model.* --generator ROOT_AND_NESTED_DTO --packageName org.jboss.examples.ticketmonster.rest;

echo "Don't forget to apply the manual changes described in tutorial provided in admin_layer_functional.patch and admin_layer_graphics.patch. Both files are in the patches sub-directory.";

echo "To build and deploy the application to JBoss EAP, run the following command:"
echo "   build clean package wildfly:deploy";

echo "Examine the app deployed at http://localhost:8080/ticket-monster/admin/index.html";
