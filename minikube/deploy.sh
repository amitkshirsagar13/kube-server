#!/bin/sh
cd
echo "Step1"
wget https://github.com/amitkshirsagar13/kube-server/raw/master/minikube/bin.7z

echo "Step2"
7z x bin.7z && rm bin.7z

echo "Step3"
chmod 755 ./bin/*

echo "Step4"
wget https://storage.googleapis.com/kubernetes-helm/helm-v2.8.2-linux-amd64.tar.gz && tar -zxvf helm-v2.8.2-linux-amd64.tar.gz && cp li*/helm . && rm -R lin* helm-*

echo "Step5"
mv helm ./bin/helm

echo "Step7"
PATH=$HOME/bin:$PATH
