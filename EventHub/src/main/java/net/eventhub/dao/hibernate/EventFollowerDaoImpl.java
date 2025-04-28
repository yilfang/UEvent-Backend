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


import net.eventhub.dao.EventFollowerDao;
import net.eventhub.domain.Event;
import net.eventhub.domain.EventFollower;
import net.eventhub.domain.EventInvitee;
import net.eventhub.utils.EventHubConstants;
import net.eventhub.valueobject.EventValueObj;


@Repository("eventFollowerDao")
public class EventFollowerDaoImpl extends AbstractGenericDao<EventFollower> implements EventFollowerDao{

	@Override
	public Collection<EventValueObj> getFollowingEvents(int userId) {
		Session session = getSession();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
     // write the Root, Path elements as usual
        Root<EventFollower> root = query.from(EventFollower.class);
        query.select(root.get("event"));  //using metamodel
        Predicate userP = builder.equal(root.get("follower").get("id"), userId);
        Predicate eventP = 
        		builder.and(builder.equal(root.get("event").get("disabled"), 0),
        					builder.greaterThan(root.get("event").get("endTime"), builder.currentTimestamp()));
        query.where(builder.and(userP, eventP)).distinct(true);
        List<Event> result = session.createQuery(query).getResultList();
        
		return DetachedEntityConverter.convertTo(result, userId);
	}

	@Override
	public void addFollowingEvents(List<EventFollower> followingEvents) {
		int i = 0;
		for(EventFollower followingEvent : followingEvents)
		{
			i++;
			save(followingEvent);
			if ( i % EventHubConstants.batch_size == 0)
			{
				getSession().flush();
				getSession().clear();
			}
		}
		
	}
	
	@Override
	public Serializable save(EventFollower follower)
	{
		Serializable id = null;
		List<EventFollower> existings = 
				getByEventAndUser(follower.getEvent().getEventId(), follower.getFollower().getId());
		
		if ( existings == null || existings.size() == 0)
		{
			id = super.save(follower);
		}
		else
		{
			id = existings.get(0).getId();
		}
		
		return id;
	}

	@Override
	public void deleteFollower(int eventId, int userId) {
		List<EventFollower> existings = getByEventAndUser(eventId, userId);
		
		if ( existings != null || existings.size() > 0)
		{
			for(EventFollower follower : existings)
			delete(follower);
		}
	}
	
	public List<EventFollower> getByEventAndUser(int eventId, int userId)
	{
		Session session = getSession();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
    
        CriteriaQuery<EventFollower> query = builder.createQuery(EventFollower.class);
     // write the Root, Path elements as usual
        Root<EventFollower> root = query.from(EventFollower.class);
        query.select(root);  //using metamodel
        Join<Object, Object> userJoin = root.join("follower");
        Predicate userP = builder.equal(userJoin.get("id"), userId);
        
        Join<Object, Object> evnetJoin = root.join("event");
        Predicate  eventP = builder.equal(evnetJoin.get("eventId"), eventId);
        
        query.where( builder.and(userP, eventP)).distinct(true);
        List<EventFollower> result = session.createQuery(query).getResultList();
        
		return result;
	}

}
