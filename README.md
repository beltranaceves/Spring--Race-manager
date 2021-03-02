# Running the project example
---------------------------------------------------------------------
Race manager server and CLI client:

   - Create with name, city, date, max participans and ticket price

   - Search for races by date and/or city
   
   - Runners can inscribe themselves in a race/collect their dorsal number/get all their inscriptions

## Running the ws-app service with Maven/Jetty.

	cd ws-app/ws-app-service
	mvn jetty:run

## Running the client application

- Configure `ws-app/ws-app-client/src/main/resources/ConfigurationParameters.properties`
  for specifying the client project service implementation (Rest or Thrift) and 
  the port number of the web server in the endpoint address (9090 for Jetty, 8080
  for Tomcat)
