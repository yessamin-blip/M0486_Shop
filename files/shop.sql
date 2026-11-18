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

-- inventario de la tienda (mismo formato que el documento de la practica)
CREATE TABLE IF NOT EXISTS inventory (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	wholesalerPrice DECIMAL(10,2) NOT NULL,
	available TINYINT(1) NOT NULL,
	stock INT NOT NULL
);

-- productos de ejemplo del documento
INSERT IGNORE INTO inventory (id, name, wholesalerPrice, available, stock) VALUES
(1, 'Manzana', 10.00, 1, 10),
(2, 'Fresa', 5.00, 1, 20);
