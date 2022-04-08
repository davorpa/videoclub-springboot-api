# videoclub-springboot-api

<div align="center" markdown="1">

![Videoclupy!](screenshot.png)

</div>

## Requisitos

Teniendo las siguientes entidades:

1. Género:
    - Código (Usar enumeración)
    - Descripción.
2. Película:
    - Título. Obligatorio.
    - Año: Obligatorio. Número positivo mayor que 1900
    - Duración: Obligatorio. Número positivo menor que 500.
    - Actores: Por lo menos debe tener uno.
    - Género: Obligatorio.
    - Ejemplares. Lista de ejemplares
3. Actor:
    - Nombre. Obligatorio
    - Primer Apellido: Obligatorio
    - Segundo Apellido: Sólo podrá venir relleno si está cubierto el primer apellido.
    - Fecha de Nacimiento: Obligatorio y mayor que 1/1/1900
4. Ejemplar
    - Estado. Obligatorio. Enumerado: Libre, Reservado, Alquilado, Comprado
    - Fecha alta en la tienda (stock)
    - Fecha baja en la tienda (stock)
    - Fecha salida de la tienda (alquiler / compra)
    - Fecha devolución en la tienda (alquiler / compra)

Se pide:

1. Por defecto, el registro de películas vacío salvo los géneros que serán Comedia, Western, Ciencia-Ficción, Romántica e Histórica.
2. Crear métodos CRUD para los actores.
3. Crear métodos CRUD para las películas.
4. Crear interfaz para dar de alta actores y poder modificar sus datos.
5. Crear interfaz para localizar películas por título, año, duración, nombre del autor, primer apellido del autor.
6. Las validaciones se realizarán mediante constraints en los DTO correspondientes.
7. Crear interfaz para permitir alquilar y devolver ejemplares de películas, así como la gestión de su stock (opcionalmente).

## API Urls:

- `GET` -> `/api/privado/generos/`: listar géneros.

- `GET` -> `/api/privado/peliculas/`: listar películas.
- `GET` -> `/api/privado/peliculas/{id}`: Obtener una película.
- `POST` -> `/api/privado/peliculas`: Crear Película.
- `PUT` -> `/api/privado/peliculas/{id}`: Modificar película.
- `DELETE` -> `/api/privado/peliculas/{id}`: Borrar película.

- `GET` -> `/api/privado/actores/`: listar actores.
- `GET` -> `/api/privado/actores/{id}`: Obtener una actor.
- `POST` -> `/api/privado/actores`: Crear actor.
- `PUT` -> `/api/privado/actores/{id}`: Modificar actor.
- `DELETE` -> `/api/privado/actores/{id}`: Borrar actor.

- `GET` -> `/api/privado/peliculas/search`: Localizar películas.

- `GET` -> `/api/privado/peliculas/{id}/stock/`: listar el stock de una película.
- `POST` -> `/api/privado/peliculas/{id}/stock/`: Modificar (incrementar / decrementar) el stock de una película.

- `POST` -> `/api/privado/store/peliculas/{id}/e/{eid}/?action=sell`: Compraventa de un ejemplar.
- `POST` -> `/api/privado/store/peliculas/{id}/e/{eid}/?action=rent`: Alquila un ejemplar.
- `POST` -> `/api/privado/store/peliculas/{id}/e/{eid}/?action=return`: Devuelve un ejemplar.


## Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.6/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.6/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.6/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.6.6/reference/htmlsingle/#boot-features-security)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.6.6/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/docs/2.6.6/reference/htmlsingle/#boot-features-validation)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.6.6/reference/htmlsingle/#production-ready)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.6.6/reference/htmlsingle/#using-boot-devtools)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.6.6/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Spring HATEOAS](https://docs.spring.io/spring-boot/docs/2.6.6/reference/htmlsingle/#boot-features-spring-hateoas)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Building a Hypermedia-Driven RESTful Web Service](https://spring.io/guides/gs/rest-hateoas/)

