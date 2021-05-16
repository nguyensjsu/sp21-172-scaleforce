# DEPLOYMENT

## Local Deployment (minimum container usage)

### Back End

#### `auth-server`

- Create a MySQL container:

  ```zsh
  docker run --rm --name mysql -td \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=cmpe172 \
  mysql:8.0
  ```

- Initialize MySQL using `auth-server/scripts/schema.sql`
- Build, run `auth-server`:

  ```zsh
  cd auth-server
  gradle build -x test
  gradle bootRun
  ```

- `auth-server` should now be running on `http://localhost:8099`

#### `backend`

- (run `auth-server`)

- Build, run `backend`:

  ```zsh
  cd backend
  gradle build -x test
  gradle bootRun
  ```

- `backend` should now be running on `http://localhost:8080`

### Front End

### `backoffice`, `cashier`, and `online-store`

#### Install `{APP_NAME}` dependencies

```zsh
cd frontend/{APP_NAME}
yarn install
```

#### Run `{APP_NAME}`

```zsh
cd frontend/{APP_NAME}
yarn start
```

## Local Deployment (containers)

### Back End

- Create a dedicated network for all containers:

  ```zsh
  docker network create --driver bridge scaleforce
  ```

- Create a MySQL container, connecting it to the `scaleforce` network:

  ```zsh
  docker run --rm --name mysql -td \
  --network scaleforce \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=cmpe172 \
  mysql:8.0
  ```

- Initialize MySQL using `auth-server/scripts/schema.sql`

#### `auth-server`

- Build `auth-server`:

  ```zsh
  cd auth-server
  gradle build -x test
  ```

- Build docker image:

  ```zsh
  docker build -t scaleforce172/auth-server .
  ```

- Run `auth-server`:

  ```zsh
  docker run --rm --name auth-server -td \
  --network scaleforce \
  -p 8099:8099 \
  -e "MYSQL_HOST=mysql" \
  scaleforce172/auth-server
  ```

- `auth-server` should now be running on `http://localhost:8099`

### `backend`

#### Build `backend` image

- Build `back-end`:

  ```zsh
  cd backend
  gradle build -x test
  ```

- Build docker image:

  ```zsh
  cd backend
  docker build -t scaleforce172/backend .
  ```

- Run `backend`

  ```zsh
  docker run --rm --name backend -td \
  --network scaleforce \
  -p 8080:8080 \
  -e "MYSQL_HOST=mysql" \
  scaleforce172/backend
  ```

- `backend` should now be running on `http://localhost:8080`

## Cloud Deployment

### Back End

#### `auth-server`

##### Cloud SQL

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
performed via Cloud Shell. **Finally, we initialize the instance based on
`auth-server/scripts/schema.sql`.**

##### Google Kubernetes Engine

We first push the latest version of `auth-server` to Docker Hub:

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

We create a `auth-server-deployment.yml` to describe our `auth-server`
Deployment and a `auth-server-service.yml` to describe our ClusterIP Service
used to create a stable internal IP address that clients can send requests to to
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
  name: auth-scaleforce-dev
  annotations:
    kubernetes.io/tls-acme: "true"
    cert-manager.io/cluster-issuer: letsencrypt-prod
    kubernetes.io/ingress.class: kong
spec:
  tls:
  - secretName: auth-scaleforce-dev
    hosts:
    - auth.scaleforce.dev
  rules:
  - host: auth.scaleforce.dev
    http:
      paths:
      - path: /
        backend:
          serviceName: auth-server-clusterip
          servicePort: 80
' | kubectl apply -f -
ingress.extensions/backend-scaleforce-dev configured
```

(Note: issuing takes between 30 minutes to an hour)

#### `backend`

Pretty much the same as `auth-server`, though in relevant scripts, substitute
`backend` for `auth-server`.

### Front End

TODO
