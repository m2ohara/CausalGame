package com.causal.game.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class BaseExecuter<E> implements IExecuter<E> {
	
	protected static Connection connection;
	protected String sql;
	
	public BaseExecuter() {
		
		this.setSql();
		
		try {
			if(connection == null || connection.isClosed()) {
				connection = SqlConnection.get();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<E> ExecuteTransaction() {
		
		Statement statement = null;
		
		//Initialise
		ResultSet result = null;
		List<E> resultList = null;	
		
		
		try {
			
			//Create user
			statement = connection.createStatement();
			result = statement.executeQuery(sql);
			
			resultList = getResult(result);
		}
		catch(SQLException ex) {
			//TO DO: Log error
			System.out.println("Error: "+ex);
		}
		finally {
			try {
				statement.close();
				result.close();
//				connection.close();
			}
			catch(SQLException e) {
				//TO DO: Log error
				System.out.println("Error closing statement: "+e);
			}
		}
		
		
		return resultList;
	}
	
	public abstract void setSql();
	public abstract List<E> getResult(ResultSet result);
}
