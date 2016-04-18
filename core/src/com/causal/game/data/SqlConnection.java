package com.causal.game.data;

import java.sql.Connection;

import com.causal.game.data.IActionResolver;


public class SqlConnection {
	
	private static IActionResolver sqlActionResolver;
	
	public SqlConnection(IActionResolver conn) {
		sqlActionResolver = conn;
	}
	
	public static Connection get() {
		return sqlActionResolver.getConnection();
	}
	
	
}
