# Kubernetes Util Helper Package
```
curl https://raw.githubusercontent.com/amitkshirsagar13/kube-server/master/minikube/dnsmasq | sudo sh
```

Install 7z Package extractor:

```
sudo apt-get update && sudo apt-get install p7zip-full -y
or
sudo dnf install p7zip -y
```
Run below in command prompt from Home directory
```
curl -Ls https://raw.githubusercontent.com/amitkshirsagar13/kube-server/master/minikube/deploy.sh |sed -e 's/\r$//' | sh
```

### Setup Kubeadm
Setup kubeadm, kubelete, docker and kubernetes-cni.

```
Setup kubeadm for Master and Node
sudo curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add - \
&& echo "deb http://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee /etc/apt/sources.list.d/kubernetes.list \
&& sudo apt-get update && sudo apt-get install kubelet kubeadm kubernetes-cni docker.io -y && sudo apt autoremove -y \
&& sudo systemctl start docker && sudo systemctl enable docker
```

For Raspberry Pi enable cgroup memory and disable swap:
```
sudo vi /boot/cmdline.txt
cgroup_enable=memory cgroup_memory=1

sudo systemctl disable dphys-swapfile

```


#### Enable ApiServer access on insecure port

Enable Authentication using user/password for APIServer using insecure port.

```
sudo vi /etc/kubernetes/manifests/kube-apiserver.yaml
```
Add below to command and mount the password files.

```
    - --basic-auth-file=/etc/kubernetes/auth.csv
    - --authorization-rbac-super-user=admin

And

    volumeMounts:
    - mountPath: /etc/kubernetes/auth.csv
      name: kubernetes-dashboard
      readOnly: true
  volumes:
  - hostPath:
      path: /etc/kubernetes/auth.csv
    name: kubernetes-dashboard
```

Enable insecure port on 8443 and route trafic from host to kubernetes service using nginx and insecure port for enabling cabin

### Setup Cluster with kubeadm
## [Setup kubeadm](https://github.com/amitkshirsagar13/kube-server/tree/master/minikube/pod-networks/README.md)
