# Use the JDK 7 image as the base
FROM jboss/base-jdk:7

MAINTAINER vpereira@redhat.com

# Install JBoss EAP
ENV EAP_VERSION 6.3.0
ENV EAP_MINOR_VERSION 6.3

# Add the JBoss EAP distribution to /opt, and make jboss the owner of the extracted tar content
# Remember to fetch and place the jboss eap distribution in the same directory as the docker file.
ADD ./jboss-eap-$EAP_VERSION.zip /opt/jboss/
USER root
RUN chown jboss /opt/jboss/jboss-eap-$EAP_VERSION.zip
USER jboss
WORKDIR /opt/jboss
RUN unzip jboss-eap-$EAP_VERSION.zip && mv jboss-eap-$EAP_MINOR_VERSION jboss-eap && rm jboss-eap-$EAP_VERSION.zip

# Set the JBOSS_HOME env variable
ENV JBOSS_HOME /opt/jboss/jboss-eap

# Expose the ports we're interested in
EXPOSE 8080 9990

# Add the TicketMonster WAR to the deployments directory of JBoss EAP.
# Remember to place the TicketMonster WAR into the root directory before running the Docker build
# You can build it and place it in the root dir, using:
# mvn -f demo clean package && cp demo/target/ticket-monster.war .
ADD ./ticket-monster.war /opt/jboss/jboss-eap/standalone/deployments/

# Set the default command to run on boot
# This will boot JBoss EAP in the standalone mode and bind to all interfaces
CMD ["/opt/jboss/jboss-eap/bin/standalone.sh", "-b", "0.0.0.0"]