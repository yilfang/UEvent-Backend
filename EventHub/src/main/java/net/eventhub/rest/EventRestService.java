package net.eventhub.rest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.io.*;
import java.net.URI;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.DatatypeConverter;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import net.eventhub.domain.Event;
import net.eventhub.domain.EventAttendee;
import net.eventhub.domain.EventCategory;
import net.eventhub.rest.filter.Secured;
import net.eventhub.domain.Event;
import net.eventhub.service.EventBusiness;
import net.eventhub.utils.EventHubUtils;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.EventUserVO;
import net.eventhub.valueobject.EventValueObj;



@Component
@Path("/events")
//@Secured
public class EventRestService {
    
    @Autowired    
    private EventBusiness eventBusiness;
    
    // Basic CRUD operations for Event service
    
    // http://localhost:8080/EventHub/rest/events/
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)   
    public Response getAllEvents() {
    	List<Event> events = eventBusiness.getAllEvents();
    	List<EventValueObj> evo = new ArrayList<>();
    	
    	for ( Event e : events )
    	{
    		evo.add(VOEntityConverter.ToEventValueObj(e));
    	}
        return returnSearchResult(evo);
    }
    
 // http://localhost:8080/EventHub/rest/events/active/aUserId
    @GET
    @Path("active/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)   
    public Response getAllActiveEvents(@PathParam("userId") int loginUserId) {
        return returnSearchResult(eventBusiness.getAllActiveEvents(loginUserId));
    }
    
    
    
 // http://localhost:8080/EventHub/rest/events/search/past?keyword=xxx&userId=123
    @GET
    @Path("search/past/")
    @Produces(MediaType.APPLICATION_JSON)   
    public Response searchPastEvents(@QueryParam("keyword")String keyword, @QueryParam("userId") int loginUserId) {
        return returnSearchResult(eventBusiness.searchPastEventByKeyword(keyword, loginUserId));
    }
    
 // http://localhost:8080/EventHub/rest/events/search/active?keyword=xxx&userId=123
    @GET
    @Path("search/active")
    @Produces(MediaType.APPLICATION_JSON)   
    public Response searchActiveEvents(@QueryParam("keyword")String keyword, @QueryParam("userId") int loginUserId) {
        return returnSearchResult(eventBusiness.searchAvtiveEventByKeyword(keyword, loginUserId));
    }
    
    // http://localhost:8080/EventHub/rest/events/search/category
    @GET
    @Path("search/category")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchEventByCategory(@QueryParam("catIds")List categoryIds, @QueryParam("userId") int loginUserId) {
    	return returnSearchResult(eventBusiness.searchEventByCategory(categoryIds, loginUserId));
    }
    
 // http://localhost:8080/EventHub/rest/events/search/timeRange
    @GET
    @Path("search/timeRange")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchEventByTimeRange(@QueryParam("startTime")String startTime,
    						@QueryParam("endTime")String endTime, @QueryParam("userId") int loginUserId) { 
    	Date start = EventHubUtils.converStingToDate(startTime);
    	Date end = EventHubUtils.converStingToDate(endTime);
    	return returnSearchResult(eventBusiness.searchEventByTimeRange(start, end, loginUserId));
    }
    
 // http://localhost:8080/EventHub/rest/events/search/categoryAndDate
    @GET
    @Path("search/categoryAndDate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchEventByCategoryAndDate(@QueryParam("catIds")List categoryIds, @QueryParam("startTime")String startTime,
			@QueryParam("endTime")String endTime, @QueryParam("userId") int loginUserId) {
    	
    	Date start = EventHubUtils.converStingToDate(startTime);
    	Date end = EventHubUtils.converStingToDate(endTime);
    	Collection<EventValueObj> results = 
    			eventBusiness.searchEventByCategoryAndTimeRange(categoryIds, start, end, loginUserId);
    	
    	return returnSearchResult(results);
    }
    
 // http://localhost:8080/EventHub/rest/events/search
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchEvents(@QueryParam("catIds")List categoryIds, @QueryParam("startTime")String startTime,
			@QueryParam("endTime")String endTime, @QueryParam("keyword")String keyword,@QueryParam("userId") int loginUserId) {
    	
    	Date start = EventHubUtils.converStingToDate(startTime);
    	Date end = EventHubUtils.converStingToDate(endTime);
    	Collection<EventValueObj> results = 
    			eventBusiness.searchEvents(categoryIds, start, end, keyword, loginUserId);
    	
    	return returnSearchResult(results);
    }

    
 // http://localhost:8080/EventHub/rest/events/getHostedEvents/"hostId"
    @GET
    @Path("/getHostedEvents/{hostId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventByHostId(@PathParam("hostId") int hostId) {
    	Collection<EventValueObj> results = eventBusiness.getEventByHostId(hostId);
    	
    	return returnSearchResult(results);
    }

    
    private Response returnSearchResult(Object results)
    {
    	if ( results == null )
    	{
    		return Response.status(Status.NO_CONTENT).entity(new ArrayList()).build();
    	}
    	
    	return Response.ok().entity(results).build();
    }
 
    
 // http://localhost:8080/EventHub/rest/events/get/1
    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventById(@PathParam("id")int eventId) {
        return getEvent(eventId);
    }
    
    // http://localhost:8080/EventHub/rest/events/get/1
    @GET
    @Path("get/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    public Response getEvent(@PathParam("id") int eventId) {
    	EventValueObj eventValueObj =  eventBusiness.getEventById(eventId);
    	
    	return returnSearchResult(eventValueObj);
    }
    
    
    
 // http://localhost:8080/EventHub/rest/events/json/add
    @POST
    @Path("json/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEvent(EventValueObj newEvent) {
    	if(newEvent == null ) {
            return EventHubUtils.validationError();
        }
    	    		
    	newEvent = eventBusiness.createEvent(newEvent);
    	
    	if(newEvent == null) {
    		
            return EventHubUtils.errorReturn();
        }
        
        return returnSearchResult(newEvent);
    }
    
    // http://localhost:8080/EventHub/rest/events/json/update
    @PUT 
    @Path("json/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEvent(EventValueObj event) {
    	
    	if(event == null ) {
            return EventHubUtils.validationError();
        }
    	try {   		
    		eventBusiness.updateEvent(event);
    	}catch (Exception e) {
    		e.printStackTrace();
    		return EventHubUtils.errorReturn();
		}
    	
        
        return Response.ok().entity(eventBusiness.getEventById(event.getId())).build();
       
    }
    
 // http://localhost:8080/EventHub/rest/events/updateHostNotis/1234/true
    @PUT 
    @Path("updateHostNotis/{id}/{hostNotis}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateHostNotis(@PathParam("id") Integer eventId, @PathParam("hostNotis") Boolean hostNotis) {
    	
    	if(eventId == null || hostNotis == null ) {
            return EventHubUtils.validationError();
        }
    	try {   		
    		eventBusiness.updateHostNotis(eventId, hostNotis);
    	}catch (Exception e) {
    		e.printStackTrace();
    		return EventHubUtils.errorReturn();
		}
    	
        
    	return Response.ok("Success").build();
       
    }
    
    // http://localhost:8080/EventHub/rest/events/delete/1
    @DELETE
    @Path("delete/{id}")
    public Response deleteEvent(@PathParam("id") int eventId) {
        try {
			eventBusiness.deleteEvent(eventId);
		} catch (Exception e) {
			e.printStackTrace();
			return EventHubUtils.errorReturn();
		}
        return Response.status(Response.Status.OK).entity("Success").build();
    }
    
	public EventBusiness getEventBusiness() {
		return eventBusiness;
	}

	public void setEventBusiness(EventBusiness eventBusiness) {
		this.eventBusiness = eventBusiness;
	}
    
    
}

