package com.cloudbees.service;

import java.io.StringWriter;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import com.cloudbees.model.Move;
import com.google.gson.stream.JsonWriter;

@Path("/moves")
public class MoveServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private MongoDAO dao = new MongoDAO();
	public enum Player { WHITE, BLACK };

	@GET
    @Path("{id}")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMoves(@PathParam("id") String id ) {
		
		StatusType statusCode = null;
		String msg = null;
		
		try {			
		    dao.connect();

		    msg = dao.getMoves( id );
		    statusCode = Response.Status.OK;
		} 
		catch (Exception e) {
			e.printStackTrace();

			// Return 500 Internal Server Error
    		statusCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		finally {
			dao.getMongo().close();
		}
		
		if (statusCode != Response.Status.OK)
			return Response.status(statusCode).build();
		else
			return Response.status(statusCode).entity(msg).build();
	}
	
	@POST
    @Path("new")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newMove( Move move ) {
		
		StatusType statusCode = null;
		Player player = null;

		StringWriter sw = new StringWriter();
		JsonWriter writer = new JsonWriter(sw);
		
		try {			
		    dao.connect();
		    
		    String strGame = move.getGame();
		    
		    // Check whose move is expected next
	    	if ( dao.getNext( strGame ).equalsIgnoreCase("W") )
				player = Player.WHITE;
	    	else 
	    		player = Player.BLACK;

	    	// Error: move is out of sequence
		   	if ( move.getMove() != dao.getMoveNo( strGame )  )
				statusCode = Response.Status.BAD_REQUEST;
		   	// Error: white to move but none supplied
		   	else if ( ( player == Player.WHITE ) && ( move.getWhite().isEmpty() ) )
		   		statusCode = Response.Status.BAD_REQUEST;
		   	// Error: black to move but none supplied
		   	else if ( ( player == Player.BLACK ) && ( move.getBlack().isEmpty() ) )
		   		statusCode = Response.Status.BAD_REQUEST;
		   	// Valid player and move number
		   	else {		    
		   		dao.newMove( move );
		    
		   		writer.beginObject();
		   		writer.name( "game" ).value( move.getGame() );
		   		writer.name( "move" ).value( move.getMove() );
		   		
		   		// Return move detail and update game record
		   		if ( player == Player.WHITE ) {
		   			writer.name( "white" ).value( move.getWhite() );
		   			dao.updateBlackToMove( strGame );
		   		}
		   		else {
		   			writer.name( "black" ).value( move.getBlack() );
		   			dao.updateWhiteToMove( strGame );
		   		}
		   		
		   		writer.endObject();
		   		writer.close();	
			
			statusCode = Response.Status.OK;
		   	}
		} 
		catch (Exception e) {
			e.printStackTrace();

			// Return 500 Internal Server Error
    		statusCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		finally {
			dao.getMongo().close();
		}
	
		if (statusCode != Response.Status.OK)
			return Response.status(statusCode).build();
		else
			return Response.status(statusCode).entity(sw.toString()).build();	
	}	
}
