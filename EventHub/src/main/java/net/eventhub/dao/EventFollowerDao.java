package net.eventhub.dao;

import java.util.Collection;
import java.util.List;

import net.eventhub.domain.Event;
import net.eventhub.domain.EventFollower;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.User;
import net.eventhub.valueobject.EventValueObj;

public interface EventFollowerDao extends GenericDao<EventFollower>{
	
	Collection<EventValueObj> getFollowingEvents(int userId);
	
	void addFollowingEvents(List<EventFollower> followingEvents);
	
	void deleteFollower(int eventId, int userId);

}
