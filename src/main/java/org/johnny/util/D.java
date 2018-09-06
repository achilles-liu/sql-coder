package org.johnny.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class D {
	private Connection conn;

	public static class db{
		public static final String MYSQL	=	"jdbc:mysql://%s:3306/%s";
	}
	public Connection conn(String host,String dbName,String username,String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = String.format(D.db.MYSQL, host,dbName);
			//if(this.conn == null) this.conn = DriverManager.getConnection(url, username, password);
			this.conn = DriverManager.getConnection(url, username, password);
			return this.conn;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void close() {
		try {
			if(this.conn != null) this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close(Statement stat) {
		try {
			if(stat != null) stat.close();
			this.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close(ResultSet rs) {
		try {
			if(rs != null) rs.close();
			this.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String host = "127.0.0.1";
		String name = "smartsso";
		//String url = String.format(D.db.MYSQL, host,name);
		D db = new D();
		Connection con = db.conn(host, name, "root", "126219");
		System.out.println(con);
	}
}
