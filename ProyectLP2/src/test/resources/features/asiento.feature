# language: es
Característica: Certificación de Calidad y Cobertura del Microservicio de Asientos (Generación Automática)

  Antecedentes:
    Dado que la base de datos de pruebas está activa y limpia

  @FlujoFeliz
  Escenario: Verificar que los asientos se crean automáticamente al existir una sala
    Dado que se registra una nueva sala con ID 1, con 5 filas y 12 columnas
    Cuando se solicita la lista de asientos de la sala ID 1 mediante una petición GET
    Entonces el sistema debe responder con un código de estado de éxito 200
    Y la lista debe contener un total de 60 asientos configurados como "LIBRE"

  @FlujoDuplicado
  Escenario: Impedir la duplicación de asientos al intentar registrar manualmente un asiento ya existente
    Dado que se registra una nueva sala con ID 1, con 5 filas y 12 columnas
    Cuando se envía una solicitud POST manual para crear un asiento en la fila 2 columna 3 que ya fue autogenerado
    Entonces el sistema debe responder con un código de estado de conflicto 409