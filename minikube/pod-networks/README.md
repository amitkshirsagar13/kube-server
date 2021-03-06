# Start kubeadm setup:

#### To Reset Kubernetes cluster:
```
sudo kubeadm reset
```

If hostname reset required use below:

```
sudo hostname k8m.io
sudo vi /etc/hostname
sudo vi /etc/hosts
sudo vi /etc/dnsmasq.conf
sudo /etc/init.d/dnsmasq restart
``` 


If want to use gce as cloud
Add `--cloud-provider=gce` to below file:


`
sudo vi /etc/systemd/system/kubelet.service.d/10-kubeadm.conf
`

Depending on Network for pod your arguments might change
- Weave:
```
sudo kubeadm init --service-dns-domain=k8m.io
 or
sudo kubeadm init --service-dns-domain=k8m.io --apiserver-advertise-address=<IPADDRESS>
```
Copy certs to allow kubectl command work properly.
```
mkdir -p $HOME/.kube
sudo rm $HOME/.kube/config
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```
Initialize the weave pod network.
```
export kubever=$(kubectl version | base64 | tr -d '\n')
kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$kubever"
```
- Flannel
```
sudo kubeadm init --pod-network-cidr=10.244.0.0/16
mkdir -p $HOME/.kube
sudo rm $HOME/.kube/config
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/v0.9.1/Documentation/kube-flannel.yml
```

Join with other nodes:
```
sudo kubeadm token create --print-join-command

and

sudo kubeadm join 10.142.0.2:6443 --token pt9pmy.oxw9sumuudub4p0d --discovery-token-ca-cert-hash sha256:4769291430a8655f2558f043856cb10d3fa3e9a3857bdb6a8914a2c155f51ff4
```

##### If you want to mark master as worker use below:

```
kubectl taint nodes --all node-role.kubernetes.io/master-
```

##### Weave Cloud Monitoring
```
curl -Ls https://get.weave.works | sh -s -- --token=895amogho8w68kbd9m1c78tt7a5mfyamfemeq
or
helm install --name weave-cloud --namespace kube-system --set ServiceToken=895amogho8w68kbd9m1c78tt7a5mfyamfemeq stable/weave-cloud
```


Some useful commands:
```
helm delete --purge kubedash
helm delete --purge weave-scope
helm install stable/weave-scope --name weave-scope --namespace kube-system
helm install stable/kubernetes-dashboard --name kubedash --namespace kube-system
```

## [Setup LoadBalancers Layer 4/7](https://github.com/amitkshirsagar13/kube-server/blob/master/minikube/bin/README.md)



getStorageClass: `kubectl get storageclass`

URL for accessing [Hystrix Turbine Stream](https://kube-hystrix.master.k8m.io/hystrix/monitor?stream=http://KUBE-HYSTRIX:8080/turbine.stream)

https://kube-hystrix.master.k8m.io/hystrix/monitor?stream=http://KUBE-HYSTRIX:8080/turbine.stream
