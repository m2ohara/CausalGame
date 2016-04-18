package com.causal.game.desktop;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.causal.game.data.IActionResolver;

public class DesktopActionResolver implements IActionResolver {

	@Override
	public Connection getConnection() {
		String url = "jdbc:sqlite:GameAppDB.sqlite";
		try {
			System.out.println("Opened db successfully");
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection(url);
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

}