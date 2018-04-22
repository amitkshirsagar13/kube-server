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
