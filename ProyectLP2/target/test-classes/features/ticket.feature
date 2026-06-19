# language: es
Característica: Gestión de Venta de Tickets de Cine - Cobertura de Transacciones

  Antecedentes:
    Dado que el sistema de ventas se encuentra limpio y preparado

  @FlujoFelizCompraTicket
  Escenario: Comprar un ticket para un asiento libre exitosamente
    Dado que la función 1 está disponible y el asiento 12 se encuentra "LIBRE"
    Cuando el cliente con ID 1 realiza la compra del ticket con precio 15.50
    Entonces el ticket se genera correctamente y el código de estado es 201

  @FlujoErrorAsientoOcupado
  Escenario: Impedir la compra de un ticket si el asiento ya está ocupado
    Dado que la función 1 está disponible y el asiento 12 se encuentra "OCUPADO"
    Cuando el cliente con ID 1 intenta realizar la compra del ticket con precio 15.50
    Entonces el sistema debe denegar la venta respondiendo con un código de error 409