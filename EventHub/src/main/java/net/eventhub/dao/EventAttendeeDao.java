package net.eventhub.dao;

import java.util.Collection;
import java.util.List;

import net.eventhub.domain.Event;
import net.eventhub.domain.EventAttendee;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.User;
import net.eventhub.valueobject.EventValueObj;

public interface EventAttendeeDao extends GenericDao<EventAttendee>{
	List<User> getEventAttendees(int eventId);
	
	void addAttendees(List<EventAttendee> attendees);
	
	Collection<EventValueObj> getEventsByAttendeeId(int attendeeId);
	
	void deleteAttendee(int eventId, int userId);

}
