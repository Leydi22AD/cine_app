# Etapa 1: Compilación de la aplicación Angular
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
# La carpeta de salida es "dist/client_event" según angular.json
RUN npm run build -- --configuration production

# Etapa 2: Servidor web con Nginx
FROM nginx:1.25-alpine
# Copiar los artefactos de la compilación desde la etapa anterior
COPY --from=build /app/dist/client_event /usr/share/nginx/html
# Copiar una configuración personalizada de Nginx si es necesario (opcional)
# COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
