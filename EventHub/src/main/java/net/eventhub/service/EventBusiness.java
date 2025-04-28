package net.eventhub.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.eventhub.domain.Event;
import net.eventhub.valueobject.EventValueObj;




public interface EventBusiness {
	
	List<Event> getAllEvents();
    
    EventValueObj getEventById(int eventId);
    
    List<Event> getEventTitle(int eventId);
    
    Collection<EventValueObj> getAllActiveEvents(int loginUserId);
    
    Collection<EventValueObj> searchPastEventByKeyword(String keyword, int loginUserId);
    
    Collection<EventValueObj> searchAvtiveEventByKeyword(String keyword, int loginUserId);
    
    List<EventValueObj> searchAvtiveEventByCategory(List categoryIds);
    
    List<EventValueObj> searchPastEventByCategory(List categoryIds);
    
    Collection<EventValueObj> searchEvents(List<Integer>categoryIds,Date startTime, Date endTime, String keyword, int loginUserId);
    
    Collection<EventValueObj> searchEventByCategory(List categoryIds, int loginUserId);
    
    Collection<EventValueObj> searchEventByTimeRange(Date startTime, Date endTime, int loginUserId);
    
    Collection<EventValueObj> searchEventByCategoryAndTimeRange(List<Integer>categoryIds,Date startTime, Date endTime, int loginUserId);
    
    Collection<EventValueObj> getEventByHostId(int hostId);
    
    EventValueObj createEvent(EventValueObj newEvent) ;
    
    void updateEvent(EventValueObj updatedEvent) throws IllegalAccessException, InvocationTargetException;
    
    void updateHostNotis(int eventId, Boolean hostNotis);
    
    void insertImage(int eventId, String imageName);
    

    void deleteEvent(int eventId);
    
    
    void deleteEvent(Event deletingEvent);


}
