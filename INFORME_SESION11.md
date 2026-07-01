# Informe Técnico — Sesión 11: Biblioteca con Contenedores

**Curso:** Ingeniería Web  
**Sesión:** 11 — Despliegue de Aplicaciones Spring  
**Proyecto:** API REST de Biblioteca con Docker  

---

## 1. Propósito y objetivo

El laboratorio consistió en preparar y desplegar una aplicación Spring Boot del contexto de biblioteca, generando un archivo `.jar`, creando un `Dockerfile`, construyendo una imagen Docker y ejecutando la aplicación dentro de un contenedor.

---

## 2. Descripción del proyecto

Se desarrolló una API REST con Spring Boot 3.2.5 y Java 17, usando Maven como gestor de dependencias y H2 como base de datos embebida.

**Módulos implementados:**

| Módulo    | Ruta base           | Operaciones                          |
|-----------|---------------------|--------------------------------------|
| Libros    | `/api/libros`       | Listar, obtener por id, crear        |
| Usuarios  | `/api/usuarios`     | Listar, obtener por id, crear        |
| Préstamos | `/api/prestamos`    | Listar, obtener por id, registrar    |

**Estructura del proyecto:**

```
Sesion11Spring/
├── src/main/java/com/biblioteca/
│   ├── BibliotecaApplication.java
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/
│   ├── dto/
│   └── exception/
├── src/main/resources/
│   ├── application.properties
│   ├── application-docker.properties
│   └── data.sql
├── Dockerfile
└── pom.xml
```

---

## 3. Proceso realizado

### Paso 1 — Compilar y ejecutar en local

```powershell
cd "c:\Users\Jonathan Rojas\PAGINAS_WEB\Sesion11Spring"
.\mvnw.cmd clean package -DskipTests
java -jar target\biblioteca-api-1.0.0.jar
```

Resultado: `BUILD SUCCESS` y la aplicación arranca en el puerto 8080.

### Paso 2 — Generar el archivo .jar

El comando `mvnw clean package` genera:

```
target/biblioteca-api-1.0.0.jar
```

Es un JAR ejecutable (fat jar) que incluye todas las dependencias necesarias para correr sin el IDE.

### Paso 3 — Crear el Dockerfile

```dockerfile
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/biblioteca-api-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
```

### Paso 4 — Construir la imagen Docker

```powershell
docker build -t biblioteca-api:1.0 .
```

### Paso 5 — Ejecutar el contenedor

```powershell
docker run -d --name biblioteca -p 8080:8080 biblioteca-api:1.0
docker ps
```

Para levantar una segunda instancia en otro puerto:

```powershell
docker run -d --name biblioteca2 -p 8081:8080 biblioteca-api:1.0
```

### Paso 6 — Comprobar con navegador o Postman

| Método | URL                                    | Resultado esperado   |
|--------|----------------------------------------|----------------------|
| GET    | http://localhost:8080/api/libros       | Lista JSON de libros |
| GET    | http://localhost:8080/api/usuarios     | Lista de usuarios    |
| GET    | http://localhost:8080/api/prestamos    | Lista de préstamos   |
| POST   | http://localhost:8080/api/libros       | Crear libro (201)    |

**Verificación local realizada:**

- `GET /api/libros` → 2 libros (El Quijote, Cien años de soledad)
- `GET /api/usuarios` → 2 usuarios (Ana López, Carlos Ruiz)
- `GET /api/prestamos` → lista vacía inicial

### Paso 7 — Capturas de evidencia

Adjuntar al `.zip` las siguientes capturas:

1. Terminal con `BUILD SUCCESS` y el `.jar` en `target/`
2. Contenido del `Dockerfile` en el editor
3. Salida de `docker build -t biblioteca-api:1.0 .`
4. Salida de `docker ps` con el contenedor en ejecución
5. Respuesta JSON de `GET /api/libros` en Postman o navegador (desde el contenedor)

---

## 4. Dockerfile explicado

| Instrucción   | Función                                                                 |
|---------------|-------------------------------------------------------------------------|
| `FROM`        | Define la imagen base con Java 17 (JRE de Eclipse Temurin)              |
| `WORKDIR`     | Establece `/app` como directorio de trabajo dentro del contenedor       |
| `COPY`        | Copia el `.jar` compilado desde el host hacia la imagen                 |
| `EXPOSE`      | Documenta que la aplicación escucha en el puerto 8080                     |
| `ENTRYPOINT`  | Ejecuta el JAR al iniciar el contenedor, activando el perfil `docker`   |

---

## 5. Imagen vs contenedor

**Imagen Docker:** plantilla de solo lectura con el sistema de archivos preparado (Java + JAR). Se crea una vez con `docker build` y puede reutilizarse.

