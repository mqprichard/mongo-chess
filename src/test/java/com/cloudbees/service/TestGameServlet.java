package com.cloudbees.service;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

import com.cloudbees.model.Game;

public class TestGameServlet {
	
	private String testWhite = "White Player";
	private String testBlack = "Black Player";
	private String testDescription = "Match Description";
	private String testResult = "";
	private String testNext = "W";
	private long testMove = 1L;
	
	// Invalid ObjectID string: IllegalArgumentException
	private String badIdGarbage = "@@@@@@@@@@@@@";
	private String badIdEmpty = "";
	private String badIdNull = null;
	
	// Non-existent Object ID: NullPointerException
	private String badIdNonExistent = "1234567890abcdef12345678";

	@Test
	public void testGameServlet() {
		try {
			GameServlet gameServlet = new GameServlet();
			Game game = new Game( testWhite,
								  testBlack,
								  testDescription );
			
			// Call newGame to create a new game
			Response response = gameServlet.newGame( game );
			assertFalse( response == null );
			String jsonResponse = response.getEntity().toString();
			assertFalse( jsonResponse.isEmpty() );

			// Validate JSON elements in response
			String strJson = "{response:" + jsonResponse + "}";
			JSONObject jObject = new JSONObject(strJson);
			JSONObject json = jObject.getJSONObject("response");
			assertFalse( json == null );
			
			// Store the game id
			String id = json.getString("id");
			assertFalse( id == null );
			
			// Call getGame() to get game by id
			response = gameServlet.getGame( id );
			jsonResponse = response.getEntity().toString();
			assertFalse( jsonResponse.isEmpty() );

			// Validate JSON elements in response
			strJson = "{response:" + jsonResponse + "}";
			jObject = new JSONObject(strJson);
			json = jObject.getJSONObject("response");
			assertFalse( json == null );	
	
			assertEquals(json.getString("white"), testWhite);
			assertEquals(json.getString("black"), testBlack);
			assertEquals(json.getString("description"), testDescription);
			assertEquals(json.getString("result"), testResult);
			assertEquals(json.getString("next"), testNext);
			assertEquals(json.getLong("move"), testMove);
			
			// Try to get game with invalid id
			System.out.println( "Testing with malformed object id: IllegalArgumentException" );
			response = gameServlet.getGame( badIdGarbage );
			assertEquals( response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode() );
			System.out.println( "Testing with empty object id: IllegalArgumentException" );
			response = gameServlet.getGame( badIdEmpty );
			assertEquals( response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode() );
			System.out.println( "Testing with null object id: IllegalArgumentException" );
			response = gameServlet.getGame( badIdNull );
			assertEquals( response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode() );
			
			// Try to get game with non-existent id
			System.out.println( "Testing with non-existent object id: NullPointerException" );
			response = gameServlet.getGame( badIdNonExistent );
			assertEquals( response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode() );
		}
		catch( Exception e ) {
			e.printStackTrace();
			fail();
		}
	}
}
