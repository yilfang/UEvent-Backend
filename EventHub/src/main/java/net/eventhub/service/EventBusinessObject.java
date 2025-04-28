package net.eventhub.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
//import javax.ws.rs.DELETE;
import javax.ws.rs.Path;

import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
//import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.eventhub.dao.EventDao;
import net.eventhub.dao.UserDao;
import net.eventhub.domain.Event;
import net.eventhub.domain.User;
import net.eventhub.utils.EventHubUtils;
import net.eventhub.utils.NullAwareBeanUtilsBean;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.EventValueObj;
import net.eventhub.valueobject.LocationCoordinates;

@Service("eventBusiness")
@Transactional(readOnly = true)
public class EventBusinessObject implements EventBusiness {
    
    @Autowired
    private EventDao eventDAO;

	@Override
	public List<Event> getAllEvents() {
		List<Event> allEvents = eventDAO.findAll();
		
		for(Event event : allEvents)
		{
			Hibernate.initialize(event.getCategories());
			Hibernate.initialize(event.getUpdates());
		}
		
		return allEvents;
		//return eventDAO.findAll();
	}

	@Override
	public EventValueObj getEventById(int eventId) {
		
		Event event = eventDAO.findById(eventId);
		
		if ( event != null )
		{
			return VOEntityConverter.ToEventValueObj(event);
		}
		
		return null;
				
	}

	@Override
	@Transactional(readOnly = false)
	public EventValueObj createEvent(EventValueObj newEvent) {
		String base64String = newEvent.getImageData();
		boolean hasImage = base64String != null && base64String.length() > 0;
		
		Event event = VOEntityConverter.toEvent(newEvent, hasImage);
		List<LocationCoordinates> coors = newEvent.getCoordinates();
		LocationCoordinates availableCoor = calCoors(coors);		
		event.setLongitude(availableCoor.getLongitude());
		event.setLatitude(availableCoor.getLatitude());
				
		int eventId = (Integer)eventDAO.save(event);
		
		if (  hasImage )
		{
			EventHubUtils.writeImageToDisk(base64String, event.getEventImage(), eventId);	
		}
		
		
		return getEventById(eventId);
	}
	
	private LocationCoordinates calCoors(List<LocationCoordinates> coors)
	{
		
		LocationCoordinates availableCoor = eventDAO.getCoordinates(coors);
		if ( availableCoor == null)
		{
			availableCoor = coors.get(0);
		}
		
		return availableCoor;
	}

	@Override
	@Transactional(readOnly = false)
	public void updateEvent(EventValueObj updatingEvent) {
		String base64String = updatingEvent.getImageData();
		boolean hasImage = base64String != null && base64String.length() > 0;
		Event newEvent = VOEntityConverter.toEvent(updatingEvent, hasImage);
		if ( updatingEvent.getCoordinates() != null && updatingEvent.getCoordinates().size() > 0 )
		{
			LocationCoordinates availableCoor = calCoors(updatingEvent.getCoordinates());
			newEvent.setLatitude(availableCoor.getLatitude());
			newEvent.setLongitude(availableCoor.getLongitude());
		}
		Event existingEvent = eventDAO.findById(updatingEvent.getId());
		Boolean deleteImage = updatingEvent.getIsImageDeleted();
		if ( !hasImage && 
				( deleteImage == null || deleteImage == false) )
		{
			newEvent.setEventImage(existingEvent.getEventImage());
		}
		existingEvent = eventDAO.merge(newEvent);
		
		if ( hasImage )
		{
			EventHubUtils.writeImageToDisk(base64String, existingEvent.getEventImage(), existingEvent.getEventId());			
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteEvent(int eventId) {
		  eventDAO.deleteEvent(eventId);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteEvent(Event deletingEvent) {
		eventDAO.delete(deletingEvent);		
	}

	@Override
	public List<Event> getEventTitle(int eventId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Collection<EventValueObj> getAllActiveEvents(int loginUserId) {
		
		Collection<EventValueObj> activeEvents = eventDAO.getAllActiveEvents(loginUserId);
		
		return activeEvents;	
		
	}

	@Override
	public Collection<EventValueObj> searchPastEventByKeyword(String keyword, int loginUserId) {
		Collection<EventValueObj> events = eventDAO.searchEventByKeyWord(keyword, false, loginUserId);
		
		return events;
	}

	@Override
	public Collection<EventValueObj> searchAvtiveEventByKeyword(String keyword, int loginUserId) {
		Collection<EventValueObj> events = eventDAO.searchEventByKeyWord(keyword, true, loginUserId);
		
		return events;
	}

	@Override
	public List<EventValueObj> searchAvtiveEventByCategory(List categoryIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventValueObj> searchPastEventByCategory(List categoryIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<EventValueObj> searchEventByCategory(List categoryIds, int loginUserId) {
		Collection<EventValueObj> events = eventDAO.searchEventByCategories(categoryIds, true, loginUserId);
		return events;
	}

	@Override
	public Collection<EventValueObj> searchEventByTimeRange(Date startTime, Date endTime, int loginUserId) {
		Collection<EventValueObj> events = eventDAO.searchEventByTimeRange(startTime, endTime, loginUserId);
		return events;
	}

	@Override
	public Collection<EventValueObj> searchEventByCategoryAndTimeRange(List<Integer>categoryIds, Date startTime, 
														Date endTime, int loginUserId) {
		Collection<EventValueObj> events = eventDAO.searchEventByCategoryAndTimeRange(startTime, endTime, categoryIds, loginUserId);
		return events;
	}

	@Override
	public Collection<EventValueObj> getEventByHostId(int hostId) {
		
		return eventDAO.getEventsByHostId(hostId);
	}

	@Override
	@Transactional(readOnly = false)
	public void insertImage(int eventId, String imageName) {
		eventDAO.insertImage(eventId, imageName);
		
	}

	@Override
	public Collection<EventValueObj> searchEvents(List<Integer> categoryIds, Date startTime, Date endTime,
			String keyword, int loginUserId) {
		Collection<EventValueObj> events = eventDAO.searchEvents(startTime, endTime, categoryIds, keyword, loginUserId);
		return events;
	}

	@Override
	@Transactional(readOnly = false)
	public void updateHostNotis(int eventId, Boolean hostNotis) {
		eventDAO.updateHostNotis(eventId, hostNotis);
		
	}
    
}

