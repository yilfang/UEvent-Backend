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
import net.eventhub.domain.EventFollower;
import net.eventhub.domain.User;
import net.eventhub.rest.filter.Secured;
import net.eventhub.service.EventFollowerBusiness;
import net.eventhub.utils.EventHubUtils;
import net.eventhub.utils.NullAwareBeanUtilsBean;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.EventUserVO;
import net.eventhub.valueobject.EventValueObj;

@Component
@Path("/followers")
//@Secured
public class EventFollowerRestService {
    
    @Autowired
    @Qualifier(value="eventFollowerBusiness")
    private EventFollowerBusiness eventFollowerBusinessObj;
    
 // http://localhost:8080/EventHub/rest/followers/followingEvents/3
    @GET
    @Path("/followingEvents/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollersByEventId(@PathParam("userId")int userId) 
    {
    	Collection<EventValueObj> events = eventFollowerBusinessObj.getFollowingEvents(userId);
    	if ( events == null || events.size() == 0)
    	{
    		return EventHubUtils.emptyReturn();
    	}
    	
    	return Response.ok().entity(events).build();
    }
    
 // http://localhost:8080/EventHub/rest/followers/json/addFollowingEvents
    @POST
    @Path("json/addFollowingEvents")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFollowingEvents(List<EventUserVO> followingEvents) {
    		
      try {
    	  eventFollowerBusinessObj.addFollowingEvents(followingEvents);
		
      	  } catch (Exception e) {
      		  e.printStackTrace();
      		  return EventHubUtils.errorReturn();
      		  
      	  }
       
      	return Response.ok().entity("Following events are added").build();
    }

    
    // Basic CRUD operations for EventFollower service
    
    // http://localhost:8080/EventHub/rest/followers/
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)   
    public Response getAllEventFollowers() {
    	List<EventFollower> followers = eventFollowerBusinessObj.getAllEventFollower();
    	if ( followers == null )
    	{
    		return EventHubUtils.errorReturn();
    	}
    	
    	List<EventUserVO> vos = new ArrayList<>();
    	
    	for ( EventFollower f : followers )
    	{
    		vos.add(VOEntityConverter.fromEventFollower(f));
    	}
        return Response.ok().entity(vos).build();
    }
    
    
 // http://localhost:8080/EventHub/rest/followers/get/1
    @GET
    @Path("/json/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventFollowerById(@PathParam("id")Integer followerId) {
    	if(followerId == null ) {
            return EventHubUtils.validationError();
        }
    	EventFollower follower = eventFollowerBusinessObj.getEventFollowerById(followerId);
    	if(follower == null) {
    		
            return EventHubUtils.errorReturn();
        }
        
        return Response.ok().entity(VOEntityConverter.fromEventFollower(follower)).build();
    }
    
    
 // http://localhost:8080/EventHub/rest/followers/json/add
    @POST
    @Path("json/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEventFollower(EventUserVO eventFollower) {
    	if(eventFollower == null ) {
            return EventHubUtils.validationError();
        }
    	    		
    	EventFollower newEventFollower = eventFollowerBusinessObj.createEventFollower(eventFollower);
    	
    	if(newEventFollower == null) {
    		
            return EventHubUtils.errorReturn();
        }
        
        return Response.ok().entity(VOEntityConverter.fromEventFollower(newEventFollower)).build();
    }
    
    
    // http://localhost:8080/EventHub/rest/followers/delete/1
    @DELETE
    @Path("delete/{id}")
    public Response deleteEventFollower(@PathParam("id") int EventFollowerId) throws Exception {
        try {
			eventFollowerBusinessObj.deleteEventFollower(EventFollowerId);
		} catch (Exception e) {		
			e.printStackTrace();
			EventHubUtils.errorReturn();
		}
        return Response.ok().entity("Success").build();
    }
    
 // http://localhost:8080/EventHub/rest/followers/delete?eventId=3&userId=23
    @DELETE
    @Path("delete")
    public Response deleteEventFollower(@QueryParam("eventId") int eventId, @QueryParam("userId")int userId) throws Exception {
        try {
			eventFollowerBusinessObj.deleteEventFollower(eventId, userId);
		} catch (Exception e) {		
			e.printStackTrace();
			EventHubUtils.errorReturn();
		}
        return Response.ok().entity("Success").build();
    }

	public EventFollowerBusiness getEventFollowerBusinessObj() {
		return eventFollowerBusinessObj;
	}

	public void setUserBusinessObj(EventFollowerBusiness EventFollowerBusinessObj) {
		this.eventFollowerBusinessObj = EventFollowerBusinessObj;
	}
    
    
}

