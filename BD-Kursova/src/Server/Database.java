package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
	private String host;
	private String database;
	private String user;
	private String pass;
	private Connection conn;
	
	public Database(String host, String database, String user, String pass){
		this.host = host;
		this.database = database;
		this.user = user;
		this.pass = pass;
	}
	
	public void connect() throws SQLException{
		if(conn != null) return;
		PreparedStatement ps = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String url = "jdbc:mysql://" + this.host;

			try {
				conn = DriverManager.getConnection(url, this.user, this.pass);
				System.out.println("Succesful connect: " + url);
			} catch (SQLException e) {
				System.out.println("Error connecting to database");
			}
			
			databaseFallback(ps);

		} catch (ClassNotFoundException e) {
			System.out.println("Driver not found");
		}
	}
	
	public void disconnect(){
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("Error closing connection");
			}
		}
	}
	
	public int query(String sqlQuery, String... params){
		int affectedRows = 0;
		
		if(conn != null){
			try {
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sqlQuery);
				
				int paramCounter = 0;
				for(String param: params){
					paramCounter++;
					
					ps.setString(paramCounter, param);
				}
				
				affectedRows = ps.executeUpdate();
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				printSqlException(e);
				rollback();
			}	
		}
		
		return affectedRows;
	}
	
	public ResultSet selectQuery(String sqlQuery, String... params){
		ResultSet result = null;
		
		if(conn != null){
			try {
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sqlQuery);
				
				int paramCounter = 0;
				for(String param: params){
					paramCounter++;
					
					ps.setString(paramCounter, param);
				}
				
				result = ps.executeQuery();
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				printSqlException(e);
				rollback();
			}
		}
		
		return result;
	}
	
	public static boolean ignoreSQLException(String sqlState) {

	    if (sqlState == null) {
	        System.out.println("The SQL state is not defined!");
	        return false;
	    }
	    // HY000: Database exist
	    if(sqlState.equalsIgnoreCase("HY000"))
	    	return true;
	    
	    // X0Y32: Jar file already exists in schema
	    if (sqlState.equalsIgnoreCase("X0Y32"))
	        return true;

	    // 42Y55: Table already exists in schema
	    if (sqlState.equalsIgnoreCase("42Y55"))
	        return true;

	    return false;
	}
	
	public void printSqlException(SQLException e){
		if (ignoreSQLException(
                ((SQLException)e).
                getSQLState()) == false) {

                //e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                    ((SQLException)e).getSQLState());

                System.err.println("Error Code: " +
                    ((SQLException)e).getErrorCode());

                System.err.println("Message: " + e.getMessage());
            }
	}
	
	public void databaseFallback(PreparedStatement ps) throws SQLException{
		try {
			conn.setAutoCommit(false);
			
			String sqlQuery = "CREATE DATABASE " + this.database;
			
			ps = conn.prepareStatement(sqlQuery);
			int result = ps.executeUpdate();
			System.out.println(result);
			if(result != 0){
				System.out.println("Database created");
				sqlQuery = "CREATE TABLE "+ this.database +".users("
							+ "id int NOT NULL AUTO_INCREMENT,"
							+ "username varchar(50) UNIQUE,"
							+ "password varchar(50),"
							+ "PRIMARY KEY(id)"
							+ ");";
				ps = conn.prepareStatement(sqlQuery);
				ps.executeUpdate();
				
				sqlQuery = "CREATE TABLE " + this.database + ".tweets("
							+ "id int NOT NULL AUTO_INCREMENT,"
							+ "tweet varchar(255),"
							+ "userId int NOT NULL,"
							+ "posted datetime DEFAULT CURRENT_TIMESTAMP,"
							+ "PRIMARY KEY(id),"
							+ "FOREIGN KEY(userId) REFERENCES users(id)"
							+ ");";
				ps = conn.prepareStatement(sqlQuery);
				ps.executeUpdate();
				
				System.out.println("Tables created");
			}
			conn.commit();
		} catch (SQLException e) {
			
			printSqlException(e);
			System.out.println("Database already setted up");
			rollback();
	        
	    } finally {
	        if (ps != null) {
	            ps.close();
	        }
	        conn.setAutoCommit(true);
	    }
	}
	
	private void rollback(){
		if (conn != null) {
            try {
                System.err.println("Transaction is being rolled back");
                conn.rollback();
            } catch(SQLException excep) {
            	printSqlException(excep);
            }
        }
	}
	
	public String getDatabaseName(){
		return this.database;
	}
}
