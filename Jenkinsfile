pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME' // Asegúrate de que 'MAVEN_HOME' esté configurado en Jenkins
    }

    environment {
        DOCKER_PROJECT_NAME = 'cine_app'
    }

    stages {
        stage('Clone Monorepo') {
            steps {
                echo '🧹 Limpiando y clonando el repositorio principal...'
                cleanWs()
                // Clonar el repositorio único que contiene backend y frontend
                git branch: 'main', url: 'https://github.com/Leydi22AD/cine_app.git'
            }
        }

        stage('Build Backend & Analyze') {
            steps {
                // Ejecutar los comandos dentro de la subcarpeta del backend
                dir('backend_cine') {
                    echo '🔨 Construyendo el backend...'
                    sh 'mvn clean verify -Dspring.profiles.active=test'

                    echo '📊 Analizando el código del backend con SonarQube...'
                    withSonarQubeEnv('sonarqube') { // Asegúrate de que 'sonarqube' esté configurado en Jenkins
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                // Ejecutar los comandos dentro de la subcarpeta del frontend
                dir('frotend_cine') {
                    echo '🎨 Construyendo el frontend...'
                    sh 'npm install'
                    sh 'npm run build -- --configuration production'
                }
            }
        }

        stage('Deploy Application with Docker Compose') {
            steps {
                echo '🚀 Desplegando la aplicación completa...'
                script {
                    // El docker-compose.yml en la raíz ya sabe dónde encontrar cada Dockerfile
                    sh "docker-compose -f docker-compose.yml -p ${env.DOCKER_PROJECT_NAME} down -v --remove-orphans || true"
                    sh "docker-compose -f docker-compose.yml -p ${env.DOCKER_PROJECT_NAME} up -d --build"
                }
            }
        }
    }

    post {
        always {
            echo '🏁 Pipeline finalizado.'
            // Recolectar resultados de pruebas del backend
            junit allowEmptyResults: true, testResults: 'backend_cine/target/surefire-reports/*.xml'
        }
        success {
            echo '🎉 ¡Éxito! El pipeline se completó correctamente.'
        }
        failure {
            echo '💥 ¡Fallo! El pipeline ha encontrado un error.'
        }
    }
}
