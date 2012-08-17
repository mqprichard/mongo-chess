package com.cloudbees.service;

import java.net.UnknownHostException;
import java.util.List;

import org.bson.types.ObjectId;

import com.cloudbees.model.Game;
import com.cloudbees.model.Move;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;


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
	
	public DBCollection getGamesCollection() {
		return games;
	}

	public DBCollection getMovesCollection() {
		return moves;
	}

	public void connect() throws Exception {
		try {
			mongo = new Mongo( getMongoHost() , getMongoPort() );
			mongoDB = mongo.getDB( getMongoDatabase() );
			games = getMongoDB().getCollection(collGames);
			moves = getMongoDB().getCollection(collMoves);
			mongo.setWriteConcern(WriteConcern.SAFE);
		} 
		catch ( Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public String getGame( String idString ) {
		DBObject theGame = null;
		String strReturn = null;
		
		try {
			DBCollection games = getGamesCollection(); 
		
			// Search by object id
			BasicDBObject searchObject = new BasicDBObject();
			searchObject.put("_id", new ObjectId(idString));
		
			// Do not include _id field
			BasicDBObject skipFields = new BasicDBObject();
			skipFields.put("_id",0);
		
			// Retrieve game object and return
			theGame = games.findOne(searchObject,skipFields);
			strReturn = theGame.toString();
		}
		catch ( NullPointerException e ) {
			e.printStackTrace();
			
			// Not Found: Servlet should return 400 Bad Request
			strReturn = null;			
		}
		catch ( IllegalArgumentException e ) {
			e.printStackTrace();
			
			// Invalid id: Servlet should return 400 Bad Request
			strReturn = null;
		}

		return strReturn;
	}
	
	public String newGame( Game game ) {
		
		DBCollection games = getGamesCollection(); 
		
		DBObject obj = new BasicDBObject();
		obj.put("white", game.getWhite());
		obj.put("black", game.getBlack());
		obj.put("description", game.getDescription());
		obj.put("result", game.getResult());
		obj.put("next", game.getNext());
		obj.put("move", game.getMove());		
		games.insert(obj);
		
		return obj.get("_id").toString();	
	}
	
	public WriteResult updateBlackToMove( String idString ) {

		DBCollection games = getGamesCollection(); 
		
		// Update game { $set:{next:"B"} }
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(idString));
		BasicDBObject set = new BasicDBObject("$set", new BasicDBObject("next", "B")); 
		return games.update( query, set );
	}
	
	public WriteResult updateWhiteToMove( String idString ) {

		DBCollection games = getGamesCollection(); 
		
		// Update game { $set:{next:"W"}, $inc:{move : 1} }
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(idString));
		BasicDBObject changes = new BasicDBObject();
		changes.put( "$set", new BasicDBObject("next", "W"));
		changes.put( "$inc", new BasicDBObject("move", 1));
		return games.update( query, changes );
	}
	
	public String getNext( String idString ) {
		
		DBCollection games = getGamesCollection(); 
		
		// Search by object id
		BasicDBObject searchObject = new BasicDBObject();
		searchObject.put("_id", new ObjectId(idString));
		DBObject theGame = games.findOne(searchObject);	
		
		return theGame.get("next").toString();	
	}
	
	public long getMoveNo( String idString ) {
		
		DBCollection games = getGamesCollection(); 
		
		// Search by object id
		BasicDBObject searchObject = new BasicDBObject();
		searchObject.put("_id", new ObjectId(idString));
		DBObject theGame = games.findOne(searchObject);	
		
		return (Long.parseLong( theGame.get("move").toString() ) );	
	}
	
	public String getMoves( String idString ) {
		
		DBCollection moves = getMovesCollection(); 
		 
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

		DBCollection moves = getMovesCollection();
		DBObject moveObj = new BasicDBObject();
		
		if ( ! move.getWhite().isEmpty() ){
			moveObj.put( "white", move.getWhite() );
		}
		else {
			moveObj.put( "black", move.getBlack() );
		}
		moveObj.put( "game", new ObjectId(move.getGame()) );
		moveObj.put( "move", move.getMove() );
		moves.save(moveObj);

		return(moveObj.toString());
	}
}
