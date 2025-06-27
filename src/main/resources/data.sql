-- ================================================
-- SCRIPT DE DATOS INICIALES - CONSULTORIO PSICOLOGÍA
-- ================================================

-- Insertar Especialidades
INSERT INTO especialidad (nombre, descripcion) VALUES
('Psicología Clínica', 'Evaluación, diagnóstico y tratamiento de trastornos mentales y emocionales'),
('Psicología Cognitivo-Conductual', 'Terapia basada en la modificación de pensamientos y comportamientos disfuncionales'),
('Psicología Infantil', 'Especialización en el desarrollo psicológico y tratamiento de niños y adolescentes'),
('Psicología de Pareja', 'Terapia especializada en relaciones de pareja y problemas matrimoniales'),
('Psicología Familiar', 'Tratamiento de dinámicas familiares y problemas del sistema familiar'),
('Neuropsicología', 'Evaluación y rehabilitación de funciones cognitivas y neurológicas'),
('Psicología del Trauma', 'Tratamiento especializado en trastorno de estrés postraumático y traumas'),
('Psicología Positiva', 'Enfoque en el bienestar, fortalezas personales y calidad de vida'),
('Psicología Organizacional', 'Aplicación de principios psicológicos en el ámbito laboral y organizacional'),
('Psicoterapia Humanística', 'Enfoque centrado en la persona y el crecimiento personal');

-- Insertar Psicólogos
INSERT INTO psicologo (nombre, apellido, email, telefono, especialidad_id, numero_colegiado, años_experiencia, descripcion) VALUES
-- Psicólogos Clínicos
('Ana', 'García Mendoza', 'ana.garcia@consultorio.com', '+51 987 654 321', 1, 'CPP-12345', 8, 'Especialista en trastornos de ansiedad y depresión con enfoque integral y personalizado.'),
('Carlos', 'Rodríguez Silva', 'carlos.rodriguez@consultorio.com', '+51 987 654 322', 1, 'CPP-12346', 12, 'Amplia experiencia en trastornos del estado de ánimo y psicosis. Certificado en terapias de tercera generación.'),

-- Psicólogos Cognitivo-Conductuales
('María', 'López Vásquez', 'maria.lopez@consultorio.com', '+51 987 654 323', 2, 'CPP-12347', 6, 'Especialista en TCC para trastornos de ansiedad, fobias y trastornos obsesivo-compulsivos.'),
('José', 'Martínez Torres', 'jose.martinez@consultorio.com', '+51 987 654 324', 2, 'CPP-12348', 10, 'Experto en modificación de conducta y técnicas de exposición para diversos trastornos.'),

-- Psicólogos Infantiles
('Lucía', 'Fernández Ruiz', 'lucia.fernandez@consultorio.com', '+51 987 654 325', 3, 'CPP-12349', 7, 'Especialista en desarrollo infantil, TDAH y problemas de conducta en niños y adolescentes.'),
('Diego', 'Sánchez Morales', 'diego.sanchez@consultorio.com', '+51 987 654 326', 3, 'CPP-12350', 9, 'Experto en terapia lúdica y tratamiento de traumas infantiles. Certificado en evaluación neuropsicológica infantil.'),

-- Psicólogos de Pareja
('Elena', 'Ramírez Castro', 'elena.ramirez@consultorio.com', '+51 987 654 327', 4, 'CPP-12351', 11, 'Terapeuta sistémica especializada en terapia de pareja y problemas de comunicación.'),
('Andrés', 'Herrera Jiménez', 'andres.herrera@consultorio.com', '+51 987 654 328', 4, 'CPP-12352', 8, 'Especialista en terapia Gottman y resolución de conflictos en relaciones de pareja.'),

-- Psicólogos Familiares
('Patricia', 'Vargas León', 'patricia.vargas@consultorio.com', '+51 987 654 329', 5, 'CPP-12353', 13, 'Terapeuta familiar sistémica con amplia experiencia en dinámicas familiares disfuncionales.'),
('Roberto', 'Cruz Delgado', 'roberto.cruz@consultorio.com', '+51 987 654 330', 5, 'CPP-12354', 9, 'Especialista en terapia familiar estratégica y problemas de adolescencia en el contexto familiar.'),

