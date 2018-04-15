#!/bin/sh
cd
wget https://github.com/amitkshirsagar13/kube-server/raw/master/minikube/bin.7z && ls 
7z x bin.7z && rm bin.7z && ls
chmod 755 -R ./bin/* && ls
wget https://storage.googleapis.com/kubernetes-helm/helm-v2.8.2-linux-amd64.tar.gz && tar -zxvf helm-v2.8.2-linux-amd64.tar.gz && cp li*/helm . && rm -R lin* helm-v2.8.2-linux-amd64.tar.gz && ls
mv helm ./bin/helm && ls
PATH=$HOME/bin:$PATH && ls
