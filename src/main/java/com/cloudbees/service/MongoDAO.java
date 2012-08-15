package com.cloudbees.service;

import java.net.UnknownHostException;
import java.util.List;
import com.cloudbees.model.Game;
import com.cloudbees.model.Move;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.ObjectId;

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
			
			// db.games.find({_id:ObjectId("502ad7d46db6f53bef0face7")},{_id:0})
			DBCollection games = db.getCollection("games"); 
			BasicDBObject searchObject = new BasicDBObject();
			searchObject.put("_id", new ObjectId("502ad7d46db6f53bef0face7"));
			BasicDBObject skipFields = new BasicDBObject();
			skipFields.put("_id",0);
			DBObject theGame = games.findOne(searchObject,skipFields);
			System.out.println(theGame.toString());			

			// db.moves.find({game:ObjectId("502ad7d46db6f53bef0face7")},{_id:0,game:0,move:0}).toArray()
			DBCollection moves = db.getCollection("moves");       
			BasicDBObject omits = new BasicDBObject();
			omits.put("_id",0);
			omits.put("game",0);
			omits.put("move", 0);
			BasicDBObject find = new BasicDBObject();
			find.put("game", new ObjectId("502ad7d46db6f53bef0face7"));			
			List<DBObject> list = moves.find(find, omits).limit(200).toArray();
			System.out.println(list.toString());
			
			// Insert new game
			DBObject obj = new BasicDBObject();
			Game game = new Game("white","black","Championship Match");
			obj.put("white", game.getWhite());
			obj.put("black", game.getBlack());
			obj.put("description", game.getDescription());
			obj.put("result", game.getResult());
			obj.put("next", game.getNext());
			obj.put("move", game.getMove());
			games.save(obj);
			String idString = obj.get("_id").toString();
			System.out.println(idString);
			
			// Retrieve game object 
			searchObject.put("_id", new ObjectId(idString));
			theGame = games.findOne(searchObject,skipFields);
			System.out.println(theGame.toString());

			// Insert new moves
			DBObject whiteObj = new BasicDBObject();
			Move move = new Move("e2-e4", "", 1, idString);
			whiteObj.put("white", move.getWhite());
			whiteObj.put("game", new ObjectId(idString));
			whiteObj.put("move", game.getMove());
			moves.save(whiteObj);
			System.out.println(whiteObj.get("_id").toString());	
			DBObject blackObj = new BasicDBObject();
			move = new Move("", "e7-e5", 1, idString);
			blackObj.put("black", move.getBlack());
			blackObj.put("game", new ObjectId(idString));
			blackObj.put("move", game.getMove());
			moves.save(blackObj);
			System.out.println(blackObj.get("_id").toString());
			
			// Retrieve moves array
			BasicDBObject findMoves = new BasicDBObject();
			findMoves.put("game", new ObjectId(idString));			
			List<DBObject> listMoves = moves.find(findMoves, omits).limit(200).toArray();
			System.out.println(listMoves.toString());

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
