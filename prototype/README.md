# Prototype

## Local Deployment

### Non-Dockerized Spring Boot App, Dockerized MySQL

#### Start MySQL (first run)

```zsh
docker run --name mysql -td -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password mysql:8.0
```

MySQL needs to be initialized on the first run. Do this either via the CLI or
via MySQL Workbench.

#### Start MySQL (subsequent runs)

```zsh
docker start mysql
```

#### Initialize MySQL - Option 1: CLI

1. Get a shell to the MySQL container

```zsh
docker exec -t mysql bash
```

2. Connect to MySQL, password should be `password`

```zsh
root@b780cabe0641:/# mysql --password
Enter password:
```

3. Run the commands from `create-database-and-default-user.sql` manually

4. Exit MySQL and the container shell

```zsh
mysql> exit
Bye
root@b780cabe0641:/# exit
exit
```

#### Initialize MySQL - Option 2: MySQL Workbench

1. Connect to local instance, password should be `password`

![mysql workbench splash](./images/mysql-workbench-splash.png)

2. Navigate to `Open SQL Script`

![mysql workbench open sql script](./images/mysql-workbench-open-sql-script.png)

3. Open `backend/create-database-and-default-user.sql`

4. Execute the script

#### Build, run the Spring Boot App

```zsh
cd backend
gradle build # build the app
gradle bootRun # run the app
```

Note: MySQL must be running in order to build the project, as :

- tests will be run automatically,
- `BackendApplicationTests` will start a Spring application context, and
- an instance of MySQL is required at runtime

Alternatively, build the app without testing as follows:

```zsh
cd backend
gradle build -x test
```

#### Stop MySQL

```zsh
docker stop mysql
```

### Dockerized Spring Boot App, Dockerized MySQL (non-`docker-compose`)

When running both the Spring Boot app and MySQL via Docker, a dedicated network
is necessary:

```zsh
docker network create --driver bridge scaleforce
```

#### Start MySQL (isolated network)

```zsh
docker run --name mysql --network scaleforce -td -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password mysql:8.0
```

Don't forget to initialize MySQL.

#### Create, run image of the Spring Boot app

To create a fresh image of the app:

```zsh
cd backend
docker build -t sp21-172-scaleforce/backend .
```

To run it:

```zsh
docker run --name backend --network scaleforce -td -p 8080:8080 -e "MYSQL_HOST=mysql" sp21-172-scaleforce/backend
```

### Dockerized Spring Boot App, Dockerized MySQL (`docker-compose`)

Wasn't able to get this working. `docker-compose` successfully starts a
container for `mysql` and initializes it using
`create-database-and-default-user.sql` and a container for `api`. `api`
generally restarts at least once since the container is created after the
`mysql` container is created, but not necessarily before the `mysql` container
is accessible via port 3306, resulting in exceptions that force `api` to
restart.

After checking the logs and confirming `api` is running and successfully
connected to the `mysql` container, I manually tested that `api` is functional
by exposing port 8080. In `docker-compose.yml`, replace

```dockerfile
...
  api:
    ...
    ports:
      - 8080
      ...
```

with

```dockerfile
...
  api:
    ...
    ports:
    - 8080:8080
    ...
```

When hitting `/todos` on port 8080, we see that `api` is running as
expected. (Note: the 404 is expected, as `mysql` starts with 0 todos).

![service available](./images/service-available.png)

However, when trying to access the API via the load-balancer on port 80, the
service is not available and haproxy reports that the backend is down due to a
failing health check.

![service unavailable](./images/service-unavailable.png)

![haproxy health check error](./images/haproxy-health-check-error.png)

I believe this is related to haproxy making a certain HTTP request against the
backend and expecting a certain status code, so I think a combination of
updating the backend app and using a `haproxy.cfg` might work, but I'm honestly
out of my depth here.
