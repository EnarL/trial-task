**<h1>Fujitsu Trial Task</h1>**
**Author: Enar Leini**

Sub-functionality of the food delivery application to calculate delivery fees based on regional base fee, vehicle type, and weather conditions.

Used technology:
- Java
- Spring Boot
- H2 database
- Gradle
- Cronjob


**<h1>How to run the project</h1>**

- Run the project using an IDE by running the TrialTaskApplication class

- The application is set to start on port 8080. 
- The database can be accessed at localhost.8080/h2-console. 
- I added a screenshot to see what should the settings be to access the database.

![image](https://github.com/EnarL/trial-task/assets/116269322/aba75983-4e41-4632-8cb9-9120b6b1c8f3)

**<h1>API requests</h1>**

- All the requests are found in Controller class.
1) /calculateRbf - used for calculating delivery fee using city and vehicle type (method: GET)
2) /changeBaseFeeRules - used to change base fees for selected city and vehicle type. (method: PUT)
3) /changeExtraFeeRules - used to change extra fees for selected table. (method: PUT)
4) /addExtraFeeRules - used to add extra fees for selected table. (method: POST)
5) /deleteExtraFeeRules - used to delete extra fees for selected table (method: DELETE)
