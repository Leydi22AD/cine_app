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
                // CORREGIDO: Usar la rama 'master'
                git branch: 'master', url: 'https://github.com/Leydi22AD/cine_app.git'
            }
        }

        stage('Build Backend & Analyze') {
            steps {
                // CORREGIDO: Usar el nombre de carpeta correcto 'ProyectLP2'
                dir('ProyectLP2') {
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
                // CORREGIDO: Usar el nombre de carpeta correcto 'Lp2-Frontend'
                dir('Lp2-Frontend') {
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
                    sh "docker-compose -f docker-compose.yml -p ${env.DOCKER_PROJECT_NAME} down -v --remove-orphans || true"
                    sh "docker-compose -f docker-compose.yml -p ${env.DOCKER_PROJECT_NAME} up -d --build"
                }
            }
        }
    }

    post {
        always {
            echo '🏁 Pipeline finalizado.'
            // CORREGIDO: Usar la ruta correcta para los resultados de las pruebas
            junit allowEmptyResults: true, testResults: 'ProyectLP2/target/surefire-reports/*.xml'
        }
        success {
            echo '🎉 ¡Éxito! El pipeline se completó correctamente.'
        }
        failure {
            echo '💥 ¡Fallo! El pipeline ha encontrado un error.'
        }
    }
}