-- Neuropsicólogos
('Carmen', 'Flores Aguirre', 'carmen.flores@consultorio.com', '+51 987 654 331', 6, 'CPP-12355', 10, 'Neuropsicóloga clínica especializada en evaluación cognitiva y rehabilitación neuropsicológica.'),
('Fernando', 'Moreno Peña', 'fernando.moreno@consultorio.com', '+51 987 654 332', 6, 'CPP-12356', 14, 'Experto en deterioro cognitivo, demencias y lesiones cerebrales traumáticas.'),

-- Psicólogos del Trauma
('Sofía', 'Guerrero Ramos', 'sofia.guerrero@consultorio.com', '+51 987 654 333', 7, 'CPP-12357', 7, 'Especialista en EMDR y tratamiento de trastorno de estrés postraumático.'),
('Miguel', 'Torres Campos', 'miguel.torres@consultorio.com', '+51 987 654 334', 7, 'CPP-12358', 6, 'Experto en trauma complejo y terapias somáticas para el tratamiento del trauma.'),

-- Psicólogos Positivos
('Isabella', 'Mendoza Ríos', 'isabella.mendoza@consultorio.com', '+51 987 654 335', 8, 'CPP-12359', 5, 'Especialista en psicología positiva, mindfulness y técnicas de bienestar emocional.'),

-- Psicólogos Organizacionales
('Alejandro', 'Paredes Vega', 'alejandro.paredes@consultorio.com', '+51 987 654 336', 9, 'CPP-12360', 12, 'Consultor organizacional especializado en clima laboral y desarrollo del talento humano.'),

-- Psicoterapeutas Humanísticos
('Valentina', 'Castillo Núñez', 'valentina.castillo@consultorio.com', '+51 987 654 337', 10, 'CPP-12361', 8, 'Terapeuta humanística especializada en terapia centrada en la persona y gestalt.');

-- Insertar Horarios Disponibles (Ejemplo para algunos psicólogos)
-- Horarios para Ana García (Lunes a Viernes)
INSERT INTO horario_disponible (psicologo_id, fecha, hora_inicio, hora_fin, disponible) VALUES
(1, '2024-01-08', '09:00:00', '10:00:00', TRUE),
(1, '2024-01-08', '10:00:00', '11:00:00', TRUE),
(1, '2024-01-08', '11:00:00', '12:00:00', TRUE),
(1, '2024-01-08', '14:00:00', '15:00:00', TRUE),
(1, '2024-01-08', '15:00:00', '16:00:00', TRUE),
(1, '2024-01-08', '16:00:00', '17:00:00', TRUE),

(1, '2024-01-09', '09:00:00', '10:00:00', TRUE),
(1, '2024-01-09', '10:00:00', '11:00:00', TRUE),
(1, '2024-01-09', '11:00:00', '12:00:00', TRUE),
(1, '2024-01-09', '14:00:00', '15:00:00', TRUE),
(1, '2024-01-09', '15:00:00', '16:00:00', TRUE),
(1, '2024-01-09', '16:00:00', '17:00:00', TRUE);

-- Horarios para María López (Martes y Jueves)
INSERT INTO horario_disponible (psicologo_id, fecha, hora_inicio, hora_fin, disponible) VALUES
(3, '2024-01-09', '08:00:00', '09:00:00', TRUE),
(3, '2024-01-09', '09:00:00', '10:00:00', TRUE),
(3, '2024-01-09', '10:00:00', '11:00:00', TRUE),
(3, '2024-01-09', '11:00:00', '12:00:00', TRUE),

(3, '2024-01-11', '08:00:00', '09:00:00', TRUE),
(3, '2024-01-11', '09:00:00', '10:00:00', TRUE),
(3, '2024-01-11', '10:00:00', '11:00:00', TRUE),
(3, '2024-01-11', '11:00:00', '12:00:00', TRUE);

