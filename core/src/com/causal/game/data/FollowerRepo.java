package com.causal.game.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.causal.game.state.Follower;
import com.causal.game.state.FollowerType;

public class FollowerRepo {
	
	private static FollowerRepo instance;
	private static Connection connection;
	
	private FollowerRepo() {
		connection = SqlConnection.get();
	}
	
	public static FollowerRepo instance() {
		if(instance == null) {
			instance = new FollowerRepo();
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
	
	public List<Follower> getFollowers() {
		return new GetFollowers().ExecuteTransaction();
	}
	
	public void createFollower(Follower entity) {
		new CreateFollower(entity).ExecuteTransaction();
	}
	
	public void updateFollower(Follower entity) {
		new UpdateFollower(entity).ExecuteTransaction();
	}
	
	public class GetFollowers extends BaseExecuter<Follower>{

		@Override
		public void setSql() {
			this.sql = "SELECT f.rowid, ft._id, ft.IMAGE_PATH FROM FOLLOWER f JOIN FOLLOWERTYPE ft ON f._id = ft._id;";
		}

		@Override
		public List<Follower> getResult(ResultSet result) {
			List<Follower> resultList = new ArrayList<Follower>();	
			
			try {
				while(result.next()) {
					resultList.add(new Follower(result.getInt("rowid"), new FollowerType(result.getInt("_id"), result.getString("IMAGE_PATH"))));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return resultList;
		}
	}
	
	public class CreateFollower extends BaseExecuter<Follower> {
		
		private Follower entity;
		
		public CreateFollower(Follower entity) {
			this.entity = entity;
		}

		@Override
		public void setSql() {
			this.sql = "INSERT INTO FOLLOWER VALUES ("+entity.type.getId()+");";
			
		}

		@Override
		public List<Follower> getResult(ResultSet result) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public class UpdateFollower extends BaseExecuter<Follower> {
		
		private Follower entity;
		
		public UpdateFollower(Follower entity) {
			this.entity = entity;
		}

		@Override
		public void setSql() {
			this.sql = "UPDATE FOLLOWER SET TYPE_ID = "+entity.getFollowerType().getId()+" WHERE rowid = "+entity.getId()+";";
			
		}

		@Override
		public List<Follower> getResult(ResultSet result) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
