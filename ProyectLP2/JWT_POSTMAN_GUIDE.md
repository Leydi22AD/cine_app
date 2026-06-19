# Guía de Pruebas JWT con Postman

## ✅ Estado del Servidor

**El servidor está corriendo en**: `http://localhost:8082`

**Configuración JWT Implementada**

- **Token de Sesión**: Duración de **5 minutos** (300000 ms)
- **Algoritmo**: HS256
- **Encriptación de Contraseñas**: BCrypt
- **Puerto del Backend**: 8082
- **Estado**: ✅ **LISTO PARA PROBAR**

## Endpoints Disponibles

### 1. Registro de Usuario (POST)
**URL**: `http://localhost:8082/api/v1/auth/register`

**Headers**:
```
Content-Type: application/json
```

**Body (JSON)**:
```json
{
    "nombre": "Juan Pérez",
    "correo": "juan@test.com",
    "password": "password123",
    "rol": "CLIENTE"
}
```

**Roles Disponibles**: `ADMIN` o `CLIENTE`

**Respuesta Exitosa (201)**:
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "usuario": {
        "id": 1,
        "nombre": "Juan Pérez",
        "correo": "juan@test.com",
        "rol": "CLIENTE"
    }
}
```

### 2. Login (POST)
**URL**: `http://localhost:8082/api/v1/auth/login`

**Headers**:
```
Content-Type: application/json
```

**Body (JSON)**:
```json
{
    "email": "juan@test.com",
    "password": "password123"
}
```

**Respuesta Exitosa (200)**:
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "usuario": {
        "id": 1,
        "nombre": "Juan Pérez",
        "correo": "juan@test.com",
        "rol": "CLIENTE"
    }
}
```

**Respuesta Error (401)**:
```json
{
    "timestamp": "2025-11-15T07:41:52.123+00:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "Credenciales inválidas",
    "path": "/api/v1/auth/login"
}
```

### 3. Validar Token (POST)
**URL**: `http://localhost:8082/api/v1/auth/validate-token`

**Headers**:
```
Content-Type: application/json
Authorization: Bearer <tu-token-aqui>
```

**Body (JSON)**:
```json
{
    "email": "juan@test.com",
    "password": "password123"
}
```

**Respuesta Exitosa (200)**: Retorna un nuevo token actualizado

### 4. Endpoints Protegidos (Requieren JWT)

Todos los demás endpoints ahora requieren autenticación JWT.

**Ejemplo - Listar Películas (GET)**:
**URL**: `http://localhost:8082/api/v1/peliculas`

**Headers**:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Sin token o token inválido (401)**:
```json
{
    "timestamp": "2025-11-15T07:41:52.123+00:00",
    "status": 401,
    "error": "Unauthorized",
    "path": "/api/v1/peliculas"
}
```

## Flujo de Prueba Recomendado

### Paso 1: Registrar Usuario
1. Abrir Postman
2. Crear request POST a `http://localhost:8082/api/v1/auth/register`
3. En Headers agregar: `Content-Type: application/json`
4. En Body seleccionar "raw" y pegar:
```json
{
    "nombre": "Test Admin",
    "correo": "admin@test.com",
    "password": "admin123",
    "rol": "ADMIN"
}
```
5. Enviar request
6. **Copiar el token** de la respuesta

### Paso 2: Verificar Login
1. Crear request POST a `http://localhost:8082/api/v1/auth/login`
2. Body:
```json
{
    "email": "admin@test.com",
    "password": "admin123"
}
```
3. Verificar que retorna el token

### Paso 3: Probar Endpoint Protegido
1. Crear request GET a `http://localhost:8082/api/v1/peliculas`
2. En Headers agregar:
   - Key: `Authorization`
   - Value: `Bearer <pegar-token-aqui>`
3. Enviar request - Debería funcionar

### Paso 4: Probar Sin Token
1. Mismo request GET pero **sin** el header Authorization
2. Debería retornar 401 Unauthorized

### Paso 5: Probar Expiración (5 minutos)
1. Esperar 5 minutos después de obtener el token
2. Intentar usar el mismo token en un endpoint protegido
3. Debería retornar 401 Unauthorized
4. Hacer login nuevamente para obtener un nuevo token

## Verificación en Base de Datos

La contraseña se almacena **encriptada con BCrypt**:

```sql
SELECT * FROM usuario;
-- La columna 'password' debería verse así:
-- $2a$10$Xh9kGYI7z3QVp8v6N2Jc4eR...
```

## Endpoints Públicos (No requieren JWT)

- `POST /api/v1/auth/login`
- `POST /api/v1/auth/register`
- `POST /api/v1/auth/validate-token`
- `POST /api/v1/usuarios/register`

## Troubleshooting

### Token expirado
**Error**: "JWT expired at..."
**Solución**: Hacer login nuevamente para obtener un nuevo token

### Credenciales inválidas
**Error**: 401 Unauthorized
**Solución**: Verificar email y password correctos

### Token no válido
**Error**: "Invalid JWT signature"
**Solución**: El token fue manipulado o es incorrecto, obtener uno nuevo

### Email duplicado
**Error**: 409 Conflict - "El correo ya está registrado"
**Solución**: Usar otro email o hacer login con el existente

## Notas Importantes

1. El token JWT expira en **5 minutos exactos**
2. Las contraseñas se almacenan **encriptadas con BCrypt**
3. Los roles deben ser exactamente `ADMIN` o `CLIENTE` (mayúsculas)
4. El header Authorization debe tener el formato: `Bearer <token>`
5. Todos los endpoints excepto los de auth requieren JWT válido
