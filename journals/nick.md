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

TODO "A snapshot (point-in-time) image of the Team's Task Board highlighting
which "Card" you worked on"

### Accomplishments

TODO "A discussion of your accomplishments that week with a list of links to
your Code Commits and PRs"

### Challenges

TODO "A discussion of the challenges you faced that week and how you resolved
those issues"

## Week 4: 5/6 - 5/13

### Tasks

TODO "A snapshot (point-in-time) image of the Team's Task Board highlighting
which "Card" you worked on"

### Accomplishments

TODO "A discussion of your accomplishments that week with a list of links to
your Code Commits and PRs"

### Challenges

TODO "A discussion of the challenges you faced that week and how you resolved
those issues"
