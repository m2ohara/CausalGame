package com.causal.game.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class DatabaseRepo {
	
	private static DatabaseRepo instance;
	private static Connection connection;
	
	private DatabaseRepo() {
		connection = SqlConnection.get();
	}
	
	public static DatabaseRepo instance() {
		if(instance == null) {
			instance = new DatabaseRepo();
		}
		try {
			if(connection == null || connection.isClosed()) {
				connection = SqlConnection.get();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return instance;
	}

	public int createDB() {
		Statement createDBStatement = null;
		
		//Get sql from file
		FileHandle createDBFile = Gdx.files.internal("db//CreateDBScript.sql");
		String fileSQL = createDBFile.readString();
		
		//Initialise as false
		int result = -1;
		
		try {
			
			//Create database tables
			createDBStatement = connection.createStatement();
			result = createDBStatement.executeUpdate(fileSQL);
		}
		catch(SQLException ex) {
		//TO DO: Log error
			System.out.println("Error creating database: "+ex);
		}
		finally {
			try {
				createDBStatement.close();
				connection.close();
			}
			catch(SQLException e) {
				
			}
		}
		return result;
	}
}
