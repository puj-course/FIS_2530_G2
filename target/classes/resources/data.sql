-- =====================================================
-- data.sql - Datos de prueba para tabla usuarios
-- =====================================================
-- Coloca este archivo en: src/main/resources/data.sql
-- Spring Boot lo ejecutará automáticamente al iniciar con H2
-- =====================================================

-- USUARIO 1: Usuario COMPLETO con todos los campos llenos
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'jperez', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Juan', 'Pérez', 'juan.perez@email.com', 'CC', '1234567890', 'Calle 123 #45-67', true, '2024-01-15 10:30:00');

-- USUARIO 2: Usuario con información PARCIAL (sin email ni dirección)
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'mrodriguez', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'María', 'Rodríguez', NULL, 'CC', '9876543210', NULL, true, '2024-02-20 14:15:00');

-- USUARIO 3: Usuario con SOLO campos obligatorios (username, hash, nombres, apellidos, tipo_documento, numero_documento)
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('c2eebc99-9c0b-4ef8-bb6d-6bb9bd380a33', 'cgarcia', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Carlos', 'García', NULL, 'TI', '1122334455', NULL, true, '2024-03-10 09:00:00');

-- USUARIO 4: Usuario INACTIVO (activo = false)
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('d3eebc99-9c0b-4ef8-bb6d-6bb9bd380a44', 'lmartinez', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Laura', 'Martínez', 'laura.martinez@email.com', 'CE', '987654321', 'Carrera 50 #30-20', false, '2024-01-05 16:45:00');

-- USUARIO 5: Usuario con tipo documento PASAPORTE
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('e4eebc99-9c0b-4ef8-bb6d-6bb9bd380a55', 'alopez', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Andrea', 'López', 'andrea.lopez@email.com', 'PASAPORTE', 'AB123456', 'Avenida 68 #45-30', true, '2024-04-01 11:20:00');

-- USUARIO 6: Usuario recién creado (sin nombres completos aún)
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('f5eebc99-9c0b-4ef8-bb6d-6bb9bd380a66', 'usuario_nuevo', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Usuario', 'Temporal', NULL, 'CC', '5566778899', NULL, true, '2024-11-01 08:00:00');

-- USUARIO 7: Usuario con email y sin dirección
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('a6eebc99-9c0b-4ef8-bb6d-6bb9bd380a77', 'psanchez', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Pedro', 'Sánchez', 'pedro.sanchez@email.com', 'CC', '6677889900', NULL, true, '2024-05-12 13:30:00');

-- USUARIO 8: Usuario con dirección y sin email
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('b7eebc99-9c0b-4ef8-bb6d-6bb9bd380a88', 'sgomez', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Sofía', 'Gómez', NULL, 'TI', '3344556677', 'Transversal 30 #15-40', true, '2024-06-18 10:15:00');

-- USUARIO 9: Usuario con nombre corto
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('c8eebc99-9c0b-4ef8-bb6d-6bb9bd380a99', 'jdoe', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Jo', 'Doe', 'jo.doe@email.com', 'CE', '1231231234', 'Calle 80 #10-20', true, '2024-07-22 15:00:00');

-- USUARIO 10: Usuario con nombre largo y completo
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('d9eebc99-9c0b-4ef8-bb6d-6bb9bd380aaa', 'jfhernandez', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Juan Francisco', 'Hernández González', 'juan.hernandez@email.com', 'CC', '4455667788', 'Diagonal 45 #25-30 Apartamento 301', true, '2024-08-30 12:45:00');

-- =====================================================
-- CASOS ESPECIALES (para testing de integridad)
-- =====================================================

-- USUARIO 11: Usuario con fecha de creación muy antigua
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380bbb', 'antiguo', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Usuario', 'Antiguo', NULL, 'CC', '7788990011', NULL, true, '2020-01-01 00:00:00');

-- USUARIO 12: Usuario con fecha reciente
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('f1eebc99-9c0b-4ef8-bb6d-6bb9bd380ccc', 'reciente', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Usuario', 'Reciente', 'reciente@email.com', 'TI', '8899001122', 'Calle 100 #50-25', true, '2024-11-04 18:30:00');

-- USUARIO 13: Usuario inactivo con todos los datos
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('a2eebc99-9c0b-4ef8-bb6d-6bb9bd380ddd', 'inactivo_completo', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Usuario', 'Desactivado', 'desactivado@email.com', 'CC', '2233445566', 'Avenida 30 #40-50', false, '2023-12-15 10:00:00');

-- USUARIO 14: Usuario con username muy corto
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('b3eebc99-9c0b-4ef8-bb6d-6bb9bd380eee', 'abc', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Test', 'User', NULL, 'CE', '3344556688', NULL, true, '2024-09-10 14:20:00');

-- USUARIO 15: Usuario con todos los tipos de documento diferentes
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('c4eebc99-9c0b-4ef8-bb6d-6bb9bd380fff', 'rnino', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Ricardo', 'Niño', 'ricardo.nino@email.com', 'RC', '1010101010', 'Calle 70 #20-15', true, '2024-10-05 09:30:00');

-- =====================================================
-- ENFERMERAS (usuarios + datos específicos)
-- =====================================================

-- Enfermera 1: AUXILIAR
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('11111111-1111-1111-1111-111111111111', 'egarcia', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Elena', 'García', 'elena.garcia@hospital.com', 'CC', '1111111111', 'Calle Hospital #10-20', true, '2024-01-10 08:00:00');
INSERT INTO enfermeras (id, nivel)
VALUES ('11111111-1111-1111-1111-111111111111', 'AUXILIAR');

-- Enfermera 2: JEFE
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('22222222-2222-2222-2222-222222222222', 'mlopez', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'María', 'López', 'maria.lopez@hospital.com', 'CC', '2222222222', 'Avenida Central #30-40', true, '2024-02-15 09:30:00');
INSERT INTO enfermeras (id, nivel)
VALUES ('22222222-2222-2222-2222-222222222222', 'JEFE');

-- Enfermera 3: GENERAL
INSERT INTO usuarios (id, username, hash, nombres, apellidos, email, tipo_documento, numero_documento, direccion, activo, creado_en)
VALUES ('33333333-3333-3333-3333-333333333333', 'cramos', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Carmen', 'Ramos', 'carmen.ramos@hospital.com', 'CE', '3333333333', 'Carrera 50 #20-30', true, '2024-03-20 10:00:00');
INSERT INTO enfermeras (id, nivel)
VALUES ('33333333-3333-3333-3333-333333333333', 'GENERAL');