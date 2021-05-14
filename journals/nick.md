# Nick's Journal

## Week 1: 4/15 - 4/22

### Tasks

For this sprint I worked on this issue: [Creating Auth Server](https://github.com/nguyensjsu/sp21-172-scaleforce/issues/5)

I also created a base project but that was mainly to test the github workflow: [create base folders](https://github.com/nguyensjsu/sp21-172-scaleforce/issues/1)

I also added some comments to John's google doc on the data structures and tables for the application

### Accomplishments
Currently the auth server has all the main functionality needed. It needs another PR to open up admin access via the stubbede interceptor pattern to allow CRUD operations

Commits: 

https://github.com/nguyensjsu/sp21-172-scaleforce/pull/2/commits

https://github.com/nguyensjsu/sp21-172-scaleforce/pull/9/commits

PRs:

https://github.com/nguyensjsu/sp21-172-scaleforce/pull/2

https://github.com/nguyensjsu/sp21-172-scaleforce/pull/9

### Challenges
The main challenge I faced was getting the libraries (namely the Java JWT library) to function correctly. The core problem was in my build.gradle, and resolving it was a combination of using the readme and googling error messages. 
I also had a bit of trouble correctly externalizing the config, but after looking around at how to create a context that uses the application.yml, I got it working with that and a properties file. I saw how you could use a bean that leaves a placeholder configuration and then you can inject it into the application.


## Week 2: 4/22 - 4/29

### Tasks

For this sprint I worked on this on this issue: [Add additionnal Auth functionality](https://github.com/nguyensjsu/sp21-172-scaleforce/issues/12)

### Accomplishments
The Auth server now is in a great state and all endpoints are fully functional. I messed up the pattern for authentication and used an interceptor instead of the more powerful filter, so I switched that over to get the correct permissions. I coordinated with Patrick to make sure we are getting the correct functionality with the changes and now the auth server is complete waiting for additional requirements to be genereated from any discussion with the frontend

Commits: 

https://github.com/nguyensjsu/sp21-172-scaleforce/pull/16/commits

PRs:

https://github.com/nguyensjsu/sp21-172-scaleforce/pull/16

### Challenges
The main challenge was switching the pattern for authentication. Interceptors were not the correct usage as you really want to use the filter pattern as it plays nicely with Spring Security.
I needed to containerize the User class yet extend the UserDetails from Spring Security to get around the first part, add a new filter, then finally instead of using the Security config, I added a default requirement for authentication of any kind (a valid JWT is provided), turned on the ability to use @Secured, then used that to keep out users that can get authentication but lack the specific permission to access an endpoint


## Week 3: 4/29 - 5/6

### Tasks

For this sprint I completed this on this issue, which is a small change to the auth server, as well as testing out the OpenAPI spec generation: [Auth Server changes](https://github.com/nguyensjsu/sp21-172-scaleforce/issues/21)

And am close to but will not quite finish this issue due to my schedule being packed: [Starting API](https://github.com/nguyensjsu/sp21-172-scaleforce/issues/23)

### Accomplishments

Commits:
https://github.com/nguyensjsu/sp21-172-scaleforce/pull/22/commits

PRs:
https://github.com/nguyensjsu/sp21-172-scaleforce/pull/22

Un-merged API work is on this branch with a few commits:
https://github.com/nguyensjsu/sp21-172-scaleforce/tree/start-api

### Challenges
I encountered two challenges: Making dates work while leveraging built-in date types, and confirming the design for the API roles.

Originally, I used Date objects with a formatter and tried to read in the String value. However, Spring doesn't use the parse function when converting dates, just a cast, so it generated an exception on the conversion to an Appointment object. 

Dates were solved by setting the data type to String for persistance and applying a application.yaml-level pattern so the Spring DateTimeFormat annotation worked as intended. I'll need to convert into Date objects to do more advanced math on them later, but this is a good solution for now and it leaves date time formatting to be globally configurable, just like the JWT TTL and secret key  

For the API role setup, since endpoints should have different behavior based on their role, I'm going to have a controller per role and then the token response will need to be adjusted to return the role so I can confirm authentication without having to have the JWT on the API server. I'll be making this change along with some small updates so that the OpenAPI generation for the auth server provides the correct documentation for all endpoints.

## Week 4: 5/6 - 5/13

### Tasks

For this week, I finished two tasks: the API task from last time (plus some bugfixes),  [API server](https://github.com/nguyensjsu/sp21-172-scaleforce/issues/23)

and adjusting the OpenAPI spec generation: [Fix OpenAPI generation for auth server](https://github.com/nguyensjsu/sp21-172-scaleforce/issues/23)

I ran into some more trouble with the date time stuff, so I didn't make as much progress as I hoped.

### Accomplishments

Commits:
https://github.com/nguyensjsu/sp21-172-scaleforce/pull/35/commits

https://github.com/nguyensjsu/sp21-172-scaleforce/pull/36/commits

PRs:

https://github.com/nguyensjsu/sp21-172-scaleforce/pull/35
https://github.com/nguyensjsu/sp21-172-scaleforce/pull/36


### Challenges
Most of my frustration this sprint was revolving around some small date-time errors and inconsistencies regarding them. At first, I left in a @Temporal annotation with a Timestamp type. Therefore, Jackson parser is parsing the date as a "TemporalType.Timestamp". The conversion in the POST calls and query parameters is input as a conversion from String to Date using the DateTimeFormatter I defined in the config. The conversion when pulling from the DB is from MySQL to Date using TemporalType.Timestamp, and I didn't give it the pre-defined pattern, and on top of that, its not a matching type, so an input without a timestamp was kind of converted to UTC. 

What this caused was that an input would look be passed in looking like 6:30, and come back as 13:30, which we later realzed was a convertion to 0 offset using timezones. We needed the API to return the same value that was passed in.

I could set the value without an external configuration, but that defeats the point of leaving the other dateTime foramt as externally configurable. After adding a timezone to the date time pattern and removing the annotation, the conversion is correct, however, we noticed that the database layer is still saving the dates as timestamp-y looking dates, but correctly converts to UTC for responses. Since there are a few more features that need to be added and the behavior on the surface is working as intended of returning an expected response, this small discrepency is left in for now and we will be continuing on. Now as long as you pass in the format, even if there's a timezone, it converts the timezone to 0 offset and returns it, so the behavior is clear why the numbers for the hour is changing.
