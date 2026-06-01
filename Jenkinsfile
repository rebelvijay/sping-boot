pipeline {
agent { label 'build' }

environment {
    JAVA_HOME = "/usr/lib/jvm/java-21-openjdk-amd64"
    PATH = "${JAVA_HOME}/bin:${PATH}"
    IMAGE_NAME = "springboot-demo"
}

stages {

    stage('Checkout') {
        steps {
            git branch: 'master',
                credentialsId: 'GitLabCred',
                url: 'https://gitlab.com/venkatarajukotikilapudi/springboot.git'
        }
    }

    stage('Verify Environment') {
        steps {
            sh '''
            java -version
            mvn -version
            '''
        }
    }

    stage('Clean Build') {
        steps {
            echo "Compiling application..."
            sh 'mvn clean compile'
        }
    }

    stage('Unit Test') {
        steps {
            echo "Running JUnit tests..."
            sh 'mvn test'
        }
    }

    stage('Code Coverage (JaCoCo)') {
        steps {
            echo "Generating JaCoCo report..."
            sh 'mvn jacoco:report'
        }
    }

    stage('Package Jar') {
        steps {
            echo "Building executable JAR..."
            sh 'mvn package -DskipTests'
        }
    }

    stage('SonarQube Analysis') {
        steps {
            withSonarQubeEnv('mysonarqube') {
                sh '''
                mvn sonar:sonar \
                -Dsonar.projectName=springboot-demo \
                -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                '''
            }
        }
    }

    stage('Quality Gate') {
        steps {
            timeout(time: 2, unit: 'MINUTES') {
                script {
                    def qg = waitForQualityGate()

                    if (qg.status != 'OK') {
                        error "Pipeline aborted due to Quality Gate failure: ${qg.status}"
                    }
                }
            }
        }
    }

    stage('Build Docker Image') {
        steps {
            script {
                docker.build("${IMAGE_NAME}:latest")
            }
        }
    }

    stage('Smoke Test') {
        steps {
            sh '''
            docker rm -f springboot-app || true

            docker run -d \
              --name springboot-app \
              -p 8081:8081 \
              springboot-demo:latest

            sleep 20

            curl http://localhost:8081

            docker stop springboot-app
            docker rm -f springboot-app
            '''
        }
    }
}

post {

    success {
        echo 'Pipeline SUCCESS'
    }

    failure {
        echo 'Pipeline FAILED'
    }

    always {
        cleanWs()
    }
}

}
