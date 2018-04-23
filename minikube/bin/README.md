# Setup Cluster

##### Create TLS Certificate for your cluster NGINX:

```
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout $HOME/bin/tls.key -out $HOME/bin/tls.crt -subj "/C=IN/ST=Maharashtra/L=Pune/O=k8-cluster IO/CN=k8m.io"
```

##### Install NGINX:

Process for installing nginx on host machine and then pass request to NGINX ingress controller.


##### Initialize the Helm Environment with Tiller Pods:

```
kubectl create serviceaccount --namespace kube-system tiller
kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller
helm init --service-account tiller --upgrade
```

##### Create NGINX Deployment for ingress controller:

Use helm with `LoadBalancer` for gce and `NodePort` for local cluster.

(Might `NodePort` work with gce as well??? open 80/443 on gce master and install nginx on it, proxy_pass to localhost:30080/30443)

```
kubectl create clusterrolebinding nginx --clusterrole cluster-admin --serviceaccount=nginx:default
helm install --name nginx --namespace nginx stable/nginx-ingress --set controller.service.type=LoadBalancer
or
helm install --name nginx  --namespace nginx stable/nginx-ingress --set controller.service.type=NodePort --set controller.service.nodePorts.https=30443 --set controller.service.nodePorts.http=30080
```

##### Test with echoservers:
Check if everything working as expected with echoservers:

```
kubectl --namespace dev run echoserver --image=gcr.io/google_containers/echoserver:1.4 --port=8080
kubectl expose deployment echoserver --type=NodePort --namespace dev
kubectl create -f $HOME/bin/echoserver.ingress.yml
```

##### Install Jenkins:

Jenkins need PVC and PV working, so need to have storageClass set and working.

Custom Clusters will not have this working out of box.

```
kubectl create clusterrolebinding  --namespace jenkins jenkins --clusterrole cluster-admin --serviceaccount=jenkins:default
helm install --name jenkins --namespace jenkins stable/jenkins --Master.ServiceType=NodePort
```
