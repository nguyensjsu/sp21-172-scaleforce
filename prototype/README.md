# Prototype

## Local Deployment

### Containerized MySQL, non-containerized Spring Boot app, non-containerized React app

#### MySQL

##### Start MySQL (first run)

```zsh
docker run --name mysql -td \
-p 3306:3306 \
-e MYSQL_ROOT_PASSWORD=password \
mysql:8.0
```

MySQL needs to be initialized on the first run. Do this either via the CLI or
via MySQL Workbench.

##### Start MySQL (subsequent runs)

```zsh
docker start mysql
```

##### Initialize MySQL - Option 1: CLI

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

##### Initialize MySQL - Option 2: MySQL Workbench

1. Connect to local instance, password should be `password`

   ![mysql workbench splash](./images/mysql-workbench-splash.png)

2. Navigate to `Open SQL Script`

   ![mysql workbench open sql script](./images/mysql-workbench-open-sql-script.png)

3. Open `backend/create-database-and-default-user.sql`

4. Execute the script

#### Spring Boot app

##### Build Spring Boot app

```zsh
cd backend
gradle build
```

Note: MySQL must be running in order to build the project, as tests require an
instance of MySQL at runtime. Alternatively, build the app without testing as follows:

```zsh
gradle build -x test
```

##### Run Spring Boot app

```zsh
gradle bootRun
```

API is available at `http://localhost:8080/todos`.

#### React app

##### Start React app

```zsh
cd frontend
npm start
```

Your browser should automatically open, navigating to `http://localhost:3000`.

### Containerized MySQL, Spring Boot app, React app (non-`docker-compose`)

When running both MySQL, Spring Boot, and React all via Docker, a dedicated network
is necessary:

```zsh
docker network create --driver bridge scaleforce
```

#### MySQL

##### Start MySQL (dedicated network)

```zsh
docker run --name mysql -td \
--network scaleforce \
-p 3306:3306 \
-e MYSQL_ROOT_PASSWORD=password \
mysql:8.0
```

Don't forget to initialize MySQL.

#### Spring Boot app

##### Build Spring Boot app image

Note: Make sure you've successfully built the Spring Boot app via `gradle build`.

```zsh
cd backend
docker build -t sp21-172-scaleforce/backend .
```

##### Run Spring Boot app from image

```zsh
docker run --name backend -td \
--network scaleforce \
-p 8080:8080 \
-e "MYSQL_HOST=mysql" \
sp21-172-scaleforce/backend
```

#### React app

##### Build React app image

```zsh
cd frontend
docker build -t sp21-172-scaleforce/frontend .
```

##### Run React app from image - Option 1: `--network="host"`

```zsh
docker run --name frontend -itd \
--network="host" \
-v ${PWD}:/app \
-v /app/node_modules \
sp21-172-scaleforce/frontend
```

App should be available at `http://localhost:8080`. I need to do more research
as to whether `--network="host"` will work for GKE.

##### Run React app from image - Option 2: `--network scaleforce`

This method seems a little more kosher, but involves more work. First off, the
original proxy defined in `package.json` means that the React app expects to be
able to access the API at `http://localhost:8080`. This doesn't work in the
dedicated Docker network. This means that we have to change the value for proxy
from `http://localhost:8080` to `http://backend:8080`, **which also means a
rebuild of the app image is required**. The rebuild should be relatively fast,
since the difference is small.

Then, run the app (note the difference in network and port options):

```zsh
docker run --name frontend -itd \
--network scaleforce \
-p 3000:3000 \
-v ${PWD}:/app \
-v /app/node_modules \
sp21-172-scaleforce/frontend
```

### Containerized MySQL, containerized Spring Boot app, containerized React app - `docker-compose`

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
out of my depth here. Have yet to test with React app.
