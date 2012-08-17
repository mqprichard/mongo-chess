package com.cloudbees.service;

import static org.junit.Assert.*;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

import com.cloudbees.model.Game;
import com.cloudbees.model.Move;
import com.mongodb.WriteResult;

public class TestMongoDAO {
	private MongoDAO dao = new MongoDAO();
	
	private String testWhite = "Fischer";
	private String testBlack = "Spassky";
	private String testDescription = "Reykjavik Game 1";
	private String testResult = "";
	private String testNext = "W";
	private long testMove = 1L;
	private String testId = "";
	private String testWhiteMove = "e2-e4";
	private String testBlackMove = "e7-e5";

	@Test
	public void testGame() {
		try {
			// Connect to MongoDB
			dao.connect();
		
			// Create new game
			Game game = new Game( testWhite, testBlack, testDescription);
			testId  = dao.newGame(game);
			assertFalse( testId.isEmpty() );
		
			// Get the game object back from MongoDB
			String result = dao.getGame( testId );
			
			// Validate JSON elements in response
			String response = "{result:" + result.toString() + "}";
			JSONObject jObject = new JSONObject(response);
			JSONObject json = jObject.getJSONObject("result");
			assertFalse( json == null );
	
			assertEquals(json.getString("white"), testWhite);
			assertEquals(json.getString("black"), testBlack);
			assertEquals(json.getString("description"), testDescription);
			assertEquals(json.getString("result"), testResult);
			assertEquals(json.getString("next"), testNext);
			assertEquals(json.getLong("move"), testMove);		
			
			// Set game state: Black to move
			WriteResult state = dao.updateBlackToMove( testId );
			assertEquals( state.getError(), null );
			
			// Get the game object back from MongoDB
			result = dao.getGame( testId );
			
			// Validate JSON elements in response
			response = "{result:" + result.toString() + "}";
			jObject = new JSONObject(response);
			json = jObject.getJSONObject("result");
			assertFalse( json == null );
	
			assertEquals(json.getString("next"), "B");
			assertEquals(json.getLong("move"), 1L);
			assertEquals( dao.getNext( testId ), "B" );
			assertEquals( dao.getMoveNo( testId ), 1L );
			
			// Set game state: White to move
			state = dao.updateWhiteToMove( testId );
			assertEquals( state.getError(), null );
			
			// Get the game object back from MongoDB
			result = dao.getGame( testId );
			
			// Validate JSON elements in response
			response = "{result:" + result.toString() + "}";
			jObject = new JSONObject(response);
			json = jObject.getJSONObject("result");
			assertFalse( json == null );
	
			assertEquals( json.getString("next"), "W" );
			assertEquals( json.getLong("move"), 2L );
			assertEquals( dao.getNext( testId ), "W" );
			assertEquals( dao.getMoveNo( testId ), 2L );
		}	
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		finally {
			dao.getMongo().close();
		}
	}

	@Test
	public void testMoves() {
		try {
			// Connect to MongoDB
			dao.connect();
	
			// Create new game
			Game game = new Game( testWhite, testBlack, testDescription);
			testId = dao.newGame(game);
			assertFalse( testId.isEmpty() );
		
			// White move
			Move move = new Move( testWhiteMove, "", 1L, testId );
			String result = dao.newMove( move );

			// Validate JSON elements in response
			String response = "{result:" + result.toString() + "}";
			JSONObject jObject = new JSONObject(response);
			JSONObject json = jObject.getJSONObject("result");
			assertFalse( json == null );
			assertFalse( json.getString("_id").isEmpty() );
			assertEquals( json.getString( "white" ), testWhiteMove );
			assertEquals( json.getJSONObject("game").getString( "$oid" ), testId);

			// Black move
			move.setWhite( "" );
			move.setBlack( testBlackMove );
			result = dao.newMove( move );
			
			// Validate JSON elements in response
			response = "{result:" + result.toString() + "}";
			jObject = new JSONObject(response);
			json = jObject.getJSONObject("result");
			assertFalse( json == null );
			assertFalse( json.getString("_id").isEmpty() );
			assertEquals( json.getString( "black" ), testBlackMove );
			assertEquals( json.getJSONObject("game").getString( "$oid" ), testId);
			
			// Get array of moves for game
			result = dao.getMoves( testId );
			response = "{result:" + result.toString() + "}";
			jObject = new JSONObject(response);
			JSONArray jsonMoves = jObject.getJSONArray("result");
			assertEquals( jsonMoves.length(), 2);
			
			// Validate array elements for both moves
			JSONObject first = jsonMoves.getJSONObject( 0 );
			assertEquals( first.getString( "white"), testWhiteMove );		
			JSONObject second = jsonMoves.getJSONObject( 1 );
			assertEquals( second.getString( "black"), testBlackMove );	
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		finally {
			dao.getMongo().close();
		}		
	}

}
