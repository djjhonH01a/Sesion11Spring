DELETE FROM prestamo;
DELETE FROM libro;
DELETE FROM usuario;

INSERT INTO libro (titulo, autor, disponible) VALUES ('El Quijote', 'Cervantes', true);
INSERT INTO libro (titulo, autor, disponible) VALUES ('Cien años de soledad', 'García Márquez', false);
INSERT INTO usuario (nombre, email) VALUES ('Ana López', 'ana@mail.com');
INSERT INTO usuario (nombre, email) VALUES ('Carlos Ruiz', 'carlos@mail.com');