-- Horarios para Lucía Fernández (Lunes, Miércoles, Viernes)
INSERT INTO horario_disponible (psicologo_id, fecha, hora_inicio, hora_fin, disponible) VALUES
(5, '2024-01-08', '09:00:00', '10:00:00', TRUE),
(5, '2024-01-08', '10:00:00', '11:00:00', TRUE),
(5, '2024-01-08', '15:00:00', '16:00:00', TRUE),
(5, '2024-01-08', '16:00:00', '17:00:00', TRUE),

(5, '2024-01-10', '09:00:00', '10:00:00', TRUE),
(5, '2024-01-10', '10:00:00', '11:00:00', TRUE),
(5, '2024-01-10', '15:00:00', '16:00:00', TRUE),
(5, '2024-01-10', '16:00:00', '17:00:00', TRUE),

(5, '2024-01-12', '09:00:00', '10:00:00', TRUE),
(5, '2024-01-12', '10:00:00', '11:00:00', TRUE),
(5, '2024-01-12', '15:00:00', '16:00:00', TRUE),
(5, '2024-01-12', '16:00:00', '17:00:00', TRUE);

-- Insertar Usuario Administrador por defecto
INSERT INTO usuario (nombre, apellido, email, password, telefono, rol, estado) VALUES
('Sistema', 'Administrador', 'admin@consultorio.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM1JatizYEjdXCGTIQTa', '+51 999 999 999', 'ADMIN', TRUE);
-- Contraseña: admin123

-- Insertar algunos usuarios pacientes de ejemplo
INSERT INTO usuario (nombre, apellido, email, password, telefono, rol, estado) VALUES
('Juan', 'Pérez García', 'juan.perez@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM1JatizYEjdXCGTIQTa', '+51 987 123 456', 'PACIENTE', TRUE),
('María', 'González López', 'maria.gonzalez@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM1JatizYEjdXCGTIQTa', '+51 987 123 457', 'PACIENTE', TRUE),
('Carlos', 'Mendoza Silva', 'carlos.mendoza@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM1JatizYEjdXCGTIQTa', '+51 987 123 458', 'PACIENTE', TRUE),
('Ana', 'Ruiz Torres', 'ana.ruiz@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM1JatizYEjdXCGTIQTa', '+51 987 123 459', 'PACIENTE', TRUE);
-- Contraseña para todos: admin123

-- Insertar algunas citas de ejemplo
INSERT INTO cita (paciente_id, psicologo_id, fecha, hora_inicio, motivo, estado) VALUES
(1, 1, '2024-01-15', '09:00:00', 'Consulta inicial por ansiedad', 'PENDIENTE'),
(1, 1, '2024-01-22', '09:00:00', 'Seguimiento tratamiento ansiedad', 'PENDIENTE'),
(2, 3, '2024-01-16', '10:00:00', 'Terapia cognitivo-conductual para fobias', 'CONFIRMADA'),
(3, 5, '2024-01-17', '15:00:00', 'Evaluación psicológica infantil', 'CONFIRMADA'),
(4, 7, '2024-01-18', '11:00:00', 'Terapia de pareja - sesión inicial', 'PENDIENTE');

-- Actualizar horarios ocupados
UPDATE horario_disponible SET disponible = FALSE WHERE 
(psicologo_id = 1 AND fecha = '2024-01-15' AND hora_inicio = '09:00:00') OR
(psicologo_id = 1 AND fecha = '2024-01-22' AND hora_inicio = '09:00:00') OR
(psicologo_id = 3 AND fecha = '2024-01-16' AND hora_inicio = '10:00:00') OR
(psicologo_id = 5 AND fecha = '2024-01-17' AND hora_inicio = '15:00:00') OR
(psicologo_id = 7 AND fecha = '2024-01-18' AND hora_inicio = '11:00:00');
