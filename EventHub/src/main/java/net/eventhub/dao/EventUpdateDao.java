package net.eventhub.dao;

import java.util.Collection;
import java.util.List;

import net.eventhub.domain.EventUpdate;
import net.eventhub.valueobject.EventUserVO;

public interface EventUpdateDao extends GenericDao<EventUpdate>{
	
	List<String> getEventUpdates(int eventId);
	
	Collection<EventUserVO> getUpdatesByUserId(int UserId);

	List<EventUserVO> getAllEvewntUpdates();

}
