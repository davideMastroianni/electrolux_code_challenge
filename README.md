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

##  Data:
Below you have all customers from the system; their addresses and the appliances they own.

|-----------------------------------|
| Kalles Grustransporter AB         |
| Cementvägen 8, 111 11 Södertälje  |
|-----------------------------------|
| AID (ApplianceId)   Factory. nr.  |
|-----------------------------------|
| YS2R4X20005399401     ABC123      |
| VLUR4X20009093588     DEF456      |
| VLUR4X20009048066     GHI789      |
|-----------------------------------|

|-----------------------------------|
| Johans Bulk AB                    |
| Balkvägen 12, 222 22 Stockholm    |
|-----------------------------------|
| AID (ApplianceId)   Factory. nr.  |
|-----------------------------------|
| YS2R4X20005388011     JKL012      |
| YS2R4X20005387949     MNO345      |
------------------------------------|

|-----------------------------------|
| Haralds Värdetransporter AB       |
| Budgetvägen 1, 333 33 Uppsala     |
|-----------------------------------|
| AID (ApplianceId)   Factory. nr.  |
|-----------------------------------|
| YS2R4X20009048066     PQR678      |
| YS2R4X20005387055     STU901      |
|-----------------------------------|