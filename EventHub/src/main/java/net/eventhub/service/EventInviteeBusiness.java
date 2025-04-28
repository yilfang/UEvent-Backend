package net.eventhub.service;

import java.util.Collection;
import java.util.List;

import net.eventhub.domain.Event;
import net.eventhub.domain.EventAttendee;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.User;
import net.eventhub.valueobject.EventUserVO;
import net.eventhub.valueobject.EventValueObj;

public interface EventInviteeBusiness {
	
	Collection<EventUserVO> getEventInvitees(int eventId);
	
	Collection<EventUserVO> getUsersInviteEvents(int userId);
	
	void addListOfInvitees(List<EventUserVO> invitees);
	
	List<EventInvitee> getAllEventInvitee();
    
	EventInvitee getEventInviteeById(int eventInviteeId);
    
	EventInvitee createEventInvitee(EventInvitee newEventInvitee) ;
    

	EventInvitee updateEventInvitee(EventUserVO updatingEventInvitee);
    

    void deleteEventInvitee(int eventInviteeId) throws Exception;
    
    
    void deleteEventInvitee(EventInvitee deletingEventInvitee) throws Exception ;

    void deleteEventInvitee(int eventId, int userId);
}
