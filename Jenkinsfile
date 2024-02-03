pipeline {
    options {
        buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    }

    agent any

    tools {
        maven 'maven_3.9.4'
    }

    stages {
        stage('Code Compilation') {
            steps {
                echo 'Code Compilation is In Progress!'
                sh 'mvn clean compile'
                echo 'Code Compilation is Completed Successfully!'
            }
        }
        stage('Code QA Execution') {
            steps {
                echo 'Junit Test case check in Progress!'
                sh 'mvn clean test'
                echo 'Junit Test case check Completed!'
            }
        }
        stage('Code Package') {
            steps {
                echo 'Creating Jar Artifact'
                sh 'mvn clean package'
                echo 'Creating jar Artifact done'
            }
        }
        stage('Building & Tag Docker Image') {
            steps {
                echo 'Starting Building Docker Image'
                sh 'docker build -t mmt-ms:dev-mmt-ms-v.1.${BUILD_NUMBER} .'
                echo 'Completed  Building Docker Image'
            }
        }
        stage('Docker Image Scanning') {
            steps {
                echo 'Docker Image Scanning Started'
                sh 'java -version'
                echo 'Docker Image Scanning Started'
            }
        }
        stage(' Docker Image Push to Amazon ECR') {
           steps {
              script {
                 withDockerRegistry([credentialsId:'ecr:ap-south-1:ecr-credentials', url:"https://559220132560.dkr.ecr.ap-south-1.amazonaws.com/mmt-ms"]){
                 sh """
                 echo "List the docker images present in local"
                 docker images
                 echo "Tagging the Docker Image: In Progress"
                 docker tag mmt-ms:dev-mmt-ms-v.1.${BUILD_NUMBER} 559220132560.dkr.ecr.ap-south-1.amazonaws.com/mmt-ms:dev-mmt-ms-v.1.${BUILD_NUMBER}
                 echo "Tagging the Docker Image: Completed"
                 echo "Push Docker Image to ECR : In Progress"
                 docker push 559220132560.dkr.ecr.ap-south-1.amazonaws.com/mmt-ms:dev-mmt-ms-v.1.${BUILD_NUMBER}
                 echo "Push Docker Image to ECR : Completed"
                 """
                 }
              }
           }
        }
        stage('Upload the docker Image to Nexus') {
           steps {
              script {
                 withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]){
                 sh 'docker login http://13.201.193.145:8085/repository/mmt-ms/ -u admin -p ${PASSWORD}'
                 echo "Push Docker Image to Nexus : In Progress"
                 sh 'docker tag mmt-ms:dev-mmt-ms-v.1.${BUILD_NUMBER} 13.201.193.145:8085/mmt-ms:dev-mmt-ms-v.1.${BUILD_NUMBER}'
                 sh 'docker push 13.201.193.145:8085/mmt-ms:dev-mmt-ms-v.1.${BUILD_NUMBER}'
                 echo "Push Docker Image to Nexus : Completed"
                 }
              }
            }
        }
	}
}
