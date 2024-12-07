INSERT INTO doctor (doctor_id, nombre, apellido_paterno, apellido_materno, especialidad)
VALUES
    (1, 'Juan', 'Pérez', 'López', 'Cardiología'),
    (2, 'María', 'González', 'Martínez', 'Pediatría'),
    (3, 'Luis', 'Hernández', 'Cruz', 'Neurología');

INSERT INTO consultorio (consultorio_id, numero_consultorio, piso)
VALUES
    (1, 101, 1),
    (2, 102, 1),
    (3, 201, 2);

commit;