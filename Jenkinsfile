pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }
        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Sonar Analysis') {
            environment {
                scannerHome = tool 'SONAR_SCANNER'
            }
            steps {
                withSonarQubeEnv('SONAR_LOCAL') {
                    sh "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=DeployBack -Dsonar.host.url=http://localhost:9000 -Dsonar.login=4b7368ebb9d8618732bc2923430e53de4043d491 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn/**,**/src/test/**,**/model/**,**Application.java"
                }
            }
        }
        // stage("Quality Gate") {
        //     steps {
        //         timeout(time: 1, unit: 'MINUTES') {
        //             sleep(10)
        //             waitForQualityGate abortPipeline: true
        //         }
        //     }
        // }
        stage("Quality Gate") {
            steps {
                waitForQualityGate webhookSecretId: 'webhooksonar'
            }
        }
        stage("Deploy Backend") {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'TomcatLogin', path: '', url: 'http://localhost:8001/')], contextPath: '/tasks-backend', onFailure: false, war: 'target/tasks-backend.war'
            }
        }
    }
}

