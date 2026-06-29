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
        // Definimos los nombres de los archivos de compose para usarlos fácilmente
        COMPOSE_FILE = 'docker-compose.yml'
        COMPOSE_TEST_FILE = 'docker-compose.test.yml'
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

        stage('Start DB for Test') {
            steps {
                echo '🐳 === INICIO: LEVANTANDO BASE DE DATOS PARA TESTS ==='
                sh "docker-compose -f ${COMPOSE_FILE} -p ${DOCKER_PROJECT_NAME}-test up -d mysql_cine_app"
                echo '⏳ Esperando que la base de datos esté lista...'
                sh '''
                    set -e
                    CONTAINER_ID=$(docker-compose -f docker-compose.yml -p cine_app-test ps -q mysql_cine_app)
                    if [ -z "$CONTAINER_ID" ]; then
                        echo "Error: No se pudo encontrar el contenedor de la base de datos."
                        exit 1
                    fi
                    timeout 120s bash -c "until docker exec $CONTAINER_ID mysqladmin ping -u'root' -p'admin123' --silent; do
                        echo 'Esperando a la base de datos...';
                        sleep 2;
                    done"
                '''
                echo '✅ === FIN: BASE DE DATOS PARA TESTS LISTA ==='
            }
        }

        // ETAPA CORREGIDA: Se llama al script con su ruta absoluta
        stage('Test') {
            steps {
                echo '🧪 === INICIO: EJECUCIÓN DE PRUEBAS DENTRO DE DOCKER ==='
                // Copiamos el script al contexto de build para que el Dockerfile.test lo pueda usar
                sh 'cp wait-for-it.sh ProyectLP2/'
                
                sh """
                    docker-compose -f ${COMPOSE_FILE} -f ${COMPOSE_TEST_FILE} -p ${DOCKER_PROJECT_NAME}-test run --rm test-runner \\
                    /bin/sh -c "/usr/local/bin/wait-for-it.sh ${DB_CONTAINER_NAME} 3306 -- \\
                    mvn -Dspring.datasource.url='jdbc:mysql://${DB_CONTAINER_NAME}:3306/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC' \\
                        -Dspring.datasource.username=${DB_USER} \\
                        -Dspring.datasource.password=${DB_PASSWORD} \\
                        test"
                """
                echo '✅ === FIN: PRUEBAS COMPLETADAS ==='
            }
        }

        stage('Stop Test Services') {
            steps {
                echo '🛑 === INICIO: DETENIENDO SERVICIOS DE TEST ==='
                sh "docker-compose -f ${COMPOSE_FILE} -p ${DOCKER_PROJECT_NAME}-test down -v"
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

        stage('Deploy Application') {
            steps {
                echo '🚀 === INICIO: PROCESO DE DESPLIEGUE FINAL ==='
                script {
                    echo '1️⃣ Limpiando despliegue anterior...'
                    sh "docker-compose -f ${COMPOSE_FILE} -p ${DOCKER_PROJECT_NAME} down -v --remove-orphans || true"

                    echo '2️⃣ Construyendo y levantando servicios...'
                    sh "docker-compose -f ${COMPOSE_FILE} -p ${DOCKER_PROJECT_NAME} up -d --build"

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
            // Aseguramos que se usen los archivos correctos para limpiar todo
            sh "docker-compose -f ${COMPOSE_FILE} -f ${COMPOSE_TEST_FILE} -p ${DOCKER_PROJECT_NAME}-test down -v || true"
        }
        success {
            echo '🎉 ✓ Pipeline completado exitosamente'
        }
        failure {
            echo '💥 ✗ Pipeline falló'
        }
    }
}