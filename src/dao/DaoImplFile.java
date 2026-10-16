package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplFile implements Dao {

	@Override
	public void connect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * lee el inventario del fichero inputInventory.txt
	 */
	@Override
	public ArrayList<Product> getInventory() {
		
		ArrayList<Product> inventory = new ArrayList<Product>();

		// busco el fichero dentro de la carpeta files del proyecto
		File f = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + "inputInventory.txt");

		// lo abro dentro del try para que se cierre solo al acabar
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			
			// leo la primera línea
			String line = br.readLine();

			// sigo leyendo hasta que no queden líneas
			while (line != null) {
			
				// separo la línea por ; para tener cada dato por separado
				String[] sections = line.split(";");

				String name = "";
				double wholesalerPrice = 0.0;
				int stock = 0;

				// recorro cada trozo de la línea
				for (int i = 0; i < sections.length; i++) {
				
					// separo por : y en la posición 1 queda el valor
					String[] data = sections[i].split(":");

					switch (i) {
					case 0:
					
						// el primero es el nombre del producto
						name = data[1];
						break;

					case 1:
						
						// el segundo es el precio, lo paso a double
						wholesalerPrice = Double.parseDouble(data[1]);
						break;

					case 2:
						
						// el tercero es el stock, lo paso a int
						stock = Integer.parseInt(data[1]);
						break;

					default:
						break;
					}
				}
				
				// creo el producto y lo añado a la lista
				inventory.add(new Product(name, new Amount(wholesalerPrice), true, stock));

				// paso a la siguiente línea
				line = br.readLine();
			}

		} catch (IOException e) {
			
			// si falla al leer el fichero muestro el error
			e.printStackTrace();
		}
		return inventory;
	}

	/**
	 * write inventory in file
	 *
	 * @return true if file written, false if any error
	 */
	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		// define file name based on date
		LocalDate myObj = LocalDate.now();
		String fileName = "inventory_" + myObj.toString() + ".txt";

		// locate file, path and name
		File f = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + fileName);

		// wrap in proper classes, closed automatically
		try (FileWriter fw = new FileWriter(f, false); PrintWriter pw = new PrintWriter(fw)) {
			// write line by line
			int counterProduct = 1;
			for (Product product : inventory) {
				// format line TO BE -> 1;Product:Manzana;Stock:10;
				StringBuilder line = new StringBuilder(
						counterProduct + ";Product:" + product.getName() + ";Stock:" + product.getStock() + ";");
				pw.println(line.toString());

				// increment counter products
				counterProduct++;
			}

			// format last line TO BE -> Total number of products:2;
			pw.println("Total number of products:" + inventory.size() + ";");

			// PrintWriter does not throw, so check for errors explicitly
			return !pw.checkError();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public ArrayList<Employee> getEmployees() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee getEmployeeById(int employeeId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEmployee(Employee employee) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateEmployee(Employee employee) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteEmployee(int employeeId) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Employee> seedEmployee() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
