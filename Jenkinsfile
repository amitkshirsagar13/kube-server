def label = "worker-${UUID.randomUUID().toString()}"


podTemplate(label: label, containers: [
  containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat'),
  containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true), 
  containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl:v1.8.8', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'helm', image: 'lachlanevenson/k8s-helm:latest', command: 'cat', ttyEnabled: true)

],
volumes: [
  hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
]) {
  node(label) {
  
	def profile = "dev"
    def myRepo = checkout scm
    def gitCommit = myRepo.GIT_COMMIT
    def gitBranch = myRepo.GIT_BRANCH
    def shortGitCommit = "${gitCommit[0..10]}"
    //def mvnTool = tool 'maven'
    def project = "basekube"
    def application = "kube-server"
    def dockerApp
    stage('Build Project') {
       echo "Building Project...$gitBranch:$shortGitCommit"
       container('maven') {
	        stage('Build a Maven project') {
	          sh "mvn -Dmaven.test.skip=true clean install"
	        }
	    }
    }    
    stage('Create Docker images and Push') {
      container('docker') {
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
          credentialsId: 'docker-hub-credentials',
          usernameVariable: 'DOCKER_HUB_USER',
          passwordVariable: 'DOCKER_HUB_PASSWORD']]) {
          sh """
            docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD}
            docker build -t amitkshirsagar13/$application:$shortGitCommit -t amitkshirsagar13/$application:latest .
	        docker push amitkshirsagar13/$application:$shortGitCommit
            docker push amitkshirsagar13/$application:latest
            """
        }
      }
    }
    stage('Deploy helm release') {
      echo "Project: $project | Application: $application | tag: $shortGitCommit"
      container('helm') {
    	sh "helm upgrade --install $application --namespace dev ./cicd/demo/ --set profile=$profile --set branch=$gitBranch --set commit=$shortGitCommit --set application=$application"
      }
    }
  }
}
