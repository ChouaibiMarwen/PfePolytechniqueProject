pipeline {
    agent any

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-11-openjdk-amd64/bin/java'
    }

    stages {
        stage('Debug') {
            steps {
                sh 'echo $JAVA_HOME'
            }
        }
        stage('Checkout Project') {
            steps {
                checkout scm
            }
        }
        stage('Clean and Package') {
            steps {
                script {
                    sh "sudo mvn clean package"
                }
            }
        }
        stage('Reload Daemon') {
            steps {
                sh "echo 'dwkaH5t7aM49' | sudo -S systemctl daemon-reload"
            }
        }
        stage('Start Daemon') {
            steps {
                sh "echo 'dwkaH5t7aM49' | sudo -S systemctl start rayaserver"
            }
        }
        stage('Enable Daemon') {
            steps {
                sh "echo 'dwkaH5t7aM49' | sudo -S systemctl enable rayaserver"
            }
        }
        stage('Restart Daemon') {
            steps {
                sh "echo 'dwkaH5t7aM49' | sudo -S systemctl restart rayaserver"
            }
        }
    }
}

