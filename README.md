# api-cart

Microservicio de carrito de compras construido con Java 17 y Spring Boot, siguiendo
arquitectura hexagonal. Es el servicio que más se comunica con el resto: consulta el
catálogo y dispara la compra a través de clientes **OpenFeign**.

Forma parte del reto **Emazon**, una tienda virtual dividida en microservicios
independientes, desarrollado durante el **Bootcamp Power Up de Pragma** (2024).

## Arquitectura

Puertos y adaptadores, con el dominio aislado de la infraestructura:

```
domain/     modelos, reglas de negocio, casos de uso y puertos
            (cart, page, role, error)
            spi/  puertos de salida hacia stock y transaction
app/        handlers de aplicación, DTOs y mappers de MapStruct
infra/      adaptadores de entrada (REST) y de salida (JPA y Feign),
            seguridad, manejo de excepciones y OpenAPI
```

El dominio declara qué necesita de los otros servicios mediante `IFeignStockAdapterPort`
e `IFeignTransactionAdapterPort`. La infraestructura los implementa con Feign, así que
el dominio no sabe que existe HTTP.

## Endpoints

Todos requieren rol `CLIENT`.

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/cart/add` | Agregar artículo al carrito |
| `POST` | `/cart/remove` | Quitar artículo del carrito |
| `POST` | `/cart/buy` | Confirmar la compra del carrito |
| `GET` | `/cart/fetch` | Consultar el carrito, paginado y filtrable |

Documentación interactiva en `/swagger-ui.html` una vez levantado el servicio.

## Comunicación entre servicios

```
api-cart ──Feign──> api-stock         validar existencias y traer datos del artículo
         └─Feign──> api-transaction   registrar la compra
```

`FeignClientInterceptor` propaga el JWT del usuario en cada llamada saliente, de modo
que el servicio destino aplica la autorización sobre el mismo usuario que inició la
petición y no sobre una identidad de servicio.

## Reglas de negocio implementadas

- Un solo carrito por cliente, con fecha de última modificación
- Validación de existencias contra `api-stock` antes de agregar
- Máximo 3 artículos por categoría en el carrito
- Consulta del carrito con precio total y disponibilidad de cada artículo
- Fecha estimada de reabastecimiento cuando un artículo no tiene existencias
- Solo el dueño del carrito puede modificarlo

## Stack

- **Java 17**, **Spring Boot 3.3**
- Spring Web, Spring Data JPA, Spring Security, Spring Validation
- **Spring Cloud OpenFeign** para las llamadas entre servicios
- **MySQL** como motor de persistencia
- **MapStruct 1.5.5**, **JJWT 0.12.6**, **springdoc-openapi 2.6.0**
- **JUnit 5** y **Mockito** para pruebas

## Ejecución local

Requiere Java 17, MySQL, y que `api-stock`, `api-user` y `api-transaction` estén
levantados.

```bash
git clone https://github.com/Herreran903/api-cart.git
cd api-cart
./gradlew bootRun
```

Configura en `src/main/resources/application.properties` la conexión a la base de datos,
la clave de firma del JWT —debe coincidir con la de `api-user`— y las URL de los
servicios `api-stock` y `api-transaction`.

```bash
./gradlew test
```

## Servicios relacionados

- [`api-user`](https://github.com/Herreran903/api-user) — autenticación y usuarios
- [`api-stock`](https://github.com/Herreran903/api-stock) — catálogo e inventario
- [`api-transaction`](https://github.com/Herreran903/api-transaction) — ventas y abastecimiento
