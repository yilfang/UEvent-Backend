package net.eventhub.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import net.eventhub.domain.Event;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.User;
import net.eventhub.rest.filter.Secured;
import net.eventhub.service.EventInviteeBusiness;
import net.eventhub.utils.EventHubUtils;
import net.eventhub.utils.NullAwareBeanUtilsBean;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.EventUserVO;
import net.eventhub.valueobject.EventValueObj;

@Component
@Path("/invitees")
//@Secured
public class EventInviteeRestService {
    
    @Autowired
    @Qualifier(value="eventInviteeBusiness")
    private EventInviteeBusiness eventInviteeBusinessObj;
    
    
 // http://localhost:8080/EventHub/rest/invitees/invites/3
    @GET
    @Path("/invites/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersInviteEvents(@PathParam("userId")int userId) 
    {
    	Collection<EventUserVO> events = eventInviteeBusinessObj.getUsersInviteEvents(userId);
    	
    	
    	if ( events == null || events.size() == 0 )
    	{
    		return EventHubUtils.emptyReturn();
    	}
        return Response.ok().entity(events).build();
    }
    
 // http://localhost:8080/EventHub/rest/invitees/getEventInvitees/3
    @GET
    @Path("/getEventInvitees/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInviteesByEventId(@PathParam("eventId")int eventId) {
    	Collection<EventUserVO> invitations = eventInviteeBusinessObj.getEventInvitees(eventId);
    	
    	if ( invitations == null || invitations.size() == 0 ) 
    	{
    		return EventHubUtils.emptyReturn();
    	}
        return Response.ok().entity(invitations).build();
    }
    
 // http://localhost:8080/EventHub/rest/invitees/json/addListOfInvitees
    @POST
    @Path("json/addListOfInvitees")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addInvitees(List<EventUserVO> invitees) {
    		
      try {
    	  	eventInviteeBusinessObj.addListOfInvitees(invitees);		
      	   } catch (Exception e) {
      			e.printStackTrace();
      			return EventHubUtils.errorReturn();    		
      	   }
      	return Response.ok().entity("Invitees are added").build();
    }
 
 
    
    // Basic CRUD operations for EventInvitee service
    
    // http://localhost:8080/EventHub/rest/invitees/
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)   
    public List<EventUserVO> getAllEventInvitees() {
    	List<EventInvitee> invitees = eventInviteeBusinessObj.getAllEventInvitee();
    	List<EventUserVO> evo = new ArrayList<>();
    	if ( invitees != null && invitees.size() > 0 )
    	{
    		for(EventInvitee invitee : invitees)
    		{
    			evo.add(VOEntityConverter.fromEventInvitee(invitee));
    		}
    		
    	}
    	
        return evo;
    }
    
 // http://localhost:8080/EventHub/rest/invitees/json/get/1
    @GET
    @Path("/json/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public  EventUserVO getEventInviteeById(@PathParam("id")int EventInviteeId) {
    	EventInvitee invitee = eventInviteeBusinessObj.getEventInviteeById(EventInviteeId);
    	if ( invitee != null )
    	{
    		return VOEntityConverter.fromEventInvitee(invitee);
    	}
    	
        return null;
    }
    
    
 // http://localhost:8080/EventHub/rest/invitees/json/add
    @POST
    @Path("json/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public EventUserVO createEventInvitee(EventUserVO eventUser) {
    	EventInvitee newEventInvitee = null;
    	if ( eventUser != null )
    	{
    		newEventInvitee = VOEntityConverter.toEventInvitee(eventUser);
    		
    		newEventInvitee = eventInviteeBusinessObj.createEventInvitee(newEventInvitee);
    	}
        
        return VOEntityConverter.fromEventInvitee(newEventInvitee);
    }
    
    // http://localhost:8080/EventHub/rest/invitees/update
    @PUT 
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEventInvitee(EventUserVO updatingEventInvitee) {
    	if ( updatingEventInvitee == null ) {
			return EventHubUtils.validationError();			
		} 
    	
    	 EventInvitee invitee = eventInviteeBusinessObj.updateEventInvitee(updatingEventInvitee);
				
		if ( invitee == null )
		{
			return EventHubUtils.errorReturn();
		}
		
        return Response.ok().entity(VOEntityConverter.fromEventInvitee(invitee)).build();
    }
    
    // http://localhost:8080/EventHub/rest/invitees/delete/1
    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEventInvitee(@PathParam("id") int EventInviteeId) throws Exception {
        try {
			eventInviteeBusinessObj.deleteEventInvitee(EventInviteeId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return EventHubUtils.errorReturn();
		}
        return Response.ok("Success").build();
    }
    
 // http://localhost:8080/EventHub/rest/invitees/delete?eventId=3&userId=23
    @DELETE
    @Path("delete")
    public Response deleteEventAttendee(@QueryParam("eventId") int eventId, @QueryParam("userId")int userId) throws Exception {
        try {
        	eventInviteeBusinessObj.deleteEventInvitee(eventId, userId);
		} catch (Exception e) {		
			e.printStackTrace();
			EventHubUtils.errorReturn();
		}
        return Response.ok().entity("Success").build();
    }


	public EventInviteeBusiness getEventInviteeBusinessObj() {
		return eventInviteeBusinessObj;
	}

	public void setEventInviteeBusinessObj(EventInviteeBusiness EventInviteeBusinessObj) {
		this.eventInviteeBusinessObj = EventInviteeBusinessObj;
	}
    
    
}

