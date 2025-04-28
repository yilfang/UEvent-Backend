package net.eventhub.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.eventhub.dao.EventAttendeeDao;
import net.eventhub.dao.EventUpdateDao;
import net.eventhub.domain.EventAttendee;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.EventUpdate;
import net.eventhub.domain.User;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.EventUserVO;

@Service("eventUpdateBusiness")
@Transactional(readOnly = true)
public class EventUpdateBusinessImpl implements EventUpdateBusiness {

	@Autowired
    private EventUpdateDao eventUpdateDao;
	
	
	@Override
	public List<EventUserVO> getAllEventUpdate() {
		// TODO Auto-generated method stub
		return eventUpdateDao.getAllEvewntUpdates();
	}

	@Override
	public EventUserVO getEventUpdateById(int eventUpdateId) {
		EventUpdate updates = eventUpdateDao.findById(eventUpdateId);
		EventUserVO eventUserVO = new EventUserVO(updates.getUpdate(), updates.getEvent().getEventId());
		return eventUserVO;
	}

	@Transactional(readOnly = false)
    public Integer createEventUpdate(EventUserVO newEventUpdate) {
		
		EventUpdate updates = VOEntityConverter.toEventUpdate(newEventUpdate);
    	Integer eventUpdateId = (Integer)eventUpdateDao.save(updates);
    	        
        return eventUpdateId;
    }
    
    @Transactional(readOnly = false)
    public void updateEventUpdate(EventUpdate updatingEventUpdate) {       
    	eventUpdateDao.saveOrUpdate(updatingEventUpdate);            
    }
    
    @Transactional(readOnly = false)
    public void deleteEventUpdate(int eventUpdateId){
    	EventUpdate deletingEventUpdate = eventUpdateDao.findById(eventUpdateId);
        
    	eventUpdateDao.delete(deletingEventUpdate);       
    }
    
    @Transactional(readOnly = false)
    public void deleteEventUpdate(EventUpdate deletingEventUpdate) throws Exception {
    	eventUpdateDao.delete(deletingEventUpdate);       
    }
    
    @Override
	public List<String> getEventUpdates(int eventId) {
		// TODO Auto-generated method stub
		return eventUpdateDao.getEventUpdates(eventId);
	}
    
    @Override
	public Collection<EventUserVO> getUpdatesByUserId(int userId) {
		// TODO Auto-generated method stub
		return eventUpdateDao.getUpdatesByUserId(userId);
	}
    
	public EventUpdateDao getEventUpdateDao() {
		return eventUpdateDao;
	}

	public void setEventUpdateDao(EventUpdateDao eventUpdateDao) {
		this.eventUpdateDao = eventUpdateDao;
	}

}
