# Setup Cluster

##### Create TLS Certificate for your cluster NGINX:

```
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout $HOME/bin/tls.key -out $HOME/bin/tls.crt -subj "/C=IN/ST=Maharashtra/L=Pune/O=k8-cluster IO/CN=k8m.io"
```

##### Install NGINX: (Layer 4 LoadBalancer)

Process for installing nginx on host machine and then pass request to NGINX ingress controller.

```
sudo apt-get install -y nginx
sudo systemctl enable --now nginx

sudo mkdir /etc/nginx/k8m /etc/pki /etc/pki/nginx /etc/pki/nginx/private
sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/pki/nginx/private/server.key -out /etc/pki/nginx/server.crt -subj "/C=IN/ST=Maharashtra/L=Pune/O=k8-cluster IO/CN=k8m.io"
```

Open `nginx.conf` and edit as below:

```
sudo vi /etc/nginx/nginx.conf
```

add ` include /etc/nginx/k8m/k8m.conf; `

Finally create k8m.conf with our cluster forward rules for NGINX, this will be first Layer 4 LoadBalancer .

```
sudo cat > /etc/nginx/k8m/k8m.conf << EOF

map $http_upgrade $connection_upgrade {
        default upgrade;
        '' close;
}

server {
   listen         80;
   return 301 https://$host$request_uri;
}

upstream websocket {
        server localhost:9090;
}

server {
   listen       80 http2;
   server_name  api.gce.k8m.io;
   location / {
     proxy_pass http://localhost:8443/;
     proxy_set_header Host $host;
     proxy_set_header X-Real-IP $remote_addr;
     proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
     proxy_set_header X-Forwarded-Proto $scheme;
   }
}

server {
   listen       443 ssl http2;
   server_name  api.gce.k8m.io;
   location / {
     proxy_pass https://localhost:6443/;
     proxy_set_header Host $host;
     proxy_set_header X-Real-IP $remote_addr;
     proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
     proxy_set_header X-Forwarded-Proto $scheme;
   }
}

server {
   listen       443 ssl http2;
   server_name  cockpit.gcp.k8m.io;
   #return 301 $scheme://$server_name:9090$request_uri;
   location / {
     proxy_pass http://websocket;
                proxy_http_version 1.1;
                proxy_buffering off;
                proxy_set_header X-Real-IP  $remote_addr;
                proxy_set_header Host $host;
                proxy_set_header X-Forwarded-For $remote_addr;

                # needed for websocket
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection $connection_upgrade;
                # change scheme of "Origin" to http
                proxy_set_header Origin http://$host;

                gzip off;
                add_header X-Frame-Options "SAMEORIGIN";
   }
}

server {
   listen       443 ssl http2 default_server;
   server_name  *.k8m.io;
   root         /usr/share/nginx/html;
   index index.html index.htm;
   ssl_certificate "/etc/pki/nginx/server.crt";
   ssl_certificate_key "/etc/pki/nginx/private/server.key";
   location / {
     proxy_pass https://localhost:30443/;
     proxy_set_header Host $host;
     proxy_set_header X-Real-IP $remote_addr;
     proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
     proxy_set_header X-Forwarded-Proto $scheme;
   }
}

EOF

sudo systemctl restart nginx

```

##### Initialize the Helm Environment with Tiller Pods:

```
kubectl create serviceaccount --namespace kube-system tiller
kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller
helm init --service-account tiller --upgrade
```

##### Create NGINX Deployment for ingress controller: (Layer 7 LoadBalancer)

Use helm with `LoadBalancer` for gce and `NodePort` for local cluster.

(Might `NodePort` work with gce as well??? open 80/443 on gce master and install nginx on it, proxy_pass to localhost:30080/30443)

```
kubectl create clusterrolebinding nginx --clusterrole cluster-admin --serviceaccount=nginx:default

helm install --name nginx  --namespace nginx stable/nginx-ingress --set controller.service.type=NodePort --set controller.service.nodePorts.https=30443 --set controller.service.nodePorts.http=30080

or

helm install --name nginx --namespace nginx stable/nginx-ingress --set controller.service.type=LoadBalancer
```

##### Test with echoservers:
Check if everything working as expected with echoservers:

```
kubectl --namespace dev run echoserver --image=gcr.io/google_containers/echoserver:1.10 --port=8080
kubectl expose deployment echoserver --namespace dev --type=NodePort
kubectl create -f $HOME/bin/echoserver.ingress.yml
```

##### Install Jenkins:

Jenkins need PVC and PV working, so need to have storageClass set and working.

Custom Clusters will not have this working out of box.

```
kubectl create clusterrolebinding  --namespace jenkins jenkins --clusterrole cluster-admin --serviceaccount=jenkins:default
helm install --name jenkins --namespace jenkins stable/jenkins --Master.ServiceType=NodePort
```
