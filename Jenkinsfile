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
                sh "./stackoverflow.sh prepare-env"
            }
        }

        stage('Acceptance test') {
            steps {
                sh "./stackoverflow.sh acceptance-test"
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
