package net.eventhub.dao.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import net.eventhub.dao.CategoryDao;
import net.eventhub.dao.EventDao;
import net.eventhub.dao.EventInviteeDao;
import net.eventhub.domain.Category;
import net.eventhub.domain.Event;
import net.eventhub.domain.EventAttendee;
import net.eventhub.domain.EventFollower;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.User;
import net.eventhub.utils.EventHubConstants;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.EventUserVO;
import net.eventhub.valueobject.EventValueObj;

@Repository("eventInviteeDao")
public class EventInviteeDaoImpl extends AbstractGenericDao<EventInvitee> implements EventInviteeDao{

	@Override
	public Collection<EventUserVO> getEventInvitees(int eventId) {
		Session session = getSession();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<EventInvitee> query = builder.createQuery(EventInvitee.class);
     // write the Root, Path elements as usual
        Root<EventInvitee> root = query.from(EventInvitee.class);
        query.select(root);  //using metamodel
        Join<Object, Object> eventJoin = root.join("event");
        query.where(builder.equal(eventJoin.get("eventId"), eventId)).distinct(true);
        List<EventInvitee> search = session.createQuery(query).getResultList();
        
        Set<EventUserVO> result = new HashSet<>();
        
        for(EventInvitee i : search)
        {
        	EventUserVO vo = VOEntityConverter.fromEventInvitee(i);
        	vo.setEvent(setUserInfo(vo, i, i.getInvitee().getId()));
        	result.add(vo);
        }
        
		return result;

	}

	@Override
	public void addInvitees(List<EventInvitee> invitees) {
		int i = 0;
		for(EventInvitee invitee : invitees)
		{
			i++;
			save(invitee);
			if ( i % EventHubConstants.batch_size == 0)
			{
				getSession().flush();
				getSession().clear();
			}
		}
		
	}
	
	@Override
	public Serializable save(EventInvitee invitee)
	{
		Serializable id = null;
		List<EventInvitee> existings = 
				getByEventAndUser(invitee.getEvent().getEventId(), invitee.getInvitee().getId());
		if ( existings == null || existings.size() == 0 )
		{
			id = super.save(invitee);
		}
		else
		{
			
			id = existings.get(0).getId();
		}
		
		return id;
	}

	@Override
	public void deleteInvitee(int eventId, int userId) {
		List<EventInvitee> invitees = getByEventAndUser(eventId, userId);
		
		if( invitees != null )
		{
			for(EventInvitee invitee : invitees )				
				delete(invitee);
		}
		
	}
	
	public List<EventInvitee> getByEventAndUser(int eventId, int userId)
	{
		Session session = getSession();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<EventInvitee> query = builder.createQuery(EventInvitee.class);
     // write the Root, Path elements as usual
        Root<EventInvitee> root = query.from(EventInvitee.class);
        query.select(root);  //using metamodel
        //Join<Object, Object> userJoin = root.join("follower");
        Predicate userP = builder.equal(root.get("invitee").get("id"), userId);
        
        //Join<Object, Object> evnetJoin = root.join("event");
        Predicate  eventP = builder.equal(root.get("event").get("eventId"), eventId);
        
        query.where( builder.and(userP, eventP)).distinct(true);
        List<EventInvitee> result = session.createQuery(query).getResultList();
        
		return result;
	}

	@Override
	public Collection<EventUserVO> getUsersInvites(int userId) {
		Session session = getSession();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<EventInvitee> query = builder.createQuery(EventInvitee.class);
     // write the Root, Path elements as usual
        Root<EventInvitee> root = query.from(EventInvitee.class);
        query.select(root);  //using metamodel
        Predicate userP = builder.equal(root.get("invitee").get("id"), userId);
        Predicate eventP = 
        		builder.and(builder.equal(root.get("event").get("disabled"), 0),
        					builder.greaterThan(root.get("event").get("endTime"), builder.currentTimestamp()));
        query.where(builder.and(userP, eventP)).distinct(true);
        List<EventInvitee> search = session.createQuery(query).getResultList();
        Set<EventUserVO> result = new HashSet<>();
        
        for(EventInvitee i : search) 
        {
        	EventUserVO vo = VOEntityConverter.fromEventInvitee(i);
        	vo.setEvent(setUserInfo(vo, i, userId));
        	
        	result.add(vo);
        }
        
		return result;
	}
	
	private EventValueObj setUserInfo(EventUserVO vo, EventInvitee invitede, int userId)
	{
		EventValueObj obj = vo.getEvent();
    	Event e = invitede.getEvent();
    	
    	obj.setIsAttendee(e.isAttendee(userId));
    	obj.setIsFollower(e.isFollower(userId));
    	
    	return obj;
	}


}

