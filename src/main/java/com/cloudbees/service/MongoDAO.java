package com.cloudbees.service;

import java.net.UnknownHostException;
import java.util.Set;

import com.cloudbees.model.Game;
import com.cloudbees.model.Move;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDAO {
	
	protected String mongoHost = "localhost";
	protected int mongoPort = 27017;
	protected String mongoDatabase = "mydb";

	public String getMongoHost() {
		return mongoHost;
	}

	public void setMongoHost(String mongoHost) {
		this.mongoHost = mongoHost;
	}

	public int getMongoPort() {
		return mongoPort;
	}

	public void setMongoPort(int mongoPort) {
		this.mongoPort = mongoPort;
	}

	public String getMongoDatabase() {
		return mongoDatabase;
	}

	public void setMongoDatabase(String mongoDatabase) {
		this.mongoDatabase = mongoDatabase;
	}
	
	public void connect() {

		Mongo m = null;
		
		try {
			m = new Mongo( getMongoHost() , getMongoPort() );
			DB db = m.getDB( getMongoDatabase() );
			
			System.out.println("Connected to Mongo");

			Set<String> colls = db.getCollectionNames();
			for (String s : colls) {
			    System.out.println(s);
			}
			
			DBCollection coll = db.getCollection("games");
			DBObject myDoc = coll.findOne();
			System.out.println(myDoc);
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		catch (MongoException e) {
			e.printStackTrace();
		}
		finally {
		}


	}
	
	public String getGame( long id ) {
		String jsonGame = "";
		return jsonGame;
	}
	
	public String newGame( Game game ) {
		String jsonId = "";
		return jsonId;		
	}
	
	public String updateGame( Game game ) {
		String jsonGame = "";
		return jsonGame;
	}
	
	public String getNext( long id ) {
		String jsonNext = "";
		return jsonNext;		
	}
	
	public String getMoves( long id ) {
		String jsonMoves = "";
		return jsonMoves;
	}

	public String newWhiteMove( Move move ) {
		String jsonMove = "";
		return jsonMove;		
	}

	public String newBlackMove( Move move ) {
		String jsonMove = "";
		return jsonMove;		
	}

	public void disconnect() {

	}

}
