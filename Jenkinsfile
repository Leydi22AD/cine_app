// 🚀 Jenkinsfile - Pipeline de CI/CD para CineApp
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
        // Definimos el nombre del archivo de compose para usarlo fácilmente
        COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Clone') {
            steps {
                timeout(time: 30, unit: 'MINUTES') {
                    echo '🔄 === INICIO: CLONACIÓN DEL REPOSITORIO ==='
                    // Optimización: Clonación superficial
                    checkout([
                        $class: 'GitSCM',
                        branches: scm.branches,
                        userRemoteConfigs: scm.userRemoteConfigs,
                        extensions: [[$class: 'CloneOption', shallow: true, noTags: true, depth: 1]]
                    ])
                }
            }
        }

        stage('Test Backend') {
            steps {
                echo '🧹 === Limpiando contenedores anteriores para liberar puertos ==='
                sh "docker-compose -f ${COMPOSE_FILE} -p ${DOCKER_PROJECT_NAME} down || true"
                dir('ProyectLP2') {
                    echo '🧪 === INICIO: PRUEBAS DEL BACKEND ==='
                    sh 'mvn clean test'
                    echo '✅ === FIN: PRUEBAS DEL BACKEND COMPLETADAS ==='
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir('ProyectLP2') {
                    echo '🔨 === INICIO: CONSTRUCCIÓN DEL BACKEND ==='
                    sh 'mvn package -DskipTests'
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

        stage('Sonar Analysis') {
            steps {
                dir('ProyectLP2') {
                    echo '📊 === INICIO: ANÁLISIS DE CALIDAD ==='
                    withSonarQubeEnv('sonarqube') {
                        sh 'mvn sonar:sonar'
                    }
                    echo '✅ === FIN: ANÁLisis de calidad completado ==='
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
        }
        success {
            echo '🎉 ✓ Pipeline completado exitosamente'
        }
        failure {
            echo '💥 ✗ Pipeline falló'
        }
    }
}