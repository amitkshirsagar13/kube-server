#!/bin/sh
cd
wget https://github.com/amitkshirsagar13/kube-server/raw/master/minikube/bin.7z
7z x bin.7z && rm bin.7z
chmod 755 -R ./bin/*
wget https://storage.googleapis.com/kubernetes-helm/helm-v2.8.2-linux-amd64.tar.gz && tar -zxvf helm-v2.8.2-linux-amd64.tar.gz && cp li*/helm . && rm -R lin* helm-v2.8.2-linux-amd64.tar.gz
mv helm ./bin/helm
PATH=$HOME/bin:$PATH
