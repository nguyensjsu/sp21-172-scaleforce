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

### `backend`

To run `backend`, `auth-server` must be running in a container. (See Local
Deployment (containerized, non-`docker-compose`)).

Once `auth-server` is running in a container, use `gradle` to run `backend` as
usual.

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
    -p 8099:8080 \
    -e "MYSQL_HOST=mysql" \
    scaleforce172/auth-server
    ```

`auth-server` should now be running on `http://localhost:8099`

### `backend`

#### Build `backend` image

(Make sure `backend` has already been built via `gradle build`)

```zsh
cd backend
docker build -t scaleforce172/backend .
```

#### Create `backend` container

- Run `backend`

    ```zsh
    docker run --name backend -td \
    --network scaleforce \
    -p 8080:8080 \
    -e "MYSQL_HOST=mysql" \
    scaleforce172/backend
    ```

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

## Cloud Deployment

### `auth-server`

#### Cloud SQL

To store data for `auth-server`, we use an instance of MySQL provided by Cloud
SQL. When creating the instance, we reuse the password used for our local
deployment for consistency. We use version 8.0 (same as our local deployment).
We choose the region closest to us (`us-west2 (Los Angeles)`) and opt for single
zonal availability, though for production, we would use multiple zones to
increase availability. We customize our instance by choosing minimum specs,
though for production, we might need beefier specs. We enable a private IP. We
disable backups.

Once our instance is provisioned, we whitelist our personal IPs in order to
access the instance through Workbench. Alternatively, administration can be
performed via Cloud Shell. Finally, we initialize the instance based on
`auth-server/scripts/schema.sql`.

#### Google Kubernetes Engine

To prepare first push the latest version of `auth-server` to Docker Hub:

```zsh
docker push scaleforce172/auth-server
```

##### Creating the Initial Deployment and ClusterIP

We then spin up a standard Kubernetes cluster, setting the zone to some zone on
the West coast. We connect our cluster to our Cloud SQL instance based on
[Connecting from Google Kubernetes
Engine](https://cloud.google.com/sql/docs/mysql/connect-kubernetes-engine#private-ip).
For simplicity, we connect without using the Cloud SQL Auth proxy, though for
production, the proxy is recommended due to better security features.

We create a `auth-server-deployment.yml` to describe out `auth-server`
Deployment and a `auth-server-service.yml` to describe our ClusterIP Service
used to create a stable internal IP address that clients can send request to to
hit the Deployment. We spin up our Deployment and ClusterIP:

```bash
kubectl create -f auth-server-deployment.yml --save-config
kubectl create -f auth-server-service.yml
```

To test endpoints within the cluster network, we use a jumpbox. The workload is
defined in `jumpbox.yml` and we run it as follows:

```bash
kubectl create -f jumpbox.yml
```

To get a shell, we run the following:

```bash
kubectl exec -it jumpbox -- /bin/bash
```

Finally, we install our favorite utilities:

```bash
apt update && apt install -y dnsutils vim tmux wget gnupg watch httpie
```

##### Kong Ingress

To serve `auth-server` publicly, we use [Kong
Ingress](https://docs.konghq.com/kubernetes-ingress-controller/1.2.x/deployment/gke/).

We deploy the initial ingress controller:

```bash
kubectl create -f https://bit.ly/k4k8s
```

We then set up our ingress rules and strip paths:

```bash
kubectl apply -f auth-server-ingress-rules.yml
kubectl apply -f auth-server-strip-path.yml
kubectl patch ingress auth-server -p  '{"metadata":{"annotations":{"konghq.com/override":"kong-strip-path"}}}'
```

##### Google Domains

To provide an alternative to using the IP of our ingress directly, we can use
our own domain. After obtaining `scaleforce.dev`, we create a Type A resource
record with the subdomain `auth`, leaving the TTL to the default of 1 hour and
setting the data to the IP address of our ingress. The end result is a publicly
accessible `http://auth.scaleforce.dev/`.

##### HTTPS

We install
[cert-manager](https://cert-manager.io/docs/installation/kubernetes/#installing-with-regular-manifests):

```bash
kubectl apply -f https://github.com/jetstack/cert-manager/releases/download/v1.3.1/cert-manager.yaml
```

Then, we get our [TLS
certificate](https://docs.konghq.com/kubernetes-ingress-controller/1.1.x/guides/cert-manager/#request-tls-certificate-from-lets-encrypt).

When setting up the ClusterIssuer, we use the following command:

```bash
echo "apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: letsencrypt-prod
  namespace: cert-manager
spec:
  acme:
    email: patrickjohnsilvestre@gmail.com
    privateKeySecretRef:
      name: letsencrypt-prod
    server: https://acme-v02.api.letsencrypt.org/directory
    solvers:
    - http01:
        ingress:
          class: kong" | kubectl apply -f -
clusterissuer.cert-manager.io/letsencrypt-prod configured
```

(Note the difference versus the Kong Docs for `apiVersion`)

We provision our certificate and use it:

```bash
echo '
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: backend-scaleforce-dev
  annotations:
    kubernetes.io/tls-acme: "true"
    cert-manager.io/cluster-issuer: letsencrypt-prod
    kubernetes.io/ingress.class: kong
spec:
  tls:
  - secretName: backend-scaleforce-dev
    hosts:
    - api.scaleforce.dev
  rules:
  - host: api.scaleforce.dev
    http:
      paths:
      - path: /
        backend:
          serviceName: backend-clusterip
          servicePort: 80
' | kubectl apply -f -
ingress.extensions/backend-scaleforce-dev configured
```

(Note: issuing takes between 30 minutes to an hour)

### `backend`

Pretty much the same as `auth-server`, though in relevant scripts, substitute
`auth-server` for `backend`.

### `back-office`, `cashier`, `online-store`

We can deploy `back-office` and our other React-based frontend apps on Heroku
based on the instructions in [Heroku Buildpack for create-react-app
](https://github.com/mars/create-react-app-buildpack#usage). One caveat: this
method requires that each frontend app have its own git repository.
