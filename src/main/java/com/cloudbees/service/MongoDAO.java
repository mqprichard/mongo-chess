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
	protected DB mongoDB = null;
	protected Mongo mongo = null;
	protected DBCollection games = null;
	protected DBCollection moves = null;
	protected String collGames = "games";
	protected String collMoves = "moves";

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
	
	public DB getMongoDB() {
		return mongoDB;
	}

	public Mongo getMongo() {
		return mongo;
	}
	
	public DBCollection getGames() {
		return games;
	}

	public DBCollection getMoves() {
		return moves;
	}

	public void connect() {

		Mongo m = null;
		
		try {
			m = new Mongo( getMongoHost() , getMongoPort() );
			DB db = m.getDB( getMongoDatabase() );
			
			this.mongo = new Mongo( getMongoHost() , getMongoPort() );
			this.mongoDB = this.mongo.getDB( getMongoDatabase() );
			this.games = db.getCollection(collGames);
			this.moves = db.getCollection(collMoves);
			
			System.out.println("Connected to Mongo");
			 
			DBCollection games = db.getCollection("games"); 
			BasicDBObject searchObject = new BasicDBObject();
			BasicDBObject skipFields = new BasicDBObject();
			skipFields.put("_id",0);
			DBObject theGame;			
			
			DBCollection moves = db.getCollection("moves");       
			BasicDBObject omits = new BasicDBObject();
			omits.put("_id",0);
			omits.put("game",0);
			omits.put("move", 0);
			
			// Insert new game
			DBObject obj = new BasicDBObject();
			Game game = new Game("Fischer","Spassky","Championship Match");
			obj.put("white", game.getWhite());
			obj.put("black", game.getBlack());
			obj.put("description", game.getDescription());
			obj.put("result", game.getResult());
			obj.put("next", game.getNext());
			obj.put("move", game.getMove());
			games.save(obj);
			String idString = obj.get("_id").toString();
			System.out.println(obj.toString());
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

			// Update game { $set:{next:"B"} }
			BasicDBObject query = new BasicDBObject();
			query.put("_id", new ObjectId(idString));
			BasicDBObject set = new BasicDBObject("$set", new BasicDBObject("next", "B")); 
			games.update( query, set );
			
			// Retrieve game object 
			searchObject.put("_id", new ObjectId(idString));
			theGame = games.findOne(searchObject,skipFields);
			System.out.println(theGame.toString());
			
			// Update game { $set:{next:"W"}, $inc:{move : 1} }
			query = new BasicDBObject();
			query.put("_id", new ObjectId(idString));
			BasicDBObject changes = new BasicDBObject();
			changes.put( "$set", new BasicDBObject("next", "W"));
			changes.put( "$inc", new BasicDBObject("move", 1));
			games.update( query, changes );
			
			// Retrieve game object 
			searchObject.put("_id", new ObjectId(idString));
			theGame = games.findOne(searchObject,skipFields);
			System.out.println(theGame.toString());

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
	
	public String getGame( String idString ) {
		
		DBCollection games = getGames(); 
		
		// Search by object id
		BasicDBObject searchObject = new BasicDBObject();
		searchObject.put("_id", new ObjectId(idString));
		
		// Do not include _id field
		BasicDBObject skipFields = new BasicDBObject();
		skipFields.put("_id",0);
		
		// Retrieve game object and return
		DBObject theGame = games.findOne(searchObject,skipFields);
		System.out.println(theGame.toString());
		
		return theGame.toString();
	}
	
	public String newGame( Game game ) {
		
		DBCollection games = getGames(); 
		
		DBObject obj = new BasicDBObject();
		obj.put("white", game.getWhite());
		obj.put("black", game.getBlack());
		obj.put("description", game.getDescription());
		obj.put("result", game.getResult());
		obj.put("next", game.getNext());
		obj.put("move", game.getMove());		
		games.save(obj);
		
		return obj.toString();		
	}
	
	public void updateBlackToMove( String idString ) {

		DBCollection games = getGames(); 
		
		// Update game { $set:{next:"B"} }
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(idString));
		BasicDBObject set = new BasicDBObject("$set", new BasicDBObject("next", "B")); 
		games.update( query, set );
	}
	
	public void updateWhiteToMove( String idString ) {

		DBCollection games = getGames(); 
		
		// Update game { $set:{next:"W"}, $inc:{move : 1} }
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(idString));
		BasicDBObject changes = new BasicDBObject();
		changes.put( "$set", new BasicDBObject("next", "W"));
		changes.put( "$inc", new BasicDBObject("move", 1));
		games.update( query, changes );
	}
	
	public String getNext( String idString ) {
		
		DBCollection games = getGames(); 
		
		// Search by object id
		BasicDBObject searchObject = new BasicDBObject();
		searchObject.put("_id", new ObjectId(idString));
		DBObject theGame = games.findOne(searchObject);	
		
		return theGame.get("next").toString();	
	}
	
	public String getMoves( String idString ) {
		
		DBCollection moves = getGames(); 
		 
		// Omit object id, game and move from result
		BasicDBObject omits = new BasicDBObject();
		omits.put("_id",0);
		omits.put("game",0);
		omits.put("move", 0);
		
		// Search by game id
		BasicDBObject findMoves = new BasicDBObject();
		findMoves.put("game", new ObjectId(idString));			
		List<DBObject> listMoves = moves.find(findMoves, omits).limit(200).toArray();

		return( listMoves.toString() );		
	}

	public String newMove( Move move ) {

		DBCollection moves = getGames();      
		
		DBObject moveObj = new BasicDBObject();
		moveObj.put("white", move.getWhite());
		moveObj.put("black", move.getBlack());
		moveObj.put("game", new ObjectId(move.getGame()));
		moveObj.put("move", move.getMove());
		moves.save(moveObj);
		
		return(moveObj.toString());
	}
}
