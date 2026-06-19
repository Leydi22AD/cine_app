# language: es
Característica: Gestion de Usuarios del Cine

  Escenario: Iniciar sesion exitosamente
    Dado que el usuario "leydi@example.com" ya esta registrado con la clave "1234"
    Cuando intenta iniciar sesion con el correo "leydi@example.com" y clave "1234"
    Entonces el sistema debe permitir el ingreso y generar un token JWT
