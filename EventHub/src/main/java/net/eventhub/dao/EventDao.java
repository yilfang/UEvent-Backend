package net.eventhub.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.eventhub.domain.Event;
import net.eventhub.valueobject.EventValueObj;
import net.eventhub.valueobject.LocationCoordinates;


public interface EventDao extends GenericDao<Event>{
	
	Collection<EventValueObj> getAllActiveEvents(int loginUserId);
	
	Collection<EventValueObj> searchEventByKeyWord(String keyWord, Boolean active, int loginUserId);
	
	Collection<EventValueObj> searchEventByCategories(List categoryIds, Boolean active, int loginUserId);
	
	Collection<EventValueObj> searchEventByTimeRange(Date startTime, Date endTime, int loginUserId);
	
	Collection<EventValueObj> searchEventByCategoryAndTimeRange(Date startTime, Date endTime, 
														List<Integer> categoriesIds, int loginUserId);
	
	Collection<EventValueObj> searchEvents(Date startTime, Date endTime, 
					List<Integer> categoriesIds, String keyword, int loginUserId);
	LocationCoordinates getCoordinates(List<LocationCoordinates> coordinates);
	
	Collection<EventValueObj> getEventsByHostId(int hostId);
	
	void insertImage(int eventId, String imageName);
	
	void deleteEvent(Integer eventId);
	
	void updateHostNotis(int eventId, Boolean hostNotis);

}
