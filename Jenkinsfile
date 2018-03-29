def label = "worker-${UUID.randomUUID().toString()}"


podTemplate(label: label, containers: [
  containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true), 
  containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl:v1.8.8', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'helm', image: 'lachlanevenson/k8s-helm:latest', command: 'cat', ttyEnabled: true)

],
volumes: [
  hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
]) {
  node(label) {
  
	
    def myRepo = checkout scm
    def gitCommit = myRepo.GIT_COMMIT
    def gitBranch = myRepo.GIT_BRANCH
    def shortGitCommit = "${gitCommit[0..10]}"
    def mvnTool = tool 'maven3.5.3'
    //def dockerTool = tool name: 'docker', type: 'org.jenkinsci.plugins.docker.commons.tools.DockerTool'
    def project = "basekube"
    def application = "demo-server"
    def dockerApp
    stage('Build Project') {
       echo "Building Project...$gitBranch:$shortGitCommit"

       // execute maven
       sh "${mvnTool}/bin/mvn -Dmaven.test.skip=true clean install"
       
    }    
    stage('Create Docker images and Push') {
       echo "Project: $project | Application: $application | tag: $shortGitCommit"
      container('docker') {
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
          credentialsId: 'docker-hub-credentials',
          usernameVariable: 'DOCKER_HUB_USER',
          passwordVariable: 'DOCKER_HUB_PASSWORD']]) {
          sh """
            docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD}
            docker build -t amitkshirsagar13/$application:$shortGitCommit -t amitkshirsagar13/base-demo:latest  -t amitkshirsagar13/$application:latest .
	    docker push amitkshirsagar13/$application:$shortGitCommit
            docker push amitkshirsagar13/$application:latest
            docker push amitkshirsagar13/base-demo:latest
            """
        }
      }
    }
    stage('Deploy application') {
       echo "Deploying application...."
       def namespace = "$gitBranch"
       //sh "helm list"
       //sh "helm upgrade --install --namespace --set commit=$shortGitCommit $namespace cicd/deploy"
    }
    stage('Run kubectl') {
      container('kubectl') {
        sh "kubectl get pods"
      }
    }
    stage('Run helm') {
      container('helm') {
        sh "helm list"
        sh "pwd"
        sh "ls cicd"
	sh "helm install --name $application --namespace dev ./cicd/demo/ --set branch=dev --set commit=latest --set application=$application"
      }
    }
  }
}
