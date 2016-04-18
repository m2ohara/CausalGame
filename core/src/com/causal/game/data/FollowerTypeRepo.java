package com.causal.game.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.causal.game.state.FollowerType;

public class FollowerTypeRepo {
	
	private static FollowerTypeRepo instance;
	private static Connection connection;
	
	private FollowerTypeRepo() {
		connection = SqlConnection.get();
	}
	
	public static FollowerTypeRepo instance() {
		if(instance == null) {
			instance = new FollowerTypeRepo();
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
	
	public List<FollowerType> get() {
		return new GetFollowerTypes().ExecuteTransaction();
	}
	
	public void create(FollowerType entity) {
		new CreateFollowerType(entity);
	}
	
	public void update(FollowerType entity) {
		new UpdateFollowerType(entity);
	}
	
	public class GetFollowerTypes extends BaseExecuter<FollowerType> {

		@Override
		public void setSql() {
			this.sql = "SELECT * FROM FOLLOWERTYPE;";
			
		}

		@Override
		public List<FollowerType> getResult(ResultSet result) {
			List<FollowerType> resultList = new ArrayList<FollowerType>();	
			
			try {
				while(result.next()) {
					resultList.add(new FollowerType(result.getInt("_id"), result.getString("IMAGE_PATH")));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return resultList;
		}

	}
	
	public class CreateFollowerType extends BaseExecuter<FollowerType> {
		
		private FollowerType entity;
		
		public CreateFollowerType(FollowerType entity) {
			this.entity = entity;
		}

		@Override
		public void setSql() {
			this.sql = "INSERT INTO FOLLOWERTYPE VALUES ( null, "+entity.getImagePath()+");";
			
		}

		@Override
		public List<FollowerType> getResult(ResultSet result) {
			// TODO Auto-generated method stub
			return null;
		}

	}
	
	public class UpdateFollowerType extends BaseExecuter<FollowerType> {
		
		private FollowerType entity;
		
		public UpdateFollowerType(FollowerType entity) {
			this.entity = entity;
		}

		@Override
		public void setSql() {
			this.sql = "UPDATE FOLLOWERTYPE SET IMAGE_PATH = "+entity.getImagePath()+" WHERE _id = "+entity.getId()+";";
			
		}

		@Override
		public List<FollowerType> getResult(ResultSet result) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
