// 🚀 Jenkinsfile - Pipeline de CI/CD para CineApp (Versión final corregida)
pipeline {
    agent any

    // 🛠️ Configuración de herramientas necesarias
    tools {
        maven 'MAVEN_HOME'
        // AÑADIDO: Especificar la herramienta NodeJS para que 'npm' esté disponible
        nodejs 'NodeJS' 
    }

    // 🔧 Definición de variables globales corregidas para CineApp
    environment {
        DOCKER_PROJECT_NAME = 'cine_app'
        APP_CONTAINER_NAME = 'cine_app_backend'
        DB_CONTAINER_NAME = 'mysql_cine_app_db'
        DB_NAME = 'cine_app_db'
        DB_USER = 'root'
        DB_PASSWORD = 'admin123'
    }

    stages {
        // 📥 Etapa 1: Clonación del repositorio
        stage('Clone') {
            steps {
                // Aumentando el timeout a 30 minutos para evitar errores
                timeout(time: 30, unit: 'MINUTES') {
                    echo '🔄 === INICIO: CLONACIÓN DEL REPOSITORIO ==='
                    cleanWs()
                    checkout scm
                }
            }
        }

        // 🏗️ Etapa 2: Construcción del Backend
        stage('Build Backend') {
            steps {
                dir('ProyectLP2') {
                    echo '🔨 === INICIO: CONSTRUCCIÓN DEL BACKEND ==='
                    sh 'mvn clean package -DskipTests'
                    echo '✅ === FIN: CONSTRUCCIÓN DEL BACKEND COMPLETADA ==='
                }
            }
        }
        
        // 🎨 Etapa 3: Construcción del Frontend
        stage('Build Frontend') {
            steps {
                dir('Lp2-Frontend') {
                    echo '🎨 === INICIO: CONSTRUCCIÓN DEL FRONTEND ==='
                    sh 'npm install'
                    sh 'npm run build'
                    echo '✅ === FIN: CONSTRUCCIÓN DEL FRONTEND COMPLETADA ==='
                }
            }
        }

        // 🧪 Etapa 4: Ejecución de Pruebas
        stage('Test') {
            steps {
                dir('ProyectLP2') {
                    echo '🧪 === INICIO: EJECUCIÓN DE PRUEBAS ==='
                    sh 'mvn test'
                    echo '✅ === FIN: PRUEBAS COMPLETADAS ==='
                }
            }
        }

        // 📊 Etapa 5: Análisis de Calidad con SonarQube
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

        // 🎯 Etapa 6: Verificación de Calidad (Quality Gate)
        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    echo '🎯 === VERIFICACIÓN DE ESTÁNDARES DE CALIDAD ==='
                    waitForQualityGate abortPipeline: true
                    echo '✅ === FIN: VERIFICACIÓN DE CALIDAD COMPLETADA ==='
                }
            }
        }

        // 🚀 Etapa 7: Despliegue de la Aplicación
        stage('Deploy Application') {
            steps {
                echo '🚀 === INICIO: PROCESO DE DESPLIEGUE ==='
                script {
                    // 🧹 Limpieza de despliegue anterior
                    echo '1️⃣ Limpiando despliegue anterior...'
                    sh "docker-compose -p ${DOCKER_PROJECT_NAME} down -v --remove-orphans || true"

                    // 🏗️ Construcción y levantamiento de servicios
                    echo '2️⃣ Construyendo y levantando servicios...'
                    sh "docker-compose -p ${DOCKER_PROJECT_NAME} up -d --build"

                    // 💾 Inicialización de la base de datos
                    echo '3️⃣ Inicializando base de datos...'
                    sleep(30) // Espera para que el servicio de MySQL esté completamente listo
                    // CORREGIDO: Usando la ruta correcta al script SQL si está dentro de ProyectLP2
                    sh "docker exec -i ${DB_CONTAINER_NAME} mysql -u${DB_USER} -p${DB_PASSWORD} ${DB_NAME} < ProyectLP2/sql/init.sql"

                    // 🔍 Verificación de la base de datos
                    echo '4️⃣ Verificando estructura de la base de datos...'
                    sh "docker exec ${DB_CONTAINER_NAME} mysql -u${DB_USER} -p${DB_PASSWORD} -e 'USE ${DB_NAME}; SHOW TABLES;'"

                    // ⏳ Espera y verificación de la aplicación
                    echo '5️⃣ Mostrando logs de la aplicación:'
                    sleep(20) // Espera para que la aplicación inicie
                    sh "docker logs --tail 100 ${APP_CONTAINER_NAME}"
                }
                echo '✅ === FIN: DESPLIEGUE COMPLETADO ==='
            }
        }
    }

    // 📝 Acciones post-ejecución
    post {
        always {
            echo '🏁 === FINALIZACIÓN DEL PIPELINE ==='
            junit allowEmptyResults: true, testResults: 'ProyectLP2/target/surefire-reports/*.xml'
        }
        success {
            echo '🎉 ✓ Pipeline completado exitosamente'
        }
        failure {
            echo '💥 ✗ Pipeline falló'
        }
    }
}