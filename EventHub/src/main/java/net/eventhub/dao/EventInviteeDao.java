package net.eventhub.dao;

import java.util.Collection;
import java.util.List;

import net.eventhub.domain.Event;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.User;
import net.eventhub.valueobject.EventUserVO;
import net.eventhub.valueobject.EventValueObj;

public interface EventInviteeDao extends GenericDao<EventInvitee>{
	
	Collection<EventUserVO> getEventInvitees(int eventId);
	
	Collection<EventUserVO> getUsersInvites(int userId);
	
	void addInvitees(List<EventInvitee> invitees);
	
	void deleteInvitee(int eventId, int userId);
	
	List<EventInvitee> getByEventAndUser(int eventId, int userId);

}
