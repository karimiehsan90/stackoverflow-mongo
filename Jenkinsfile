pipeline {
    agent any

    stages {
        stage('Unit test') {
            steps {
                echo "not implemented"
            }
        }

        stage('Build') {
            steps {
                sh './stackoverflow.sh build'
            }
        }

        stage('Prepare Env') {
            steps {
                echo "not implemented"
            }
        }

        stage('Acceptance test') {
            steps {
                echo "not implemented"
            }
        }

        stage('Stress test') {
            steps {
                echo "not implemented"
            }
        }
    }

    post {
        always {
            cleanWs()
            sh "docker system prune -f"
        }
    }
}
