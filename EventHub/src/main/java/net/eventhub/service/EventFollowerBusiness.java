package net.eventhub.service;

import java.util.Collection;
import java.util.List;

import net.eventhub.domain.Event;
import net.eventhub.domain.EventFollower;
import net.eventhub.valueobject.EventUserVO;
import net.eventhub.valueobject.EventValueObj;

public interface EventFollowerBusiness {
	
	Collection<EventValueObj> getFollowingEvents(int userId);
	
	void addFollowingEvents(List<EventUserVO> followingEvents);
	
	List<EventFollower> getAllEventFollower();
    
	EventFollower getEventFollowerById(int eventFollowerId);
    
	EventFollower createEventFollower(EventUserVO eventFollower) ;
    

    void updateEventFollower(EventFollower updatingEventFollower);
    

    void deleteEventFollower(int eventFollowerId) throws Exception;
    
    
    void deleteEventFollower(EventFollower deletingEventFollower) throws Exception;
    
    void deleteEventFollower(int eventId, int userId);

}
