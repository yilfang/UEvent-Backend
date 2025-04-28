package net.eventhub.rest;

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
import javax.ws.rs.core.MediaType;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import net.eventhub.domain.EventUpdate;
import net.eventhub.domain.EventFollower;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.User;
import net.eventhub.rest.filter.Secured;
import net.eventhub.service.EventUpdateBusiness;
import net.eventhub.service.EventBusiness;
import net.eventhub.service.EventUpdateBusiness;
import net.eventhub.service.UserBusiness;
import net.eventhub.service.UserBusinessObject;
import net.eventhub.utils.EventHubUtils;
import net.eventhub.utils.NullAwareBeanUtilsBean;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.EventUserVO;


@Component
@Path("/updates")
//@Secured
public class EventUpdateRestService {
    
    @Autowired
    @Qualifier(value="eventUpdateBusiness")
    private EventUpdateBusiness eventUpdateBusiness;
    
    // http://localhost:8080/EventHub/rest/updates/getEventUpdates/3
    @GET
    @Path("/getEventUpdates/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getUpdatesByEventId(@PathParam("eventId")int eventId) {
        return eventUpdateBusiness.getEventUpdates(eventId);
    }
    
 // http://localhost:8080/EventHub/rest/updates/users/"userId"
    @GET
    @Path("/users/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUpdatesByUserId(@PathParam("userId")int userId) {
    	Collection<EventUserVO> updates =  eventUpdateBusiness.getUpdatesByUserId(userId);
    	
    	if ( updates == null || updates.size() == 0 )
    	{
    		return EventHubUtils.emptyReturn();
    	}
    	
        return Response.ok().entity(updates).build();
    }

    
    // Basic CRUD operations for EventUpdate service
    
    
    // http://localhost:8080/EventHub/rest/updates/
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)   
    public List<EventUserVO> getAllEventUpdates() {
        return eventUpdateBusiness.getAllEventUpdate();
    }
    
 // http://localhost:8080/EventHub/rest/updates/get/1
    @GET
    @Path("/json/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public EventUserVO getEventUpdateById(@PathParam("id")int updateId) {
        return eventUpdateBusiness.getEventUpdateById(updateId);
    }
    
    
 // http://localhost:8080/eventHub/rest/updates/json/add
    @POST
    @Path("json/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEventUpdate(EventUserVO EventUpdate) {
    	if(EventUpdate == null ) {
            return EventHubUtils.validationError();
        }
    	    		
    	Integer newUpdatesId = eventUpdateBusiness.createEventUpdate(EventUpdate);
    	
    	if(newUpdatesId == null) {
    		
            return EventHubUtils.errorReturn();
        }
        
        return Response.ok("An event updates is added.").build();
    }
    
    
    // http://localhost:8080/EventHub/rest/updates/delete/1
    @DELETE
    @Path("delete/{id}")
    public Response deleteEventUpdate(@PathParam("id") int eventUpdateId){
        try {
			eventUpdateBusiness.deleteEventUpdate(eventUpdateId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EventHubUtils.errorReturn();
		}
        return Response.ok("Success").build();
    }

	public EventUpdateBusiness getEventUpdateBusiness() {
		return eventUpdateBusiness;
	}

	public void setUserBusinessObj(EventUpdateBusiness EventUpdateBusinessObj) {
		this.eventUpdateBusiness = EventUpdateBusinessObj;
	}
    
    
}

