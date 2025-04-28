package net.eventhub.service;

import java.util.Collection;
import java.util.List;

import net.eventhub.domain.Event;
import net.eventhub.domain.EventAttendee;
import net.eventhub.domain.User;
import net.eventhub.valueobject.EventUserVO;
import net.eventhub.valueobject.EventValueObj;

public interface EventAttendeeBusiness {
	
	List<User> getEventAttendees(int eventId);
	
	Collection<EventValueObj> getAttendingEvents(int attendId);
	
	void addEventAttendees(List<EventUserVO> attendees);
	
	List<EventAttendee> getAllEventAttendee();
    
	EventAttendee getEventAttendeeById(int eventAttendeeId);
    
	EventAttendee createEventAttendee(EventUserVO newEventAttendee) ;
    

    void updateEventAttendee(EventAttendee updatingEventAttendee);
    

    void deleteEventAttendee(int eventAttendeeId) throws Exception;
    
    
    void deleteEventAttendee(EventAttendee deletingEventAttendee) throws Exception ;
    
    void deleteEventAttendee(int eventId, int userId);


}
