# language: es
Característica: Certificación de Calidad - Programación de Funciones de Cine

  Antecedentes:
    Dado que la base de datos de pruebas está activa y limpia

  @FlujoFelizFuncion
  Escenario: Crear una nueva función para una película en una sala exitosamente
    Dado que existe una película con ID 32 y una sala con ID 27 en el sistema
    Cuando el administrador programa una función para la fecha "2030-06-15" hora "19:30:00" con precio 15.50
    Entonces el sistema debe registrar la función retornando un código de estado 201
    Y el JSON de respuesta debe incluir el ID de la función generada

  @FlujoErrorCamposVacíos
  Escenario: Impedir la programación de una función con parámetros inválidos o vacíos
    Cuando el administrador intenta programar una función con precio -5.00 y sin ID de película
    Entonces el sistema debe denegar el registro respondiendo con un código de error 400
    Y el mensaje de validación debe indicar "Campos de función obligatorios"

  @FlujoErrorHorarioOcupado
  Escenario: Impedir la programación de dos funciones a la misma hora en la misma sala
    Dado que existe una película con ID 18 y una sala con ID 10 en el sistema
    Cuando el administrador programa una función para la fecha "2030-06-15" hora "19:30:00" con precio 15.50
    Y otro administrador intenta programar otra función en la sala ID 10 para la misma fecha "2030-06-15" y hora "19:30:00"
    Entonces el sistema debe lanzar un conflicto respondiendo con un código de estado 409