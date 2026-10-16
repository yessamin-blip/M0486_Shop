package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Employee;
import model.Product;

public interface Dao {
	
	public void connect();

	public void disconnect();

	public Employee getEmployee(int employeeId, String password);

	// inventory
	public ArrayList<Product> getInventory();

	// recibe el inventario y devuelve true si se ha guardado bien o false si ha fallado
	public boolean writeInventory(ArrayList<Product> inventory);

	// CRUD employee
	public ArrayList<Employee> getEmployees() throws SQLException;

	public Employee getEmployeeById(int employeeId) throws SQLException;

	public void addEmployee(Employee employee) throws SQLException;

	public void updateEmployee(Employee employee) throws SQLException;

	public void deleteEmployee(int employeeId) throws SQLException;

	// create employee table and insert initial employees
	public ArrayList<Employee> seedEmployee() throws SQLException;
}
