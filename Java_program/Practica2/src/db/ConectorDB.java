package db;

import Utils.Output;

import java.sql.*;

public class ConectorDB {
	private String userName;
	private String password;
	private String db;
	private int port;
	private String url = "jdbc:mysql://";
	private Connection conn = null;
	private Statement s;
    
	public ConectorDB(String usr, String pass, String db, int port, String connectionTo) {
		this.userName = usr;
		this.password = pass;
		this.db = db;
		this.port = port;
		this.url += connectionTo + ":"+port+"/";
		this.url += db;
        this.url += "?verifyServerCertificate=false&useSSL=true";
    }

    public boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Connection");
            conn = (Connection) DriverManager.getConnection(url, userName, password);
            if (conn != null) {
                //System.out.println("Conexió a base de dades "+url+" ... Ok");
            }
            return true;
        }
        catch(SQLException ex) {
            //System.out.println("Problema al connecta-nos a la BBDD --> "+url);
        }
        catch(ClassNotFoundException ex) {
            //System.out.println(ex);
        }
        return false;

    }
    
    public void insertQuery(String query){
        try {
            s =(Statement) conn.createStatement();
            s.executeUpdate(query);

        } catch (SQLException ex) {
            Output.print("Error inserting query:\n\t " + query, "red");
        }
    }

    public void executeQuery(String query){
        try {
            System.out.println(query);
            s =(Statement) conn.createStatement();
            s.execute(query);
        } catch (SQLException e) {
            Output.print("Error executing query:\n\t " + query, "red");
        }
    }
    
    public void updateQuery(String query){
    	 try {
             s =(Statement) conn.createStatement();
             s.executeUpdate(query);

         } catch (SQLException ex) {
             Output.print("Error updating query", "red");
         }
    }
    
    public void deleteQuery(String query){
    	 try {
             s =(Statement) conn.createStatement();
             s.executeUpdate(query);
             
         } catch (SQLException ex) {
             Output.print("Error deleting query", "red");
         }
    	
    }
    
    public ResultSet selectQuery(String query){
    	ResultSet rs = null;
    	 try {
             s =(Statement) conn.createStatement();
             rs = s.executeQuery (query);
             
         } catch (SQLException ex) {
             Output.print("Error selecting query", "red");
             ex.printStackTrace();
         }
		return rs;
    }
    
    public void disconnect(){
    	try {
			conn.close();
            System.out.println("Desconnectat!");
		} catch (SQLException e) {
            Output.print("Error disconnecting query", "red");
		}
    }

    public Connection getConn(){
	    return conn;
    }
}
