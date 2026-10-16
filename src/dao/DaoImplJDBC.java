package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Employee;
import model.Product;

public class DaoImplJDBC implements Dao {
	Connection connection;

	// initial employees inserted by seedEmployee()
	private static final Employee[] SEED_EMPLOYEES = {
			new Employee(123, "Test", "test")
	};

	@Override
	public void connect() {
		// Define connection parameters
		String url = "jdbc:mysql://localhost:3306/shop?createDatabaseIfNotExist=true";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		Employee employee = null;
		String query = "select * from employee where employeeId= ? and password = ? ";
		// no connection, login fails instead of NullPointerException
		if (connection == null) {
			return null;
		}
		
		try (PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setInt(1,employeeId);
    	  	ps.setString(2,password);
    	  	//System.out.println(ps.toString());
            try (ResultSet rs = ps.executeQuery()) {
            	if (rs.next()) {
            		employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
            	}
            }
        } catch (SQLException e) {
			// in case error in SQL
			e.printStackTrace();
		}
    	return employee;
	}

	@Override
	public ArrayList<Product> getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		// no lo uso aquí porque el inventario va en fichero, lo pongo porque lo pide la interface
		return false;
	}

	@Override
	public ArrayList<Employee> getEmployees() throws SQLException {
		checkConnection();
		ArrayList<Employee> employees = new ArrayList<>();
		String query = "select employeeId, name, password from employee order by employeeId";

		try (Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(query)) {
			while (rs.next()) {
				employees.add(new Employee(rs.getInt("employeeId"), rs.getString("name"), rs.getString("password")));
			}
		}
		return employees;
	}

	@Override
	public Employee getEmployeeById(int employeeId) throws SQLException {
		checkConnection();
		Employee employee = null;
		String query = "select employeeId, name, password from employee where employeeId = ?";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, employeeId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					employee = new Employee(rs.getInt("employeeId"), rs.getString("name"), rs.getString("password"));
				}
			}
		}
		return employee;
	}

	@Override
	public void addEmployee(Employee employee) throws SQLException {
		checkConnection();
		String query = "insert into employee (employeeId, name, password) values (?, ?, ?)";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, employee.getEmployeeId());
			ps.setString(2, employee.getName());
			ps.setString(3, employee.getPassword());
			ps.executeUpdate();
		}
	}

	@Override
	public void updateEmployee(Employee employee) throws SQLException {
		checkConnection();
		String query = "update employee set name = ?, password = ? where employeeId = ?";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, employee.getName());
			ps.setString(2, employee.getPassword());
			ps.setInt(3, employee.getEmployeeId());
			if (ps.executeUpdate() == 0) {
				throw new SQLException("No existe el empleado " + employee.getEmployeeId());
			}
		}
	}

	@Override
	public void deleteEmployee(int employeeId) throws SQLException {
		checkConnection();
		String query = "delete from employee where employeeId = ?";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, employeeId);
			if (ps.executeUpdate() == 0) {
				throw new SQLException("No existe el empleado " + employeeId);
			}
		}
	}

	/**
	 * connect() only prints the error, so fail here with a clear message
	 */
	private void checkConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			throw new SQLException("No hay conexión con la base de datos");
		}
	}

	@Override
	public ArrayList<Employee> seedEmployee() throws SQLException {
		checkConnection();
		ArrayList<Employee> created = new ArrayList<>();
		String createTable = "create table if not exists employee ("
				+ "employeeId int not null primary key, "
				+ "name varchar(100) not null, "
				+ "password varchar(100) not null)";
		String insert = "insert ignore into employee (employeeId, name, password) values (?, ?, ?)";

		try (Statement st = connection.createStatement()) {
			st.executeUpdate(createTable);
		}
		try (PreparedStatement ps = connection.prepareStatement(insert)) {
			for (Employee employee : SEED_EMPLOYEES) {
				ps.setInt(1, employee.getEmployeeId());
				ps.setString(2, employee.getName());
				ps.setString(3, employee.getPassword());
				// 0 rows means employee already existed
				if (ps.executeUpdate() == 1) {
					created.add(getEmployeeById(employee.getEmployeeId()));
				}
			}
		}
		return created;
	}

}
