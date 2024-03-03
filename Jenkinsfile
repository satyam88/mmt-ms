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
                echo 'Creating Jar Artifact with auto-incrementing version'
                sh 'mvn versions:set -DgenerateBackupPoms=false -DnewVersion=1.0.${BUILD_NUMBER} && mvn clean package'
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
        stage('Docker Image Push to Amazon ECR') {
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
        stage('Delete Docker Image from Local') {
            steps {
                script {
                    sh "docker rmi mmt-ms:dev-mmt-ms-v.1.${BUILD_NUMBER}"
                }
            }
        }
        stage('Deploy app to dev env') {
            when {
                expression {
                    env.BRANCH_NAME == 'dev'
                }
            }
            steps {
                script {
                    def devenvManifest = 'kubernetes/dev/'
                    def yamlFile = 'kubernetes/dev/05-deployment.yaml'
                    sh "sed -i 's/<latest>/dev-booking-v.1.${BUILD_NUMBER}/g' ${yamlFile}"
                    sh "kubectl apply -f ${devenvManifest}"
                }
            }
        }
        stage('Deploy app to preprod env') {
            when {
                expression {
                    env.BRANCH_NAME == 'preprod'
                }
            }
            steps {
                script {
                    sh 'kubectl apply -f kubernetes/preprod'
                }
            }
        }
        stage('Deploy app to prod env') {
            when {
                expression {
                    env.BRANCH_NAME == 'prod'
                }
            }
            steps {
                script {
                    sh 'kubectl apply -f kubernetes/prod'
                }
            }
        }
    }
}
