/*
Name: Jerry Wang
Course: CNT 4714 Spring 2025
Assignment title: Project 3 – A Specialized Accountant Application
Date: March 14, 2025
Class: ResultSetTable
*/

import java.io.FileInputStream;
import javax.swing.table.AbstractTableModel;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import com.mysql.cj.jdbc.MysqlDataSource;

public class ResultSetTable extends AbstractTableModel {
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numOfRows;

	// Track connection status
	private boolean connectedToDb = false;

	// Constructor to initialize ResultSetTable
	public ResultSetTable (Connection c, String query) throws SQLException, ClassNotFoundException {
		// Create statement with scroll and read-only properties
		statement = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		// Mark as connected to the database
		connectedToDb = true;
	}

	// Method to get the class type of a column
	@Override
	public Class getColumnClass (int column) throws IllegalStateException {
		// Check if connected to the database
		if (!connectedToDb) {
			throw new IllegalStateException("Not connected to Datase");
		}

		// Check java clas of column
		try {
			// Get the class name for the column type
			String className = metaData.getColumnClassName(column + 1);

			// Return the class type
			return Class.forName(className);
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}

		// Return Object class if exception occurs
		return Object.class;
	}

	// Method to return the number of rows
	@Override
	public int getRowCount() throws IllegalStateException {
		// Ensure the database connection is active
		if (!connectedToDb) {
			throw new IllegalStateException("Not connected to Datase");
		}

		return numOfRows;
	}

	// Method to return the number of columns
	@Override
	public int getColumnCount() throws IllegalStateException {
		// Ensure the database connection is active
		if (!connectedToDb) {
			throw new IllegalStateException("Not connected to Datase");
		}

		try {
			// Get the number of columns from metadata
			return metaData.getColumnCount();
		}
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		return 0;
	}

	// Method to return the name of a specific column
	@Override
	public String getColumnName(int column) throws IllegalStateException {
		// Ensure the database connection is active
		if (!connectedToDb) {
			throw new IllegalStateException("Not connected to Datase");
		}
		
		try {
			// Get the column name from metadata
			return metaData.getColumnName(column + 1);
		}
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}

		return "";
	}

	// Method to return the value at a specific row and column
    @Override
	public Object getValueAt(int rowIndex, int columnIndex) throws IllegalStateException {
		// Ensure the database connection is active
		if (!connectedToDb) {
			throw new IllegalStateException("Not connected to Datase");
		}
		
		try {
			// Move to the specified row
			resultSet.next();
			resultSet.absolute(rowIndex + 1);

			// Get the object value from the specified column
			return resultSet.getObject(columnIndex + 1);
		}
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}

		// Return an empty string if an error occurs
		return "";
	}


	// Method to execute a query and update the table model
	public void setQuery(String query) throws SQLException, IllegalStateException {
		// Ensure the database connection is active
		if (!connectedToDb) {
			throw new IllegalStateException("Not connected to Database");
		}

		// Execute the query
		resultSet = statement.executeQuery(query);

		// Retrieve metadata from the result set
		metaData = resultSet.getMetaData();

		// Move to the last row to get the number of rows
		resultSet.last(); // Change to last row
		numOfRows = resultSet.getRow(); // Get current row

		// Update the operation log for a query
		updateOpLog("num_queries");

		// Notify the table model that data has changed
		fireTableStructureChanged();
	}

	// Method to execute an update and refresh the table model
	public void setUpdate(String query) throws SQLException, IllegalStateException {
		int res;

		// Ensure the database connection is active
		if (!connectedToDb) {
			throw new IllegalStateException("Not connected to Database");
		}

		// Execute the update query
		res = statement.executeUpdate(query);

		// Update the operation log for an update
		updateOpLog("num_updates");

		// Notify the table model that data has changed
		fireTableStructureChanged();
	}

	// Method to update the operations log
	public void updateOpLog(String para) {
		Properties properties = new Properties();
		FileInputStream filein = null;
		MysqlDataSource dataSource = null;
		Connection connectionToOpLog = null;

		// Read file
		try {
			// Load properties from the file
			filein = new FileInputStream("oplog.properties");
			properties.load(filein);

			// Initialize data source
			dataSource = new MysqlDataSource();
			dataSource.setUrl(properties.getProperty("MYSQL_DB_URL"));
			dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
			dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));

			// Establish connection to the operations log
			connectionToOpLog = dataSource.getConnection();

			// Create statement and update operation count
			Statement opLogsStatement = connectionToOpLog.createStatement();
			opLogsStatement.executeUpdate("UPDATE operationscount set " + para + " = "  + para +" + 1");

			// Close the connection
			connectionToOpLog.close();
		}
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
			System.exit(1);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
