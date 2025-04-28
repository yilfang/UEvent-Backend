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
//import javax.ws.rs.PUT;
//import javax.ws.rs.DELETE;
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
import net.eventhub.domain.EventAttendee;
import net.eventhub.domain.EventFollower;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.User;
import net.eventhub.rest.filter.Secured;
import net.eventhub.service.EventAttendeeBusiness;
import net.eventhub.service.EventBusiness;
import net.eventhub.service.UserBusiness;
import net.eventhub.service.UserBusinessObject;
import net.eventhub.utils.EventHubUtils;
import net.eventhub.utils.NullAwareBeanUtilsBean;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.EventUserVO;
import net.eventhub.valueobject.EventValueObj;

@Component
@Path("/attendees")
//@Secured
public class EventAttendeeRestService {
    
    @Autowired
    @Qualifier(value="eventAttendeeBusiness")
    private EventAttendeeBusiness eventAttendeeBusinessObj;
    
    // http://localhost:8080/EventHub/rest/attendees/getEventAttendees/3
    @GET
    @Path("/getEventAttendees/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventUserVO> getAttendeesByEventId(@PathParam("eventId")int eventId) {
    	List<User> users = eventAttendeeBusinessObj.getEventAttendees(eventId);
    	List<EventUserVO> evo = new ArrayList<>();
    	if ( users != null && users.size() > 0 ) {
    		for(User user : users)
    		{
    			evo.add(VOEntityConverter.fromUser(user));
    		}
    	}
        return evo;        
    }
    
 // http://localhost:8080/EventHub/rest/attendees/attendingEvents/3
    @GET
    @Path("/attendingEvents/{attendeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAttendingEvents(@PathParam("attendeeId")int attendeeId) {
    	Collection<EventValueObj> events = eventAttendeeBusinessObj.getAttendingEvents(attendeeId);
    	if ( events == null || events.size() == 0)
    	{
    		return EventHubUtils.emptyReturn();
    	}
    	 
    	return Response.ok().entity(events).build();
    }

    
    // Basic CRUD operations for EventAttendee service
    
    // http://localhost:8080/EventHub/rest/attendees/
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)   
    public List<EventUserVO> getAllEventAttendees() {
    	List<EventAttendee> attendees = eventAttendeeBusinessObj.getAllEventAttendee();
    	List<EventUserVO> evo = new ArrayList<>();
    	if ( attendees != null && attendees.size() > 0 )
    	{
    		for(EventAttendee attendee : attendees)
    		{
    			evo.add(VOEntityConverter.fromEventAttendee(attendee));
    		}
    		
    	}
        return evo;
    }
    
 // http://localhost:8080/EventHub/rest/attendees/get/1
    @GET
    @Path("/json/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventAttendeeById(@PathParam("id")int attendeeId) {
    	EventAttendee attendee = eventAttendeeBusinessObj.getEventAttendeeById(attendeeId);
    	if ( attendee != null )
    	{
    		return Response.ok().entity(VOEntityConverter.fromEventAttendee(attendee)).build();
    	}
    	
        return EventHubUtils.errorReturn();
    }
    
    
 // http://localhost:8080/eventHub/rest/attendees/json/add
    @POST
    @Path("json/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEventAttendee(EventUserVO eventAttendee) {
    	if(eventAttendee == null ) {
            return EventHubUtils.validationError();
        }
    	    		
    	EventAttendee neweventAttendee = eventAttendeeBusinessObj.createEventAttendee(eventAttendee);
    	
    	if(neweventAttendee == null) {
    		
            return EventHubUtils.errorReturn();
        }
        
        return Response.ok().entity(VOEntityConverter.fromEventAttendee(neweventAttendee)).build();
    }
    
    
    // http://localhost:8080/EventHub/rest/attendees/delete/1
    @DELETE
    @Path("delete/{id}")
    public Response deleteEventAttendee(@PathParam("id") int eventAttendeeId){
        try {
			eventAttendeeBusinessObj.deleteEventAttendee(eventAttendeeId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EventHubUtils.errorReturn();
		}
        return Response.ok().entity("Success").build();
    }
    
 // http://localhost:8080/EventHub/rest/attendees/delete?eventId=3&userId=23
    @DELETE
    @Path("delete")
    public Response deleteEventAttendee(@QueryParam("eventId") int eventId, @QueryParam("userId")int userId) throws Exception {
        try {
        	eventAttendeeBusinessObj.deleteEventAttendee(eventId, userId);
		} catch (Exception e) {		
			e.printStackTrace();
			EventHubUtils.errorReturn();
		}
        return Response.ok().entity("Success").build();
    }


	public EventAttendeeBusiness getEventAttendeeBusinessObj() {
		return eventAttendeeBusinessObj;
	}

	public void setUserBusinessObj(EventAttendeeBusiness eventAttendeeBusinessObj) {
		this.eventAttendeeBusinessObj = eventAttendeeBusinessObj;
	}
    
    
}

