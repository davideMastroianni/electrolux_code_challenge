# Electrolux Code Challenge

	- Imagine you are one of our employee and you got assigned to a feature.

## Scenario:

	- They have a number of connected appliances that belongs to a number of customers.
	- They have a need to be able to view the status of the connection among these appliances on a monitoring display.
	- The appliances send the status of the connection one time per minute.
	- The status can be compared with a ping (network trace); no request from the appliance means no connection. (No need to ping anything, It is just to understand the concept)  
	- So, Appliance is either Connected or Disconnected.

## Task:

	- Your task will be to create a data store that keeps these appliances with their status and the customers who own them.
	- Obviously, for this task, there are no real appliances available that can respond to your "ping" request.
	- This can either be solved by using static values or ​​by creating a separate machinery that returns random fake status.

## Good to have
	- Appliance could be separate service (Micro service)
	- Could support high transactions (High number of appliances).

## Requirements
	1-An API
	2-Designing the database (In memory database also is acceptable).
	3-Creating Services
	4-Use dependency injection
	5-Do Unit Testing
	6-Use Java or C#

# Install and run

* Run ```mvn clean compile```
* To run the service launch ./scripts/run.sh from root directory
* Import the postman collections: ecc-integration-test.postman_collection.json and ecc-integration-test-environment.postman_environment.json
* Run the postman routine to execute the tests

# Swagger and openApi

After running the service a postman collection from swagger can be found
* http://localhost:8080/api-docs
* http://localhost:8080/swagger-ui.html