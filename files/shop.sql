-- Base de datos de la tienda: tabla employee usada por el login y el CRUD de empleados
CREATE DATABASE IF NOT EXISTS shop;
USE shop;

CREATE TABLE IF NOT EXISTS employee (
	employeeId INT NOT NULL PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	password VARCHAR(100) NOT NULL
);

-- empleado de prueba (antes estaba hardcodeado en Employee: 123 / test)
INSERT IGNORE INTO employee (employeeId, name, password) VALUES (123, 'Test', 'test');
