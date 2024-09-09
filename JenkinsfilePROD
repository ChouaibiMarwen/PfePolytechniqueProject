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
                sh "echo 'Cloud2025' | sudo -S systemctl daemon-reload"
            }
        }
        stage('Start Daemon') {
            steps {
                sh "echo 'Cloud2025' | sudo -S systemctl start server"
            }
        }
        stage('Enable Daemon') {
            steps {
                sh "echo 'Cloud2025' | sudo -S systemctl enable server"
            }
        }
        stage('Restart Daemon') {
            steps {
                sh "echo 'Cloud2025' | sudo -S systemctl restart server"
            }
        }
    }
}
