package net.eventhub.service;

import java.util.Collection;
import java.util.List;
import net.eventhub.domain.EventUpdate;
import net.eventhub.valueobject.EventUserVO;

public interface EventUpdateBusiness {
	
	List<String> getEventUpdates(int eventId);
	
	Collection<EventUserVO> getUpdatesByUserId(int userId);
    
	EventUserVO getEventUpdateById(int eventUpdateId);
    
	Integer createEventUpdate(EventUserVO newEventUpdate) ;
    

    void updateEventUpdate(EventUpdate updatingEventUpdate);
    

    void deleteEventUpdate(int eventUpdateId);

	List<EventUserVO> getAllEventUpdate();


}
