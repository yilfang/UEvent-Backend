package net.eventhub.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.eventhub.dao.EventAttendeeDao;
import net.eventhub.domain.Event;
import net.eventhub.domain.EventAttendee;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.User;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.EventUserVO;
import net.eventhub.valueobject.EventValueObj;

@Service("eventAttendeeBusiness")
@Transactional(readOnly = true)
public class EventAttendeeBusinessImpl implements EventAttendeeBusiness {

	@Autowired
    private EventAttendeeDao eventAttendeeDao;
	
	@Override
	public List<EventAttendee> getAllEventAttendee() {
		// TODO Auto-generated method stub
		return eventAttendeeDao.findAll();
	}

	@Override
	public EventAttendee getEventAttendeeById(int eventAttendeeId) {
		// TODO Auto-generated method stub
		return eventAttendeeDao.findById(eventAttendeeId);
	}

	@Transactional(readOnly = false)
    public EventAttendee createEventAttendee(EventUserVO newEventAttendee) {
			
		EventAttendee attendee = VOEntityConverter.toEventAttendee(newEventAttendee);
    	int eventAttendeeId = (Integer)eventAttendeeDao.save(attendee);
    	attendee = (EventAttendee)eventAttendeeDao.findById(eventAttendeeId);
    	        
        return attendee;
    }
    
    @Transactional(readOnly = false)
    public void updateEventAttendee(EventAttendee updatingEventAttendee) {       
    	eventAttendeeDao.saveOrUpdate(updatingEventAttendee);            
    }
    
    @Transactional(readOnly = false)
    public void deleteEventAttendee(int eventAttendeeId) throws Exception {
    	EventAttendee deletingEventAttendee = getEventAttendeeById(eventAttendeeId);
        
    	eventAttendeeDao.delete(deletingEventAttendee);       
    }
    
    @Transactional(readOnly = false)
    public void deleteEventAttendee(EventAttendee deletingEventAttendee) throws Exception {
    	eventAttendeeDao.delete(deletingEventAttendee);       
    }
	public EventAttendeeDao getEventAttendeeDao() {
		return eventAttendeeDao;
	}

	public void setEventAttendeeDao(EventAttendeeDao eventAttendeeDao) {
		this.eventAttendeeDao = eventAttendeeDao;
	}

	@Override
	public List<User> getEventAttendees(int eventId) {
		// TODO Auto-generated method stub
		return eventAttendeeDao.getEventAttendees(eventId);
	}
	
	@Transactional(readOnly = false)
	@Override
	public void addEventAttendees(List<EventUserVO> attendees) {
		List<EventAttendee> newAttendees = new ArrayList<>();
		for(EventUserVO vo : attendees)
		{
			EventAttendee entity = VOEntityConverter.toEventAttendee(vo);			
			newAttendees.add(entity);
			
		}
		eventAttendeeDao.addAttendees(newAttendees);
		
	}

	@Override
	public Collection<EventValueObj> getAttendingEvents(int attendId) {
		// TODO Auto-generated method stub
		return eventAttendeeDao.getEventsByAttendeeId(attendId);
	}
	
	@Transactional(readOnly = false)
	@Override
	public void deleteEventAttendee(int eventId, int userId) {
		eventAttendeeDao.deleteAttendee(eventId, userId);
		
	}

}
