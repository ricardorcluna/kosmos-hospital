
# Proyecto de Citas Médicas - Spring Boot

Este es un proyecto para la gestión de citas médicas, desarrollado con **Spring Boot**, **PostgreSQL** y **Docker**. La aplicación permite la creación, actualización y cancelación de citas para pacientes con validaciones de horarios, conflictos de citas y límites de citas por doctor.

## Requisitos

- **Java 17** o superior
- **Docker** y **Docker Compose**
- **PostgreSQL**

## Configuración del Proyecto

### Docker

Este proyecto utiliza Docker para ejecutar PostgreSQL. Puedes iniciar la base de datos ejecutando el siguiente comando desde la raíz del proyecto:

```bash
docker-compose up
```

Esto levantará un contenedor de PostgreSQL en el puerto `5432` y creará una base de datos llamada `kosmos_db` con las credenciales configuradas.

### Estructura de la Base de Datos

El archivo `data.sql` en el directorio `src/main/resources` contiene los datos de prueba que se insertan en la base de datos al iniciar el contenedor de PostgreSQL.

```sql
-- Datos de prueba para doctores
INSERT INTO doctor (doctor_id, nombre, apellido_paterno, apellido_materno, especialidad)
VALUES
    (1, 'Juan', 'Pérez', 'López', 'Cardiología'),
    (2, 'María', 'González', 'Martínez', 'Pediatría'),
    (3, 'Luis', 'Hernández', 'Cruz', 'Neurología');

-- Datos de prueba para consultorios
INSERT INTO consultorio (consultorio_id, numero_consultorio, piso)
VALUES
    (1, 101, 1),
    (2, 102, 1),
    (3, 201, 2);
```

## Endpoints

La aplicación expone los siguientes endpoints para gestionar citas médicas:

### Crear una Cita

- **URL**: `/citas/addCita`
- **Método**: `POST`
- **Request Body**:

```json
{
  "doctorId": 1,
  "consultorioId": 1,
  "nombrePaciente": "Carlos",
  "horarioInicio": "2024-12-10T09:00:00",
  "horarioFin": "2024-12-10T09:30:00"
}
```

- **Respuesta**:

```json
{
  "message": "Cita fue creada con éxito."
}
```

### Cancelar una Cita

- **URL**: `/citas/cancelar/{citaId}`
- **Método**: `DELETE`
- **Path Variable**: `citaId` (ID de la cita a cancelar)
- **Respuesta**:

```json
{
  "message": "Cita cancelada con éxito."
}
```

### Editar una Cita

- **URL**: `/citas/{citaId}`
- **Método**: `PUT`
- **Request Body**:

```json
{
  "doctorId": 2,
  "consultorioId": 2,
  "nombrePaciente": "Juan",
  "horarioInicio": "2024-12-10T10:00:00",
  "horarioFin": "2024-12-10T10:30:00"
}
```

- **Respuesta**:

```json
{
  "message": "Cita actualizada con éxito."
}
```

## Validaciones

Se han implementado diversas validaciones para garantizar que las citas no generen conflictos:

- **Horario de inicio antes que el horario de fin**: Se valida que el horario de inicio sea antes que el horario de fin.

- **Conflicto en el consultorio**: Se valida que no haya conflictos de horario en el consultorio para la cita programada.

- **Conflicto con el doctor**: Se valida que el doctor no tenga otra cita en el mismo horario.

- **Conflicto con el paciente**: Se valida que el paciente no tenga otra cita el mismo día con menos de 2 horas de diferencia.

- **Límite de citas por doctor**: Se valida que el doctor no tenga más de 8 citas al día.
