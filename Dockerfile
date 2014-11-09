# Use latest jboss/base-jdk:7 image as the base
FROM jboss/base-jdk:7

MAINTAINER pmuir@bleepbleep.org.uk

# Install Maven
USER root
RUN yum install -y maven
USER jboss

# Set the EAP_VERSION env variable
ENV EAP_VERSION 6.3.0
ENV EAP_MINOR_VERSION 6.3

# Add the EAP distribution to /opt, and make wildfly the owner of the extracted tar content
# Make sure the distribution is available from a well-known place
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

# Set up settings.xml with our recommendations
RUN mkdir -p /opt/jboss/.m2
ADD ./settings.xml /opt/jboss/.m2/

# Build a copy of the maven repo 
RUN mkdir -p /opt/jboss/ticket-monster
WORKDIR /opt/jboss/ticket-monster
ADD ./demo/pom.xml /opt/jboss/ticket-monster/
RUN mvn -fn dependency:resolve dependency:resolve-plugins
ADD ./demo /opt/jboss/ticket-monster/

# Fix permissions on jboss-eap-quickstarts
USER root
RUN chown jboss /opt/jboss/ticket-monster
USER jboss

# Build the quickstart
RUN mvn clean package

# Copy the created war file to the #{product_name} deployments file, so that is deployed when #{product_name} starts
RUN cp target/ticket-monster.war /opt/jboss/jboss-eap/standalone/deployments/

# Set the default command to run on boot
# This will boot WildFly in the standalone mode and bind to all interface
CMD ["/opt/jboss/jboss-eap/bin/standalone.sh", "-b", "0.0.0.0"]

