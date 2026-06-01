pipeline {
    agent { label 'build' }

    environment {
        JAVA_HOME = "/usr/lib/jvm/java-21-openjdk"
        PATH = "$JAVA_HOME/bin:$PATH"
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

        stage('Clean Build') {
            steps {
                echo "Cleaning and building project..."
                sh 'mvn clean compile'
            }
        }

        stage('Unit Test') {
            steps {
                echo "Running tests..."
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
                echo "Building final JAR..."
                sh 'mvn package -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "Running SonarQube..."
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
                script {
                    timeout(time: 2, unit: 'MINUTES') {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline failed due to Quality Gate: ${qg.status}"
                        }
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("${IMAGE_NAME}:latest")
                }
            }
        }

        stage('Run Container (Smoke Test)') {
            steps {
                sh '''
                docker rm -f springboot-app || true
                docker run -d --name springboot-app -p 8081:8081 springboot-demo:latest
                sleep 20
                curl http://localhost:8081/hello
                docker stop springboot-app
                '''
            }
        }
    }

    post {
        always {
            echo "Cleaning workspace..."
            cleanWs()
        }

        success {
            echo "Pipeline SUCCESS 🚀"
        }

        failure {
            echo "Pipeline FAILED ❌"
        }
    }
}