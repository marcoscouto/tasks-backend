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
        stage("Quality Gate") {
            steps {
                timeout(time: 1, unit: 'MINUTES') {
                    sleep(5)
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage("Deploy Backend") {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'TomcatLogin', path: '', url: 'http://localhost:8001/')], contextPath: '/tasks-backend', onFailure: false, war: 'target/tasks-backend.war'
            }
        }
        stage("API Test - Rest Assured") {
            steps {
                dir('api-test'){
                    git branch: 'main', url: 'https://github.com/marcoscouto/tasks-api-test'
                    sh 'mvn test'
                }
            }
        }
        stage("Deploy Frontend") {
            steps {
                dir('frontend'){
                    git branch: 'master', url: 'https://github.com/marcoscouto/tasks-frontend'
                    sh 'mvn clean package'
                    deploy adapters: [tomcat8(credentialsId: 'TomcatLogin', path: '', url: 'http://localhost:8001/')], contextPath: '/tasks', onFailure: false, war: 'target/tasks.war'
                }
            }
        }
        stage("Functional Test - Selenium") {
            steps {
                dir('functional-test'){
                    git branch: 'main', url: 'https://github.com/marcoscouto/tasks-functional-tests'
                    sh 'mvn test'
                }
            }
        }
        // stage("Deploy Prod") {
        //     steps {
        //         sh 'docker-compose build'
        //         sh 'docker-compose up -d'
        //     }
        // }
    }
    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, api-test/target/surefire-reports/*.xml, functional-test/target/surefire-reports/*.xml'
        }
    }
}

