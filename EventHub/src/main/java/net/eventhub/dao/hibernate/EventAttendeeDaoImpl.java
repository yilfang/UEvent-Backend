package net.eventhub.dao.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import net.eventhub.dao.CategoryDao;
import net.eventhub.dao.EventAttendeeDao;
import net.eventhub.dao.EventDao;
import net.eventhub.domain.Category;
import net.eventhub.domain.Event;
import net.eventhub.domain.EventAttendee;
import net.eventhub.domain.EventFollower;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.User;
import net.eventhub.utils.EventHubConstants;
import net.eventhub.valueobject.EventValueObj;

@Repository("eventAttendeeDao")
public class EventAttendeeDaoImpl extends AbstractGenericDao<EventAttendee> implements EventAttendeeDao{

	@Override
	public List<User> getEventAttendees(int eventId) {
		Session session = getSession();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<User> query = builder.createQuery(User.class);
     // write the Root, Path elements as usual
        Root<EventAttendee> root = query.from(EventAttendee.class);
        query.select(root.get("attendee"));  //using metamodel
        Join<Object, Object> eventJoin = root.join("event");
        query.where(builder.equal(eventJoin.get("eventId"), eventId));
        List<User> result = session.createQuery(query).getResultList();
        
		return result;
	}

	@Override
	public void addAttendees(List<EventAttendee> attendees) {
		int i = 0;
		for(EventAttendee attendee : attendees)
		{
			i++;
			save(attendee);
			if ( i % EventHubConstants.batch_size == 0)
			{
				getSession().flush();
				getSession().clear();
			}
		}
		
	}

	@Override
	public Collection<EventValueObj> getEventsByAttendeeId(int attendeeId) {
		Session session = getSession();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
     // write the Root, Path elements as usual
        Root<EventAttendee> root = query.from(EventAttendee.class);
        query.select(root.get("event"));  //using metamodel
        Join<EventAttendee, User> attendeeJoin = root.join("attendee");
        Predicate atendeeP = builder.equal(attendeeJoin.get("id"), attendeeId);
        Predicate liveEventP = 
        		builder.and(builder.equal(root.get("event").get("disabled"), 0),
        							builder.lessThan(builder.currentTimestamp(), root.get("event").get("endTime")));
        query.where(builder.and(atendeeP, liveEventP)).distinct(true);
        
        List<Event> result = session.createQuery(query).getResultList();
        
		return DetachedEntityConverter.convertTo(result, attendeeId);
	}
	
	public Serializable save(EventAttendee attendee)
	{
		Serializable id = null;
		List<EventAttendee> existings = 
				getByEventAndUser(attendee.getEvent().getEventId(), attendee.getAttendee().getId());
		
		if ( existings == null || existings.size() == 0 )
		{
			id = super.save(attendee);
		}
		else
		{
			
			id = existings.get(0).getId();
		}
		
		return id;
	}

	@Override
	public void deleteAttendee(int eventId, int userId) {
		List<EventAttendee> existings = getByEventAndUser(eventId, userId);
		if ( existings != null && existings.size() > 0)
		{
			for(EventAttendee attendee : existings)
			delete(attendee);
		}
		
	}
	
	public List<EventAttendee> getByEventAndUser(int eventId, int userId)
	{
		Session session = getSession();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<EventAttendee> query = builder.createQuery(EventAttendee.class);
     // write the Root, Path elements as usual
        Root<EventAttendee> root = query.from(EventAttendee.class);
        query.select(root);  //using metamodel
       
        Predicate userP = builder.equal(root.get("attendee").get("id"), userId);
        Predicate  eventP = builder.equal(root.get("event").get("eventId"), eventId);
        
        query.where( builder.and(userP, eventP)).distinct(true);
        List<EventAttendee> result = session.createQuery(query).getResultList();
        
		return result;
	}

}
