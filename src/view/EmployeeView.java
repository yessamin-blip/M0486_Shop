package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.Dao;
import dao.DaoImplJDBC;
import model.Employee;

public class EmployeeView extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	// connection using JDBC SQL
	private Dao dao = new DaoImplJDBC();

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldId;
	private JTextField textFieldName;
	private JPasswordField passwordField;
	private JTable table;
	private DefaultTableModel tableModel;
	private JButton btnAdd;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JButton btnClear;
	private JButton btnClose;

	/**
	 * Create the dialog.
	 */
	public EmployeeView() {
		setTitle("Gestión de empleados");
		setBounds(100, 100, 600, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		// id section
		JLabel lblId = new JLabel("Número empleado:");
		lblId.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblId.setBounds(33, 10, 140, 19);
		contentPanel.add(lblId);
		textFieldId = new JTextField();
		textFieldId.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldId.setBounds(180, 10, 150, 25);
		contentPanel.add(textFieldId);

		// name section
		JLabel lblName = new JLabel("Nombre:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblName.setBounds(33, 45, 140, 19);
		contentPanel.add(lblName);
		textFieldName = new JTextField();
		textFieldName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldName.setBounds(180, 45, 150, 25);
		contentPanel.add(textFieldName);

		// password section
		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPassword.setBounds(33, 80, 140, 19);
		contentPanel.add(lblPassword);
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		passwordField.setBounds(180, 80, 150, 25);
		contentPanel.add(passwordField);

		// CRUD buttons
		btnAdd = new JButton("Añadir");
		btnAdd.setBounds(360, 10, 190, 25);
		contentPanel.add(btnAdd);
		btnAdd.addActionListener(this);

		btnUpdate = new JButton("Modificar");
		btnUpdate.setBounds(360, 45, 190, 25);
		contentPanel.add(btnUpdate);
		btnUpdate.addActionListener(this);

		btnDelete = new JButton("Eliminar");
		btnDelete.setBounds(360, 80, 190, 25);
		contentPanel.add(btnDelete);
		btnDelete.addActionListener(this);

		btnClear = new JButton("Limpiar");
		btnClear.setBounds(360, 115, 190, 25);
		contentPanel.add(btnClear);
		btnClear.addActionListener(this);

		// employee list, password is not shown
		tableModel = new DefaultTableModel(new Object[] { "Número", "Nombre" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// fill form with selected employee
		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
				loadSelectedEmployee();
			}
		});
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(33, 155, 517, 200);
		contentPanel.add(scrollPane);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			btnClose = new JButton("Cerrar");
			buttonPane.add(btnClose);
			btnClose.addActionListener(this);
		}

		// open connection while the dialog is alive
		dao.connect();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				dao.disconnect();
			}
		});

		loadEmployees();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAdd) {
			addEmployee();
		}
		if (e.getSource() == btnUpdate) {
			updateEmployee();
		}
		if (e.getSource() == btnDelete) {
			deleteEmployee();
		}
		if (e.getSource() == btnClear) {
			clearForm();
		}
		if (e.getSource() == btnClose) {
			// release current screen
			dispose();
		}
	}

	/**
	 * read all employees from database into the table
	 */
	private void loadEmployees() {
		tableModel.setRowCount(0);
		try {
			for (Employee employee : dao.getEmployees()) {
				tableModel.addRow(new Object[] { employee.getEmployeeId(), employee.getName() });
			}
		} catch (SQLException ex) {
			showError(ex.getMessage());
		}
	}

	/**
	 * fill form with the employee selected in the table
	 */
	private void loadSelectedEmployee() {
		int employeeId = (int) tableModel.getValueAt(table.getSelectedRow(), 0);
		try {
			Employee employee = dao.getEmployeeById(employeeId);
			if (employee != null) {
				textFieldId.setText(String.valueOf(employee.getEmployeeId()));
				textFieldId.setEditable(false);
				textFieldName.setText(employee.getName());
				passwordField.setText(employee.getPassword());
			}
		} catch (SQLException ex) {
			showError(ex.getMessage());
		}
	}

	private void addEmployee() {
		Employee employee = readForm();
		if (employee == null) {
			return;
		}
		try {
			if (dao.getEmployeeById(employee.getEmployeeId()) != null) {
				showError("Empleado ya existe");
				return;
			}
			dao.addEmployee(employee);
			JOptionPane.showMessageDialog(this, "Empleado añadido", "Information", JOptionPane.INFORMATION_MESSAGE);
			loadEmployees();
			clearForm();
		} catch (SQLException ex) {
			showError(ex.getMessage());
		}
	}

	private void updateEmployee() {
		Employee employee = readForm();
		if (employee == null) {
			return;
		}
		try {
			dao.updateEmployee(employee);
			JOptionPane.showMessageDialog(this, "Empleado modificado", "Information", JOptionPane.INFORMATION_MESSAGE);
			loadEmployees();
			clearForm();
		} catch (SQLException ex) {
			showError(ex.getMessage());
		}
	}

	private void deleteEmployee() {
		int employeeId;
		try {
			employeeId = Integer.parseInt(textFieldId.getText().trim());
		} catch (NumberFormatException ex) {
			showError("Seleccione o introduzca un número de empleado válido");
			return;
		}
		int answer = JOptionPane.showConfirmDialog(this, "¿Eliminar el empleado " + employeeId + "?", "Confirmar",
				JOptionPane.YES_NO_OPTION);
		if (answer != JOptionPane.YES_OPTION) {
			return;
		}
		try {
			dao.deleteEmployee(employeeId);
			JOptionPane.showMessageDialog(this, "Empleado eliminado", "Information", JOptionPane.INFORMATION_MESSAGE);
			loadEmployees();
			clearForm();
		} catch (SQLException ex) {
			showError(ex.getMessage());
		}
	}

	/**
	 * validate form data
	 * @return employee from form or null if data is not valid
	 */
	private Employee readForm() {
		int employeeId;
		try {
			employeeId = Integer.parseInt(textFieldId.getText().trim());
		} catch (NumberFormatException ex) {
			showError("El número de empleado debe ser numérico");
			return null;
		}
		String name = textFieldName.getText().trim();
		String password = new String(passwordField.getPassword());
		if (name.isEmpty() || password.isEmpty()) {
			showError("Nombre y contraseña son obligatorios");
			return null;
		}
		return new Employee(employeeId, name, password);
	}

	private void clearForm() {
		table.clearSelection();
		textFieldId.setText("");
		textFieldId.setEditable(true);
		textFieldName.setText("");
		passwordField.setText("");
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

}
