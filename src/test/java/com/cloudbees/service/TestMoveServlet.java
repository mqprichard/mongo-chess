package com.cloudbees.service;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

import com.cloudbees.model.Game;
import com.cloudbees.model.Move;

public class TestMoveServlet {
	
	private String testWhite = "White Player";
	private String testBlack = "Black Player";
	private String testDescription = "Match Description";
	private String testGameId = null;
	private long testMoveNo = 0;
	private String testWhiteMove = "";
	private String testBlackMove = "";
	
	private String testMoves1W = "[{ \"white\" : \"e2-e4\"}]";
	private String testMoves1B = "[{ \"white\" : \"e2-e4\"}, { \"black\" : \"e7-e5\"}]";
	private String testMoves2W = "[{ \"white\" : \"e2-e4\"}, { \"black\" : \"e7-e5\"}, { \"white\" : \"d2-d4\"}]";

	@Test
	public void testMoveServlet() {
		
		GameServlet gameServlet = new GameServlet();
		MoveServlet moveServlet = new MoveServlet();
		
		Game game = new Game( testWhite,
							  testBlack,
							  testDescription );

		try {
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
			testGameId = json.getString("id");
			assertFalse( testGameId == null );

			// Test a white move
			testWhiteMove = "e2-e4";
			testBlackMove = "";
			testMoveNo = 1L;
			
			Move testMove = new Move(testWhiteMove, 
								 	 testBlackMove,
								 	 testMoveNo,
								 	 testGameId );
			
			response = moveServlet.newMove( testMove );
			assertFalse( response == null );
			assertEquals( response.getStatus(), Response.Status.OK.getStatusCode() );
			
			// Test getMoves()
			response = moveServlet.getMoves( testGameId );
			assertFalse( response == null );
			assertEquals( response.getStatus(), Response.Status.OK.getStatusCode() );
			assertEquals( response.getEntity().toString(), testMoves1W );
			
			// Test a black move
			testWhiteMove = "";
			testBlackMove = "e7-e5";
			testMoveNo = 1L;
			
			testMove.setWhite( testWhiteMove );
			testMove.setBlack( testBlackMove );
			testMove.setMove( testMoveNo );
			
			response = moveServlet.newMove( testMove );
			assertFalse( response == null );
			assertEquals( response.getStatus(), Response.Status.OK.getStatusCode() );
			
			// Test getMoves() 
			response = moveServlet.getMoves( testGameId );
			assertFalse( response == null );
			assertEquals( response.getStatus(), Response.Status.OK.getStatusCode() );
			assertEquals( response.getEntity().toString(), testMoves1B );
			
			// Error: move number out of sequence 
			testWhiteMove = "e2-e4";
			testBlackMove = "";
			testMoveNo = 1L;
			
			testMove = new Move(testWhiteMove, 
								 	 testBlackMove,
								 	 testMoveNo,
								 	 testGameId );
			
			response = moveServlet.newMove( testMove );
			assertFalse( response == null );
			assertEquals( response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode() );
			
			// Error: player out of sequence
			testWhiteMove = "";
			testBlackMove = "d7-d4";
			testMoveNo = 2L;
			
			testMove = new Move(testWhiteMove, 
				 	 testBlackMove,
				 	 testMoveNo,
				 	 testGameId );

			response = moveServlet.newMove( testMove );
			assertFalse( response == null );
			assertEquals( response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode() );	
			
			// Error: No move data
			testWhiteMove = "";
			testBlackMove = "";
			testMoveNo = 2L;
	
			testMove = new Move(testWhiteMove, 
				 	 testBlackMove,
				 	 testMoveNo,
				 	 testGameId );

			response = moveServlet.newMove( testMove );
			assertFalse( response == null );
			assertEquals( response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode() );	

			// Test a white move
			testWhiteMove = "d2-d4";
			testBlackMove = "";
			testMoveNo = 2L;
			
			testMove = new Move(testWhiteMove, 
								 	 testBlackMove,
								 	 testMoveNo,
								 	 testGameId );
			
			response = moveServlet.newMove( testMove );
			assertFalse( response == null );
			assertEquals( response.getStatus(), Response.Status.OK.getStatusCode() );
			
			response = moveServlet.getMoves( testGameId );
			assertFalse( response == null );
			assertEquals( response.getStatus(), Response.Status.OK.getStatusCode() );
			assertEquals( response.getEntity().toString(), testMoves2W );
		}
		catch( Exception e ) {
			e.printStackTrace();
			fail( "Exception" );
		}
	}
}
