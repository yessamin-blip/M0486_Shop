package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Shop;
import utils.Constants;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;

public class ShopView extends JFrame implements ActionListener, KeyListener{

	private static final long serialVersionUID = 1L;
	private Shop shop;
	
	private JPanel contentPane;
	private JButton btnShowCash;
	private JButton btnAddProduct;
	private JButton btnAddStock;
	private JButton btnManageEmployees;
	private JButton btnRemoveProduct;
	private JButton btnExportInventory;
	
	
	

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShopView frame = new ShopView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ShopView() {
		setTitle("MiTenda.com - Menu principal");
		// listen key
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		// create shop
		shop = new Shop();
		shop.loadInventory();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblShowCash = new JLabel("Seleccione o pulse una opción:");
		lblShowCash.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblShowCash.setBounds(57, 20, 236, 14);
		contentPane.add(lblShowCash);
		
		// option count cash
		btnShowCash = new JButton("1. Contar caja");
		btnShowCash.setHorizontalAlignment(SwingConstants.LEFT);
		btnShowCash.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnShowCash.setBounds(99, 40, 236, 40);
		contentPane.add(btnShowCash);
		// listen button
		btnShowCash.addActionListener(this);
		
		// option add product
		btnAddProduct = new JButton("2. Añadir producto");
		btnAddProduct.setHorizontalAlignment(SwingConstants.LEFT);
		btnAddProduct.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAddProduct.setBounds(99, 90, 236, 40);
		contentPane.add(btnAddProduct);
		// listen button
		btnAddProduct.addActionListener(this);
		
		// option add stock
		btnAddStock = new JButton("3. Añadir stock");
		btnAddStock.setHorizontalAlignment(SwingConstants.LEFT);
		btnAddStock.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAddStock.setBounds(99, 140, 236, 40);
		contentPane.add(btnAddStock);
		// listen button
		btnAddStock.addActionListener(this);

		// option manage employees
		btnManageEmployees = new JButton("4. Gestionar empleados");
		btnManageEmployees.setHorizontalAlignment(SwingConstants.LEFT);
		btnManageEmployees.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnManageEmployees.setBounds(99, 190, 236, 40);
		contentPane.add(btnManageEmployees);
		// listen button
		btnManageEmployees.addActionListener(this);

		// option add product
		btnRemoveProduct = new JButton("9. Eliminar producto");
		btnRemoveProduct.setHorizontalAlignment(SwingConstants.LEFT);
		btnRemoveProduct.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnRemoveProduct.setBounds(99, 240, 236, 40);
		contentPane.add(btnRemoveProduct);
		// listen button
		btnRemoveProduct.addActionListener(this);

		// option export inventory
		btnExportInventory = new JButton("0. Exportar inventario");
		btnExportInventory.setHorizontalAlignment(SwingConstants.LEFT);
		btnExportInventory.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnExportInventory.setBounds(99, 290, 236, 40);
		contentPane.add(btnExportInventory);
		// listen button
		btnExportInventory.addActionListener(this);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyChar() == '1' ) {
			this.openCashView();	
        }
		if (e.getKeyChar() == '2') {
			this.openProductView(Constants.OPTION_ADD_PRODUCT);	
		}
		if (e.getKeyChar() == '3') {
			this.openProductView(Constants.OPTION_ADD_STOCK);
		}
		if (e.getKeyChar() == '4') {
			this.openEmployeeView();
		}
		if (e.getKeyChar() == '9') {
			this.openProductView(Constants.OPTION_REMOVE_PRODUCT);
		}
		// key 0: export inventory to a file
		if (e.getKeyChar() == '0') {
			this.exportInventory();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnShowCash) {
			this.openCashView();						
		}
		if (e.getSource() == btnAddProduct) {
			this.openProductView(Constants.OPTION_ADD_PRODUCT);						
		}
		if (e.getSource() == btnAddStock) {
			this.openProductView(Constants.OPTION_ADD_STOCK);				
		}
		if (e.getSource() == btnManageEmployees) {
			this.openEmployeeView();
		}
		if (e.getSource() == btnRemoveProduct) {
			this.openProductView(Constants.OPTION_REMOVE_PRODUCT);
		}
		// button 0: export inventory to a file
		if (e.getSource() == btnExportInventory) {
			this.exportInventory();
		}
		
	}
	
	/**
	 * open dialog to show shop cash
	 */
	public void openCashView() {
		// create a dialog Box
        CashView dialog = new CashView(shop);  
        // setsize of dialog
        dialog.setSize(400, 400);
        // set visibility of dialog
        dialog.setModal(true);
        dialog.setVisible(true);
	}
	
	/**
	 * open dialog to add/remove/stock product
	 */
	public void openProductView(int option) {
		// create a dialog Box
		ProductView dialog = new ProductView(shop, option);  
        // setsize of dialog
        dialog.setSize(400, 400);
        // set visibility of dialog
        dialog.setModal(true);
        dialog.setVisible(true);
	}
	
	
	/**
	 * open dialog to create/read/update/delete employees
	 */
	public void openEmployeeView() {
		EmployeeView dialog = new EmployeeView();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	/**
	 * export inventory to a file and show if it worked or not
	 */
	public void exportInventory() {
		// writeInventory returns true if the file was written correctly
		if (shop.writeInventory()) {
			// show confirmation panel
			JOptionPane.showMessageDialog(this, "Inventario exportado correctamente",
					"Exportar inventario", JOptionPane.INFORMATION_MESSAGE);
		} else {
			// show error panel
			JOptionPane.showMessageDialog(this, "Error al exportar el inventario",
					"Exportar inventario", JOptionPane.ERROR_MESSAGE);
		}
	}

}
