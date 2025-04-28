package net.eventhub.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.eventhub.dao.EventAttendeeDao;
import net.eventhub.dao.EventInviteeDao;
import net.eventhub.domain.Event;
import net.eventhub.domain.EventAttendee;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.User;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.EventUserVO;
import net.eventhub.valueobject.EventValueObj;

@Service("eventInviteeBusiness")
@Transactional(readOnly = true)
public class EventInviteeBusinessImpl implements EventInviteeBusiness {

	@Autowired
    private EventInviteeDao eventInviteeDao;
	
	@Override
	public List<EventInvitee> getAllEventInvitee() {
		// TODO Auto-generated method stub
		return eventInviteeDao.findAll();
	}

	@Override
	public EventInvitee getEventInviteeById(int eventInviteeId) {
		// TODO Auto-generated method stub
		return eventInviteeDao.findById(eventInviteeId);
	}

	@Transactional(readOnly = false)
    public EventInvitee createEventInvitee(EventInvitee newEventInvitee) {
 
    	if ( newEventInvitee != null )
    	{
    		int eventInviteeId = (Integer)eventInviteeDao.save(newEventInvitee);
    		newEventInvitee = (EventInvitee)eventInviteeDao.findById(eventInviteeId);
    	}
        
        return newEventInvitee;
    }
    
    @Transactional(readOnly = false)
    public EventInvitee updateEventInvitee(EventUserVO updatingEventInvitee) { 
    	EventInvitee newInvitee = VOEntityConverter.toEventInvitee(updatingEventInvitee);
    	
    	List<EventInvitee> invitees = 
    			eventInviteeDao.getByEventAndUser(updatingEventInvitee.getEventId(), updatingEventInvitee.getUserId());
    	EventInvitee existingInvitee = null;
    	if ( invitees != null )
    	{
    		existingInvitee = invitees.get(0);
    	}
    	newInvitee.setId(existingInvitee.getId());
    	existingInvitee = eventInviteeDao.merge(newInvitee);
    	
    	return existingInvitee;
    }
    
    @Transactional(readOnly = false)
    public void deleteEventInvitee(int eventInviteeId) throws Exception {
    	EventInvitee deletingEventInvitee = getEventInviteeById(eventInviteeId);
        
    	eventInviteeDao.delete(deletingEventInvitee);       
    }
    
    @Transactional(readOnly = false)
    public void deleteEventInvitee(EventInvitee deletingEventInvitee) throws Exception {
    	eventInviteeDao.delete(deletingEventInvitee);       
    }
	public EventInviteeDao getEventInviteeDao() {
		return eventInviteeDao;
	}

	public void setEventAttendeeDao(EventInviteeDao eventInviteeDao) {
		this.eventInviteeDao = eventInviteeDao;
	}

	@Override
	public Collection<EventUserVO> getEventInvitees(int eventId) {
		// TODO Auto-generated method stub
		return eventInviteeDao.getEventInvitees(eventId);
	}

	@Override
	@Transactional(readOnly = false)
	public void addListOfInvitees(List<EventUserVO> invitees) {
		List<EventInvitee> newInvitees = new ArrayList<>();
		for(EventUserVO vo : invitees)
		{
			EventInvitee entity = VOEntityConverter.toEventInvitee(vo);			
			newInvitees.add(entity);
			
		}
		eventInviteeDao.addInvitees(newInvitees);
	}

	@Transactional(readOnly = false)
	@Override
	
	public void deleteEventInvitee(int eventId, int userId) {
		eventInviteeDao.deleteInvitee(eventId, userId);		
	}

	@Override
	public Collection<EventUserVO> getUsersInviteEvents(int userId) {
		// TODO Auto-generated method stub
		return eventInviteeDao.getUsersInvites(userId);
	}

}
