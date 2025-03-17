/*
Name: Jerry Wang
Course: CNT 4714 Spring 2025
Assignment title: Project 3 – A Two-tier Client-Server Application
Date: March 14, 2025
Class: AccountantApp
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import com.mysql.cj.jdbc.MysqlDataSource;

// Public Class for the SQL Client Application
public class AccountantApp extends JFrame {
	// Declare UI components
	private JButton connectBtn, clearQuery, executeBtn, clearWindow, closeApp, disconnectBtn;
	private ResultSetTable tableModel;
	private JLabel queryLabel, userLabel, passwordLabel, statusLabel, windowLabel, dbInfoLabel, jdbcLabel, dbPropertiesLabel, userPropertiesLabel;
	private JTextArea queryArea;
	private JTextField userField, dbPropertiesField, propertiesField;
	private JLabel blankLabel;
	private JPasswordField passwordField;
	private JComboBox<String> urlCombo;
	private Connection connection;
	private TableModel empty;
	private JTable resultTable;

	// GUI Constructor
	public AccountantApp() {
		setName("Project 3");
		setTitle("Specialized Accountant Application - (MJL - CNT4714 - Spring 2025 - Project 3)");
		setSize(900, 650);
		blankLabel = new JLabel();

		// Initialize UI Components
		// Connection Details Label
		dbInfoLabel = new JLabel("Connections Details");
		dbInfoLabel.setFont(new Font("Arial", Font.BOLD, 12));
		dbInfoLabel.setForeground(Color.blue);
		setLayout(null);
		dbInfoLabel.setBounds(10, 2, 200, 20); // Adjust position and size as needed
		add(dbInfoLabel);

		// Self enter the properties for Accountant GUI
		dbPropertiesField = new JTextField("");
		propertiesField = new JTextField("");
		
		// Password Text Area
		queryArea = new JTextArea("");
		queryArea.setEnabled(false);
		userField = new JTextField("");
		userField.setForeground(Color.WHITE);
		userField.setBackground(Color.BLACK);
		passwordField = new JPasswordField();
		passwordField.setForeground(Color.WHITE);
		passwordField.setBackground(Color.BLACK);

		// Connect Button
		connectBtn = new JButton("Connect to Database");
		connectBtn.setFont(new Font("Arial", Font.BOLD, 12));
		connectBtn.setForeground(Color.YELLOW);
		connectBtn.setBackground(Color.BLUE);

		// Clear Query Button
		clearQuery = new JButton("Clear SQL Command");
		clearQuery.setFont(new Font("Arial", Font.BOLD, 12));
		clearQuery.setForeground(Color.BLACK);
		clearQuery.setBackground(Color.YELLOW);
		clearQuery.setEnabled(false);

		// Execute Button
		executeBtn = new JButton("Execute SQL Command");
		executeBtn.setFont(new Font("Arial", Font.BOLD, 12));
		executeBtn.setForeground(Color.BLACK);
		executeBtn.setBackground(Color.GREEN);
		executeBtn.setEnabled(false);

		// Clear Result Button
		clearWindow = new JButton("Clear Result Window");
		clearWindow.setFont(new Font("Arial", Font.BOLD, 12));
		clearWindow.setForeground(Color.BLACK);
		clearWindow.setBackground(Color.YELLOW);
		clearWindow.setEnabled(false);

		// Close Application Button
		closeApp = new JButton("Close Application");
		closeApp.setFont(new Font("Arial", Font.BOLD, 12));
		closeApp.setForeground(Color.BLACK);
		closeApp.setBackground(Color.RED);
		closeApp.setEnabled(true);

		// Disconnect Button
		disconnectBtn = new JButton("Disconnect");
		disconnectBtn.setFont(new Font("Arial", Font.BOLD, 12));
		disconnectBtn.setForeground(Color.BLACK);
		disconnectBtn.setBackground(Color.RED);
		disconnectBtn.setEnabled(false);

		// Enter Command Box
		queryLabel = new JLabel("Enter an SQL Command");
		queryLabel.setFont(new Font("Arial", Font.BOLD, 12));
		queryLabel.setForeground(Color.BLUE);

		jdbcLabel = new JLabel("User Properties");
		jdbcLabel.setOpaque(true);
		jdbcLabel.setForeground(Color.BLACK);
		jdbcLabel.setBackground(Color.LIGHT_GRAY);

		// DB Properties label
		dbPropertiesLabel = new JLabel("DB URL Properties");
		dbPropertiesLabel.setForeground(Color.BLACK);
		dbPropertiesLabel.setBackground(Color.LIGHT_GRAY);
		dbPropertiesLabel.setOpaque(true);

		// User Properties label
		userPropertiesLabel = new JLabel("Username Properties");
		userPropertiesLabel.setForeground(Color.BLACK);
		userPropertiesLabel.setBackground(Color.LIGHT_GRAY);
		userPropertiesLabel.setOpaque(true);

		// Username label
		userLabel = new JLabel("Username");
		userLabel.setForeground(Color.BLACK);
		userLabel.setBackground(Color.LIGHT_GRAY);
		userLabel.setOpaque(true);

		// Password Label + Combo
		urlCombo = new JComboBox<>();
		passwordLabel = new JLabel("Password");
		passwordLabel.setForeground(Color.BLACK);
		passwordLabel.setBackground(Color.LIGHT_GRAY);
		passwordLabel.setOpaque(true);

		// Status Label
		statusLabel = new JLabel(" No Connection Established");
		statusLabel.setForeground(Color.RED);
		statusLabel.setBackground(Color.LIGHT_GRAY);
		statusLabel.setOpaque(true);

		// Execution Window Label
		windowLabel = new JLabel("SQL Execution Result Window");
		windowLabel.setFont(new Font("Arial", Font.BOLD, 12));
		windowLabel.setForeground(Color.BLUE);

		// Execution table
		resultTable = new JTable(tableModel);
		JScrollPane square = new JScrollPane(resultTable);
		square.setOpaque(true);
		square.setBackground(Color.WHITE);

		// Button pos
		connectBtn.setBounds(20, 150, 165, 25);
		clearQuery.setBounds(470, 150, 165, 25);
		executeBtn.setBounds(680, 150, 170, 25);
		clearWindow.setBounds(20, 565, 165, 25);
		closeApp.setBounds(705, 565, 165, 25);
		disconnectBtn.setBounds(200, 150, 165, 25);

		// Label pos
		userLabel.setBounds(10, 78, 125, 25);
		passwordLabel.setBounds(10, 107, 125, 24);
		jdbcLabel.setBounds(10, 50, 125, 25);
		dbPropertiesLabel.setBounds(10, 21, 125, 25);
		statusLabel.setBounds(20, 187, 850, 25);
		windowLabel.setBounds(20, 220, 220, 25);
		square.setBounds(20, 240, 850, 320);
		queryLabel.setBounds(450, 0, 215, 25);
		queryArea.setBounds(450, 21, 420, 120);
		propertiesField.setBounds(135, 50, 290, 25);
		dbPropertiesField.setBounds(135, 21, 290, 25);
		urlCombo.setBounds(135, 49, 290, 25);
		userField.setBounds(135, 78, 290, 25);
		passwordField.setBounds(135, 106, 290, 25);

		// Add all
		add(connectBtn);
		add(disconnectBtn);
		add(clearQuery);
		add(executeBtn);
		add(queryLabel);
		add(square);
		add(queryArea);
		add(dbInfoLabel);
		add(jdbcLabel);
		add(propertiesField);
		add(dbPropertiesLabel);
		add(dbPropertiesField); 
		add(userLabel);
		add(userField);
		add(passwordLabel);
		add(passwordField);
		add(statusLabel);
		add(windowLabel);
		add(clearWindow);
		add(closeApp);
		add(blankLabel);

		// Disconnect button
		disconnectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				disconnectFromDatabase();
				dbPropertiesField.setEnabled(true);
			}
		});

		// Connect button
		connectBtn.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent event) {
				boolean usernameMatch = false;
				boolean passwordMatch = false;
				try {
					// If connected,close connection
					if (connection != null) {
						connection.close();
					}
					statusLabel.setText(" No Connection Established");
					statusLabel.setForeground(Color.RED);

					Properties properties = new Properties();
					FileInputStream filein = null;
					MysqlDataSource dataSource = null;
					Connection connectionToOpLog = null;

					// Read user properties
					try {
						filein = new FileInputStream((String) propertiesField.getText());
						properties.load(filein);

						// set the parameters: get url dataSource.setUrl(properties.getProperty("MYSQL_DB_URL"));
						dataSource = new MysqlDataSource();
						
						// Match credentials to properties
						if (userField.getText().equals((String) properties.getProperty("MYSQL_DB_USERNAME"))) {
							usernameMatch = true;
							if (passwordField.getText().equals((String) properties.getProperty("MYSQL_DB_PASSWORD"))) {
								passwordMatch = true;
							}
						}

						// If credentials match log the user in.
						if (passwordMatch && usernameMatch) {
							dataSource.setUser((String) properties.getProperty("MYSQL_DB_USERNAME"));
							dataSource.setPassword((String) properties.getProperty("MYSQL_DB_PASSWORD"));

							// establish a connection
							connection = dataSource.getConnection();
							// update status label
							statusLabel.setText(" CONNECTED TO: " + (String) properties.getProperty("MYSQL_DB_URL"));
							statusLabel.setText(" CONNECTED TO: " + (String) properties.getProperty("MYSQL_DB_URL"));
							statusLabel.setForeground(Color.BLACK);
							clearQuery.setEnabled(true);
							executeBtn.setEnabled(true);
							clearWindow.setEnabled(true);
							closeApp.setEnabled(true);
							queryArea.setEnabled(true);
							// Enable the Disconnect button when connected
							disconnectBtn.setEnabled(true);

							userField.setEditable(false);
							passwordField.setEditable(false);
							connectBtn.setEnabled(false);
							propertiesField.setEnabled(false);
							dbPropertiesField.setEnabled(false);
						} else {
							statusLabel.setText("NOT CONNECTED - User Credentials Do Not Match Properties File");
						}
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
					}

					// Read db properties file
					try {
						filein = new FileInputStream((String) dbPropertiesField.getText());
						properties.load(filein);

						// set the database parameters
						dataSource.setUrl(properties.getProperty("MYSQL_DB_URL"));

						// establish a connection to the database
						connection = dataSource.getConnection();
						// update status label
						statusLabel.setText(" CONNECTED TO: " + (String) properties.getProperty("MYSQL_DB_URL"));
						statusLabel.setForeground(Color.BLACK);
						clearQuery.setEnabled(true);
						executeBtn.setEnabled(true);
						clearWindow.setEnabled(true);
						closeApp.setEnabled(true);
						queryArea.setEnabled(true);
						// Enable the Disconnect button when connected
						disconnectBtn.setEnabled(true);

						userField.setEditable(false);
						passwordField.setEditable(false);
						connectBtn.setEnabled(false);
						propertiesField.setEnabled(false);

					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Clear Query Button
		clearQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				queryArea.setText("");
			}
		});

		// Clear Result button
		clearWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				empty = new DefaultTableModel();
				resultTable.setModel(empty);
			}
		});

		// Close Application
		closeApp.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent event) {
        		try {
            		if (connection != null && !connection.isClosed()) {
                		connection.close();
                		queryArea.append("Disconnected from database.\n");
            		}
        		} catch (SQLException ex) {
            		queryArea.append("Error closing connection: " + ex.getMessage() + "\n");
        		}
        		setVisible(false); // Close the window
        		dispose(); // Release resources
        		System.exit(0); // Exit the program
    		}
		});

		// Execute Button
		executeBtn.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent event) {
				try {
					String query = queryArea.getText();
					System.out.println("Executing query: " + query);

					if (query.toLowerCase().startsWith("select")) {
						System.out.println("Executing SELECT query");
						tableModel = new ResultSetTable(connection, query);
						tableModel.setQuery(query);
					} else {
						System.out.println("Executing UPDATE/INSERT/DELETE query");
						tableModel = new ResultSetTable(connection, query);
						tableModel.setUpdate(query);
						empty = new DefaultTableModel();
						resultTable.setModel(empty);
					}

					resultTable.setModel(tableModel);

				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
				} catch (ClassNotFoundException NotFound) {
					JOptionPane.showMessageDialog(null, "MySQL driver not found", "Driver not found", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	// Disconnect method
	private void disconnectFromDatabase() {
		try {
			if (connection != null) {
				connection.close();
			}
			connection = null;
			statusLabel.setText(" No Connection Established");
			statusLabel.setForeground(Color.RED);
			clearQuery.setEnabled(false);
			executeBtn.setEnabled(false);
			clearWindow.setEnabled(false);
			closeApp.setEnabled(true);
			queryArea.setEnabled(false);
			userField.setEditable(true);
			passwordField.setEditable(true);
			connectBtn.setEnabled(true);
			propertiesField.setEnabled(true);
			disconnectBtn.setEnabled(false);

			// Clear the result table
			empty = new DefaultTableModel();
			resultTable.setModel(empty);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Main
	public static void main(String[] args) {
		AccountantApp project = new AccountantApp();
		project.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		project.setVisible(true);
	}
}
