package com.cloudbees.service;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

@Path("/game")
public class GameServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private MongoDAO dao = new MongoDAO();
	
	@GET
    @Path("{id}")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGame(@PathParam("id") long id ) {
		
		StatusType statusCode = null;
		String msg = null;
		
		try {
			dao.connect();			
			statusCode = Response.Status.OK;
		}		
		catch (Exception e) {
			e.printStackTrace();

			// Return 500 Internal Server Error
    		statusCode = Response.Status.INTERNAL_SERVER_ERROR;			
		}
		finally {
		}

		if (statusCode != Response.Status.OK)
			return Response.status(statusCode).build();
		else
			return Response.status(statusCode).entity(msg).build();	
		
	}
	public MongoDAO getDao() {
		return dao;
	}
	public void setDao(MongoDAO dao) {
		this.dao = dao;
	}
}