**Contenedor:** instancia en ejecución de una imagen. Tiene su propio proceso, red y puertos mapeados. Se crea con `docker run` y se puede detener, eliminar o volver a crear.

Relación: una imagen puede generar múltiples contenedores, cada uno aislado del otro.

---

## 6. Comparación: ejecución desde IDE vs contenedor

| Aspecto          | IDE / `java -jar` local          | Contenedor Docker                    |
|------------------|----------------------------------|--------------------------------------|
| Entorno          | Depende del JDK instalado en PC  | JDK incluido en la imagen base       |
| Configuración    | Variables locales del sistema    | Definida en Dockerfile y perfil      |
| Portabilidad     | Puede fallar en otra máquina     | Mismo comportamiento en cualquier PC |
| Base de datos    | H2 en memoria local              | H2 embebida dentro del contenedor    |
| Despliegue       | Manual, paso a paso              | Un comando: `docker run`             |

---

## 7. Múltiples contenedores

Si se necesitan varias instancias de la aplicación, cada contenedor debe tener un nombre y un puerto del host distintos:

```powershell
docker run -d --name biblioteca  -p 8080:8080 biblioteca-api:1.0
docker run -d --name biblioteca2 -p 8081:8080 biblioteca-api:1.0
```

Cada contenedor corre la misma imagen pero es un proceso independiente. En un entorno empresarial esto permite escalar horizontalmente: varias réplicas detrás de un balanceador de carga.

---

## 8. Despliegue futuro en la nube

Esquema básico:

1. Compilar el `.jar` y construir la imagen Docker
2. Subir la imagen a un registro (Docker Hub, AWS ECR, Azure ACR)
3. Desplegar en un servicio de contenedores (AWS ECS, Azure Container Apps, Google Cloud Run)
4. Configurar variables de entorno, puerto y base de datos gestionada
5. Activar monitoreo y escalado automático según demanda

---

## 9. Dificultades encontradas

- **Puerto 8080 ocupado:** si la app corre localmente y luego se levanta el contenedor en el mismo puerto, hay conflicto. Solución: detener el proceso local antes de `docker run`.
- **Docker Desktop apagado:** `docker build` falla si el motor de Docker no está activo. Solución: iniciar Docker Desktop y esperar a que esté listo.
- **Perfil incorrecto:** sin `--spring.profiles.active=docker` el contenedor podría intentar conectarse a una BD distinta. Se resolvió activando el perfil en el `ENTRYPOINT`.

---

## 10. Reflexión final

¿Qué beneficios ofrece el uso de contenedores cuando una aplicación debe ejecutarse en distintos equipos o entornos?

Los contenedores empaquetan la aplicación junto con sus dependencias (Java, librerías, configuración) en una unidad portable. Esto elimina el problema de "en mi máquina funciona" porque el entorno dentro del contenedor es idéntico en desarrollo, pruebas y producción. Además, el despliegue se reduce a construir la imagen y ejecutarla, lo que facilita la migración entre equipos del laboratorio, servidores on-premise o servicios en la nube sin reinstalar JDK ni reconfigurar manualmente cada servidor.

---

## 11. Checklist de cumplimiento

| Requisito del laboratorio                              | Estado        |
|--------------------------------------------------------|---------------|
| Proyecto Spring Boot funcional                         | Cumplido      |
| Maven genera `.jar` ejecutable                         | Cumplido      |
| `application.properties` configurado                     | Cumplido      |
| Funcionamiento local verificado                        | Cumplido      |
| Al menos un módulo (libros, usuarios, préstamos)       | Cumplido (3)  |
| Compilar y ejecutar en local                           | Cumplido      |
| Generar `.jar`                                         | Cumplido      |
| `Dockerfile` con Java, COPY, EXPOSE, ENTRYPOINT        | Cumplido      |
| Construir imagen Docker                                | Pendiente*    |
| Ejecutar contenedor                                    | Pendiente*    |
| Verificar con navegador/Postman/Insomnia               | Local OK / contenedor pendiente* |
| Capturas del proceso                                   | Por adjuntar  |
| Informe técnico con reflexión                          | Cumplido      |

\* Requiere Docker Desktop en ejecución. Comandos listos en sección 3.

---

## 12. Comandos rápidos de referencia

```powershell
# Compilar
.\mvnw.cmd clean package -DskipTests

# Ejecutar local
java -jar target\biblioteca-api-1.0.0.jar

# Docker (con Docker Desktop activo)
docker build -t biblioteca-api:1.0 .
docker run -d --name biblioteca -p 8080:8080 biblioteca-api:1.0
docker logs biblioteca
docker stop biblioteca
docker rm biblioteca
```
