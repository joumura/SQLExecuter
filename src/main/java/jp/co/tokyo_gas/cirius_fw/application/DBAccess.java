package jp.co.tokyo_gas.cirius_fw.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * RDBにDMLを発行する.
 */
public class DBAccess implements AutoCloseable {

	private Connection connection;
	private PreparedStatement stmt;

	public DBAccess(String driver, String url, String user, String password)
			throws ClassNotFoundException {
		Class.forName(driver);
		try {
			if (null == connection || connection.isClosed()) {
				connection = DriverManager.getConnection(url, user, password);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Object execute(String sql) {
		try {
			stmt = connection.prepareStatement(sql);
			if (stmt.execute()) {
				return stmt.getResultSet();
			} else {
				return stmt.getUpdateCount();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Object getMoreResults() {
		try {
			if (stmt.getMoreResults()) {
				return stmt.getResultSet();
			} else {
				return stmt.getUpdateCount();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

//	public ResultSet executeQuery(String sql) {
//		try {
//			PreparedStatement stmt = connection.prepareStatement(sql);
//			return stmt.executeQuery();
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	public int executeUpdate(String sql) {
//		try (PreparedStatement stmt = connection.prepareStatement(sql);) {
//			return stmt.executeUpdate();
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}

	@Override
	public void close() {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void outputAsCsv(ResultSet rs) {
		try {
			ResultSetMetaData meta = rs.getMetaData();
			int numberOfColumns = meta.getColumnCount();

	//		String dataHeaders = "\"" + meta.getColumnName(1) + "\"";
	//		for (int i = 2; i < numberOfColumns + 1; i++) {
	//			dataHeaders += ",\"" + meta.getColumnName(i).replaceAll("\"", "\\\"") + "\"";
	//		}
	//		System.out.println(dataHeaders);

			while (rs.next()) {
				String row = "\"" + rs.getString(1).replaceAll("\"", "\\\"") + "\"";
				for (int i = 2; i < numberOfColumns + 1; i++) {
					row += ",\"" + rs.getString(i).replaceAll("\"", "\\\"") + "\"";
				}
				System.out.println(row);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public void outputResult(String sql) {
		for (Object obj = execute(sql); true; obj = getMoreResults()) {
			if (obj instanceof ResultSet) {
				outputAsCsv((ResultSet) obj);
			} else {
				if (-1 == (int) obj) {
					break;
				} else {
					System.out.println((int) obj);
				}
			}
		}
	}

}
