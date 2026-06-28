// 🚀 Jenkinsfile - Pipeline de CI/CD para CineApp (con DB para tests)
pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
        nodejs 'NodeJS' 
    }

    environment {
        DOCKER_PROJECT_NAME = 'cine_app'
        APP_CONTAINER_NAME = 'cine_app_backend'
        DB_CONTAINER_NAME = 'mysql_cine_app_db'
        DB_NAME = 'cine_app_db'
        DB_USER = 'root'
        DB_PASSWORD = 'admin123'
    }

    stages {
        stage('Clone') {
            steps {
                timeout(time: 30, unit: 'MINUTES') {
                    echo '🔄 === INICIO: CLONACIÓN DEL REPOSITORIO ==='
                    cleanWs()
                    checkout scm
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir('ProyectLP2') {
                    echo '🔨 === INICIO: CONSTRUCCIÓN DEL BACKEND ==='
                    sh 'mvn clean package -DskipTests'
                    echo '✅ === FIN: CONSTRUCCIÓN DEL BACKEND COMPLETADA ==='
                }
            }
        }
        
        stage('Build Frontend') {
            steps {
                dir('Lp2-Frontend') {
                    echo '🎨 === INICIO: CONSTRUCCIÓN DEL FRONTEND ==='
                    sh 'npm install'
                    sh 'chmod +x ./node_modules/.bin/ng'
                    sh 'npm run build'
                    echo '✅ === FIN: CONSTRUCCIÓN DEL FRONTEND COMPLETADA ==='
                }
            }
        }

        // ETAPA NUEVA: Levantar la BD para las pruebas
        stage('Start Services for Test') {
            steps {
                echo '🐳 === INICIO: LEVANTANDO SERVICIOS PARA TESTS ==='
                sh "docker-compose -p ${DOCKER_PROJECT_NAME}-test up -d mysql_cine_app"
                echo '⏳ Esperando que la base de datos esté lista...'
                sh 'sleep 30'
                echo '✅ === FIN: SERVICIOS PARA TESTS LISTOS ==='
            }
        }

        // ETAPA MODIFICADA: Ejecutar pruebas contra la BD de Docker
        stage('Test') {
            steps {
                dir('ProyectLP2') {
                    echo '🧪 === INICIO: EJECUCIÓN DE PRUEBAS ==='
                    // CORRECCIÓN FINAL: Usar un bloque de script multilínea para manejar caracteres especiales
                    sh """
                        mvn -Dspring.datasource.url='jdbc:mysql://localhost:3307/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC' \\
                            -Dspring.datasource.username=${DB_USER} \\
                            -Dspring.datasource.password=${DB_PASSWORD} \\
                            test
                    """
                    echo '✅ === FIN: PRUEBAS COMPLETADAS ==='
                }
            }
        }

        // ETAPA NUEVA: Detener los servicios de prueba
        stage('Stop Test Services') {
            steps {
                echo '🛑 === INICIO: DETENIENDO SERVICIOS DE TEST ==='
                sh "docker-compose -p ${DOCKER_PROJECT_NAME}-test down -v"
                echo '✅ === FIN: SERVICIOS DE TEST DETENIDOS ==='
            }
        }

        stage('Sonar Analysis') {
            steps {
                dir('ProyectLP2') {
                    echo '📊 === INICIO: ANÁLISIS DE CALIDAD ==='
                    withSonarQubeEnv('sonarqube') {
                        sh 'mvn sonar:sonar'
                    }
                    echo '✅ === FIN: ANÁLISIS DE CALIDAD COMPLETADO ==='
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    echo '🎯 === VERIFICACIÓN DE ESTÁNDARES DE CALIDAD ==='
                    waitForQualityGate abortPipeline: true
                    echo '✅ === FIN: VERIFICACIÓN DE CALIDAD COMPLETADA ==='
                }
            }
        }

        // ETAPA FINAL: Despliegue completo de la aplicación
        stage('Deploy Application') {
            steps {
                echo '🚀 === INICIO: PROCESO DE DESPLIEGUE FINAL ==='
                script {
                    echo '1️⃣ Limpiando despliegue anterior...'
                    sh "docker-compose -p ${DOCKER_PROJECT_NAME} down -v --remove-orphans || true"

                    echo '2️⃣ Construyendo y levantando servicios...'
                    sh "docker-compose -p ${DOCKER_PROJECT_NAME} up -d --build"

                    echo '3️⃣ Inicializando base de datos...'
                    sleep(30)
                    sh "docker exec -i ${DB_CONTAINER_NAME} mysql -u${DB_USER} -p${DB_PASSWORD} ${DB_NAME} < ProyectLP2/sql/init.sql"

                    echo '4️⃣ Verificando estructura de la base de datos...'
                    sh "docker exec ${DB_CONTAINER_NAME} mysql -u${DB_USER} -p${DB_PASSWORD} -e 'USE ${DB_NAME}; SHOW TABLES;'"

                    echo '5️⃣ Mostrando logs de la aplicación:'
                    sleep(20)
                    sh "docker logs --tail 100 ${APP_CONTAINER_NAME}"
                }
                echo '✅ === FIN: DESPLIEGUE COMPLETADO ==='
            }
        }
    }

    post {
        always {
            echo '🏁 === FINALIZACIÓN DEL PIPELINE ==='
            junit allowEmptyResults: true, testResults: 'ProyectLP2/target/surefire-reports/*.xml'
            // Asegurarse de que los contenedores de test se detengan incluso si hay un fallo
            sh "docker-compose -p ${DOCKER_PROJECT_NAME}-test down -v || true"
        }
        success {
            echo '🎉 ✓ Pipeline completado exitosamente'
        }
        failure {
            echo '💥 ✗ Pipeline falló'
        }
    }
}