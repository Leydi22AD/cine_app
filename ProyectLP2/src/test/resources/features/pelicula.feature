# language: es
Característica: Gestión de Cartelera de Películas - Cobertura de Consultas

  Antecedentes:
    Dado que la base de datos de películas está limpia y preparada

  @FlujoFelizBuscarPelicula
  Escenario: Buscar una película existente por su título exitosamente
    Dado que en la cartelera existe la película "Intensamente 2"
    Cuando el usuario busca la película por el título "Intensamente 2"
    Entonces el sistema devuelve los detalles de la película y el código de estado es 200

  @FlujoErrorPeliculaNoEncontrada
  Escenario: Intentar buscar una película que no está en cartelera
    Cuando el usuario busca la película por el título "Pelicula Inexistente 999"
    Entonces el sistema debe denegar la consulta respondiendo con un código de error 404