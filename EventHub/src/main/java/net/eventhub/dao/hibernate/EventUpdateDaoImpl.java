package net.eventhub.dao.hibernate;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import net.eventhub.dao.CategoryDao;
import net.eventhub.dao.EventAttendeeDao;
import net.eventhub.dao.EventDao;
import net.eventhub.dao.EventUpdateDao;
import net.eventhub.domain.Category;
import net.eventhub.domain.Event;
import net.eventhub.domain.EventAttendee;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.EventUpdate;
import net.eventhub.domain.User;
import net.eventhub.utils.EventHubConstants;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.EventUserVO;
import net.eventhub.valueobject.EventValueObj;

@Repository("eventUpdateDao")
public class EventUpdateDaoImpl extends AbstractGenericDao<EventUpdate> implements EventUpdateDao{

	@Override
	public List<String> getEventUpdates(int eventId) {
		Session session = getSession();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<String> query = builder.createQuery(String.class);
     // write the Root, Path elements as usual
        Root<EventUpdate> root = query.from(EventUpdate.class);
        Join<Object, Object> eventJoin = root.join("event");
        query.select(root.get("update")).where(builder.equal(eventJoin.get("eventId"), eventId));
        List<String> result = session.createQuery(query).getResultList();
        
        
        
		return result;
	}

	
	@Override
	public List<EventUserVO> getAllEvewntUpdates() {
		
		Session session = getSession();
			
		CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<EventUserVO> query = builder.createQuery(EventUserVO.class);
     // write the Root, Path elements as usual
        Root<EventUpdate> root = query.from(EventUpdate.class);
       
        query.multiselect(root.get("update"), root.get("event").get("eventId"));
        List<EventUserVO> result = session.createQuery(query).getResultList();
        
		return result;
	}


	@Override
	public Collection<EventUserVO> getUpdatesByUserId(int userId) {
		Session session = getSession();
		
		  StringBuilder qStr = new StringBuilder(); 
		  qStr.append("select distinct eu.*")
		  	  .append(" from event_update eu inner join event e on eu.event_id = e.event_id")
		  	  .append(" inner join event_attendee ea on eu.event_id = ea.event_id")
		  	  .append(" where e.end_time > current_timestamp() ")
		  	  .append(" and ea.attendee_id = " ).append(userId);
		  
		  NativeQuery searchQuery = session.createNativeQuery(qStr.toString()).addEntity(EventUpdate.class);
		  //searchQuery.setResultTransformer(Transformers.aliasToBean();
		  
		  List<EventUpdate> searches = searchQuery.getResultList();
		  Set <EventUserVO> result = new HashSet<>();
		  for(EventUpdate u : searches)
		  {
			  EventUserVO vo = VOEntityConverter.fromEventUpdates(u);
			  
			  Event e = u.getEvent();
			  EventValueObj eObj = vo.getEvent(); 
			  eObj.setIsAttendee(e.isAttendee(userId));
			  eObj.setIsFollower(e.isFollower(userId));
			  eObj.setIsInvitee(e.isInvitee(userId));
			  vo.setEvent(eObj);
			  
			  result.add(vo);
		  }
		 
        
		return result;
	}
}
