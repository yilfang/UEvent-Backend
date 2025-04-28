package net.eventhub.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.eventhub.dao.EventAttendeeDao;
import net.eventhub.dao.EventFollowerDao;
import net.eventhub.domain.Event;
import net.eventhub.domain.EventAttendee;
import net.eventhub.domain.EventFollower;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.User;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.EventUserVO;
import net.eventhub.valueobject.EventValueObj;

@Service("eventFollowerBusiness")
@Transactional(readOnly = true)
public class EventFollowerBusinessImpl implements EventFollowerBusiness {

	@Autowired
    private EventFollowerDao eventFollowerDao;
	
	@Override
	public List<EventFollower> getAllEventFollower() {
		// TODO Auto-generated method stub
		return eventFollowerDao.findAll();
	}

	@Override
	public EventFollower getEventFollowerById(int eventFollowerId) {
		// TODO Auto-generated method stub
		return eventFollowerDao.findById(eventFollowerId);
	}

	@Transactional(readOnly = false)
    public EventFollower createEventFollower(EventUserVO eventFollower) {
		EventFollower newEventFollower = null;
    	if ( eventFollower != null )
    	{
    		EventFollower follower = VOEntityConverter.toEventFollower(eventFollower);
    		int eventFollowerId = (Integer)eventFollowerDao.save(follower);
    		newEventFollower = (EventFollower)eventFollowerDao.findById(eventFollowerId);
    	}
        
        return newEventFollower;
    }
    
    @Transactional(readOnly = false)
    public void updateEventFollower(EventFollower updatingEventFollower) {       
    	eventFollowerDao.saveOrUpdate(updatingEventFollower);            
    }
    
    @Transactional(readOnly = false)
    public void deleteEventFollower(int eventFollowerId) throws Exception {
    	EventFollower deletingEventFollower = getEventFollowerById(eventFollowerId);
        
    	eventFollowerDao.delete(deletingEventFollower);       
    }
    
    @Transactional(readOnly = false)
    public void deleteEventFollower(EventFollower deletingEventFollower) throws Exception {
    	eventFollowerDao.delete(deletingEventFollower);       
    }
    
    @Transactional(readOnly = false)
    public void deleteEventFollower(int eventId, int userId) {
    	eventFollowerDao.deleteFollower(eventId, userId);       
    }
    
	public EventFollowerDao getEventFollowerDao() {
		return eventFollowerDao;
	}

	public void setEventAttendeeDao(EventFollowerDao eventFollowerDao) {
		this.eventFollowerDao = eventFollowerDao;
	}

	@Override
	public Collection<EventValueObj> getFollowingEvents(int userId) {
		// TODO Auto-generated method stub
		return eventFollowerDao.getFollowingEvents(userId);
	}

	@Override
	@Transactional(readOnly = false)
	public void addFollowingEvents(List<EventUserVO> followingEvents) {
		List<EventFollower> newEvents = new ArrayList<>();
		for(EventUserVO vo : followingEvents)
		{
			EventFollower entity = VOEntityConverter.toEventFollower(vo);			
			newEvents.add(entity);
			
		}
		eventFollowerDao.addFollowingEvents(newEvents);
		
	}

}
