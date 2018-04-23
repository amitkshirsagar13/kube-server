# Setup GlusterFS

Taint Master as Node and label it to run glusterfs.

```
kubectl taint nodes --all node-role.kubernetes.io/master-
kubectl label node k8m storagenode=glusterfs
```

#### GlusterFS Configuration
Create Disk/Devices using files:
```
dd if=/dev/zero of=/home/master/glusterimage bs=1M count=5120

sudo losetup /dev/loop0 /home/master/glusterimage
```
Enable GlusterFS Service
```
sudo cat > /etc/systemd/system/loopback_gluster.service << EOF
[Unit]
Description=Create the loopback device for GlusterFS
DefaultDependencies=false
Before=local-fs.target
After=systemd-udev-settle.service
Requires=systemd-udev-settle.service
[Service]
Type=oneshot
ExecStart=/usr/bin/bash -c "modprobe dm_thin_pool && [ -b /dev/loop0 ] || losetup /dev/loop0 /home/master/glusterimage"
[Install]
WantedBy=local-fs.target
EOF

sudo systemctl enable --now /etc/systemd/system/loopback_gluster.service

```
#### Configure Heketi

Go to Below location:
```
cd /home/master/git/heketi/extras/kubernetes

cat > heketi-secret.yml << EOF
---
apiVersion: v1
kind: Secret
metadata:
  name: heketi-config-secret
  namespace: default
data:
  # base64 encoded password. E.g.: echo -n "password" | base64
  key: cGFzc3dvcmQ=
type: kubernetes.io/glusterfs
EOF

cat > heketi-service.yml << EOF
---
kind: Service
apiVersion: v1
metadata:
  name: deploy-heketi
  labels:
    glusterfs: heketi-service
    deploy-heketi: service
  annotations:
    description: Exposes Heketi Service
spec:
  selector:
    deploy-heketi: pod
  type: NodePort
  ports:
  - name: deploy-heketi
    port: 8080
    targetPort: 8080
    nodePort: 30625
EOF

cat > heketi-config-secret.yml << EOF
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: heketi-config-secret
data:
  heketi.json: |
    {
      "name": "HEKETI_ADMIN_KEY",
      "valueFrom": {
        "secretKeyRef": {
          "name": "heketi-secret",
          "key": "key"
        }
      }
    }
EOF

kubectl create -f glusterfs-daemonset.json
kubectl create -f heteki-secret.yml
kubectl create -f heteki-config-secret.yml
kubectl create -f heketi-service-account.json
kubectl create -f heketi-bootstrap.json

```

Install Heketi Application:
```
HEKETI_BIN="heketi-cli"      # heketi or heketi-cli
HEKETI_VERSION="5.0.0"       # latest heketi version => https://github.com/heketi/heketi/releases
HEKETI_OS="linux"            # linux or darwin

curl -SL https://github.com/heketi/heketi/releases/download/v${HEKETI_VERSION}/heketi-v${HEKETI_VERSION}.${HEKETI_OS}.amd64.tar.gz -o /tmp/heketi-v${HEKETI_VERSION}.${HEKETI_OS}.amd64.tar.gz && \
tar xzvf /tmp/heketi-v${HEKETI_VERSION}.${HEKETI_OS}.amd64.tar.gz -C /tmp && \
rm -vf /tmp/heketi-v${HEKETI_VERSION}.${HEKETI_OS}.amd64.tar.gz && \
sudo cp /tmp/heketi/${HEKETI_BIN} /usr/local/bin/${HEKETI_BIN}_${HEKETI_VERSION} && \
rm -vrf /tmp/heketi && \
cd /usr/local/bin && \
sudo ln -vsnf ${HEKETI_BIN}_${HEKETI_VERSION} ${HEKETI_BIN} && cd &&\
unset HEKETI_BIN HEKETI_VERSION HEKETI_OS
```
