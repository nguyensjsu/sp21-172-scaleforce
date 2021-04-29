# DEPLOYMENT

## Local Deployment (minimum container usage)

### `auth-server`

#### Build, run `auth-server`

- Create a MySQL container

    ```zsh
    docker run --name mysql -td \
    -p 3306:3306 \
    -e MYSQL_ROOT_PASSWORD=cmpe172 \
    mysql:8.0
    ```

- Initialize MySQL using `auth-server/scripts/schema.sql`
  - Easiest way is via MySQL Workbench
- Run `auth-server`

    ```zsh
    cd auth-server
    gradle bootRun
    ```

`auth-server` should now be running on `http://localhost:8080`

### `backoffice`

#### Install `backoffice` dependencies

```zsh
cd frontend/cashier
npm install
```

#### Run `backoffice`

```zsh
cd frontend/backoffice
npm start
```

### `cashier`

#### Install `cashier` dependencies

```zsh
cd frontend/cashier
yarn install
```

#### Run `cashier`

```zsh
cd frontend/cashier
yarn start
```

### `online-store`

#### Install `online-store` dependencies

```zsh
cd frontend/online-store
npm install
```

#### Run `online-store`

```zsh
cd frontend/online-store
npm start
```

## Local Deployment (containerized, non-`docker-compose`)

### `auth-server`

#### Build `auth-server` image

(Make sure `auth-server` has already been built via `gradle build`)

```zsh
cd auth-server
docker build -t scaleforce172/auth-server .
```

#### Create `auth-server` container

- Create a dedicated network for our `auth-server` and `mysql` containers

    ```zsh
    docker network create --driver bridge scaleforce
    ```

- Create a MySQL container, connecting it to the `scaleforce`
network.

    ```zsh
    docker run --name mysql -td \
    --network scaleforce \
    -p 3306:3306 \
    -e MYSQL_ROOT_PASSWORD=cmpe172 \
    mysql:8.0
    ```

- Initialize MySQL using `auth-server/scripts/schema.sql`
  - Easiest way is via MySQL Workbench
- Run `auth-server`

    ```zsh
    docker run --name auth-server -td \
    --network scaleforce \
    -p 8080:8080 \
    -e "MYSQL_HOST=mysql" \
    scaleforce172/auth-server
    ```

`auth-server` should now be running on `http://localhost:8080`

### `backoffice`

#### Build `backoffice` image

```zsh
cd frontend/backoffice
docker build -t scaleforce172/backoffice .
```

#### Create `backoffice` container

```zsh
docker run --name backoffice -itd \
--network="host" \
-v ${PWD}:/app \
-v /app/node_modules \
scaleforce172/backoffice
```

`backoffice` should now be running on `http://localhost:3000`

### `cashier`

#### Build `cashier` image

```zsh
cd frontend/cashier
docker build -t scaleforce172/cashier .
```

#### Create `cashier` container

```zsh
docker run --name cashier -itd \
--network="host" \
-v ${PWD}:/app \
-v /app/node_modules \
scaleforce172/cashier
```

`cashier` should now be running on `http://localhost:3000`

### `online-store`

#### Build `online-store` image

```zsh
cd frontend/online-store
docker build -t scaleforce172/online-store .
```

#### Create `online-store` container

```zsh
docker run --name online-store -itd \
--network="host" \
-v ${PWD}:/app \
-v /app/node_modules \
scaleforce172/online-store
```

`online-store` should now be running on `http://localhost:3000`
