package net.eventhub.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

@Component
@Path("/api")
//@Secured
public class ApiKeyRestService {
	//http://localhost:8080/EventHub/rest/api/key
	@GET
    @Path("/key")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUserByKeyword(@HeaderParam("user") String user, @HeaderParam("code") String code ) {
		
		if ( user.equals("eventhub")  && code.equals("umich"))
		{
			return Response.ok().entity("AIzaSyDgXc8NSGaSm6rQvxauE1rd_ihMCrqWw10").build();
		}
		
		return Response.serverError().build();
	}

}
