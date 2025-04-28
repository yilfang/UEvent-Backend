package net.eventhub.dao.hibernate;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.eventhub.dao.EventDao;

import net.eventhub.domain.Event;
import net.eventhub.domain.EventAttendee;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.EventUpdate;
import net.eventhub.domain.User;
import net.eventhub.utils.EventHubConstants;
import net.eventhub.utils.EventHubUtils;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.EventUserVO;
import net.eventhub.valueobject.EventValueObj;
import net.eventhub.valueobject.LocationCoordinates;

@Repository("eventDao")
public class EventDaoImpl extends AbstractGenericDao<Event> implements EventDao{

	
	@Override
	public void delete(Event event) {
		deleteEvent(event.getEventId());
	}
	
	@Override
	public void deleteEvent(Integer eventId) {
		Session session = getSession();
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("update event set disabled = 1")
				.append(" where event_id =")
				.append(eventId);
		
		Query query = session.createNativeQuery(sBuilder.toString());
		int result = query.executeUpdate();
	}
	
	
	
	@Override
	public Collection<EventValueObj> getAllActiveEvents(int loginUserId) {
		
		Session session = getSession();
		Set<Event> events = new HashSet();
		
		StringBuilder queryStr = new StringBuilder();
		queryStr.append(getEventSelect());
		queryStr.append(getPrivateEventString(false))
		.append(getPastActiveString(true));	
		List<Event> publicEvents = executeStatement(session, queryStr.toString(), loginUserId);
		events.addAll(publicEvents);
		
		queryStr.setLength(0);
		queryStr.append(getEventSelect());
		queryStr.append(getPrivateEventString(true))
				.append(getPastActiveString(true))
				.append(getEventInviteeString(loginUserId) );
		
		List<Event> priEents = executeStatement(session, queryStr.toString(), loginUserId);	
		events.addAll(priEents);
		
		
		return DetachedEntityConverter.convertTo(events, loginUserId);
		
	}
	
    private String getPrivateEventString(boolean isPrivateEvent)
    {
    	StringBuilder build = new StringBuilder();
    	String privateEvent = isPrivateEvent ? "1" : "0";
    	build.append(" and private_event = ").append(privateEvent);
    	
    	return build.toString();
    }
    
    private String getEventInviteeString(int loginUserId)
    {
    	StringBuilder build = new StringBuilder();
    	
    	build.append(" and (" ).append(loginUserId).append(" in (")
		.append(" select invitee_id from event_invitee ie where e.event_id = ie.event_id)")
    	.append( " or host_id = ")
    	.append(loginUserId).append(")");
    	
    	return build.toString();
    }
    
    private String getEventSelect() {
		StringBuilder eventSelect = new StringBuilder();
		eventSelect.append("select distinct e.* from event e ")
					.append(" inner join event_category ca on e.event_id = ca.event_id ")
					.append(" inner join category c on ca.category_id=c.category_id ")
					.append(" where e.disabled = 0 ");
		/*
		 * eventSelect
		 * .append("select e.event_id as eventId, e.name, host_id as hostId, location, latitude, e.description, end_time as endTime, "
		 * )
		 * .append("location_details as locationDetails, private_event as privateEvent, "
		 * )
		 * .append("registration_link as registrationLink, start_time as startTime, organizer_website as organizerWebsite, "
		 * )
		 * .append("organizer, location_name as locationName, longitude, virtual_event as virtualEvent, c.name as mainCategory, c.category_id as mainCategoryId"
		 * )
		 * .append(" from event e join event_category ca on e.event_id = ca.event_id ")
		 * .append(" join category c on ca.category_id=c.category_id and ca.main=1");
		 */
		
		return eventSelect.toString();
	}
    
    private String getKeywordString(String keyword)
    {
    	String key = keyword.toLowerCase();
		StringBuilder query = new StringBuilder();
		query.append(" and (lower(c.name) like '%").append(key)
				 .append("%' or lower(e.name) like '%").append(key)
				 .append("%' or lower(e.description) like '%").append(key)
				 .append("%' or lower(e.organizer) like '%").append(key)
				 .append("%' or lower(e.location) like '%").append(key)
				 .append("%' )");
		
		return query.toString();
    }
	
	@Override
	public Collection<EventValueObj> searchEventByKeyWord(String keyWord, Boolean active, int loginUserId) {
		Session session = getSession();
		Set<Event> events = new HashSet<>();
		
		String key = keyWord.toLowerCase();
		StringBuilder queryStr = new StringBuilder();
		 queryStr.append(getEventSelect()) 
				 .append(getKeywordString(keyWord))
				 .append(getPastActiveString(active));
		 
		 StringBuilder publicQuery = new StringBuilder();
		 publicQuery.append(queryStr.toString())
		 			.append(getPrivateEventString(false));
		 
		 List<Event> publicResults = executeStatement(session, publicQuery.toString(),loginUserId);
		 events.addAll(publicResults);
		 
		 StringBuilder privateQuery = new StringBuilder();
		 privateQuery.append(queryStr.toString())
		 			.append(getPrivateEventString(true))
		 			.append(getEventInviteeString(loginUserId));
		 
		 List<Event> privateResults = executeStatement(session, privateQuery.toString(), loginUserId);
		 events.addAll(privateResults);
		
		return DetachedEntityConverter.convertTo(events, loginUserId);
	}
	
	private List<EventValueObj> executeStatement(Session session, String queryStr)
	{
		NativeQuery searchQuery =  session.createNativeQuery(queryStr);
		/*
		 * searchQuery.addEntity("e", EventValueObj.class) .addJoin("c", "e.categories")
		 */
		//searchQuery.setProjection(Projections.distinct(Projections.property("event_id")));
		//searchQuery.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		searchQuery.setResultTransformer(Transformers.aliasToBean(EventValueObj.class)); 
		List<EventValueObj> results = searchQuery.list();
		
		return results;
	}
	
	private List<Event> executeStatement(Session session, String queryStr, Integer loginUserId)
	{
		NativeQuery searchQuery =  session.createNativeQuery(queryStr).addEntity(Event.class);
		/*
		 * searchQuery.addEntity("e", EventValueObj.class) .addJoin("c", "e.categories")
		 */
		//searchQuery.setProjection(Projections.distinct(Projections.property("event_id")));
		//searchQuery.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		//searchQuery.setResultTransformer(Transformers.aliasToBean(Event.class)); 
		List<Event> results = searchQuery.getResultList();
		
		return results;
	}

	@Override
	public Collection<EventValueObj> searchEventByCategories(List categoryIds, Boolean active, int loginUserId) {
		Session session = getSession();
		Set<Event> events = new HashSet<>();
		
		StringBuilder queryStr = new StringBuilder();
		queryStr.append(getEventSelect())
				 .append(getCategoryString(categoryIds))
				 .append(getPrivateEventString(false))
				 .append(getPastActiveString(active));
		
		 List<Event> publicResults = executeStatement(session, queryStr.toString(), loginUserId);
		 events.addAll(publicResults);
		 
		 queryStr.setLength(0);
		 
		 queryStr.append(getEventSelect())
		 .append(getCategoryString(categoryIds))
		 .append(getPrivateEventString(true))
		 .append(getPastActiveString(active))
		 .append(getEventInviteeString(loginUserId));

		 List<Event> privateResults = executeStatement(session, queryStr.toString(), loginUserId);
		 events.addAll(privateResults);
		
		return DetachedEntityConverter.convertTo(events, loginUserId);
	}
	
	private String getCategoryString(List<Integer> categoryIds)
	{
		StringBuilder categoryStr = new StringBuilder();
		
		categoryStr.append(" and c.category_id in (")
		 .append(StringUtils.collectionToCommaDelimitedString(categoryIds))
		 .append(")");
		
		return categoryStr.toString();
		
	}
	
	
	
	private String getPastActiveString(Boolean active)
	{
		StringBuilder pastActive = new StringBuilder();
		
		if ( active != null )
		{
			String current = EventHubUtils.getCurrentTimeString();
			if ( active )
			{			
				pastActive.append(" and ").append(" end_time > '").append(current).append("'");
			}
			else
			{
				pastActive.append(" and '").append(current).append("' > end_time ");
			}
		}	
		
		return pastActive.toString();
	}
	
	
	
	private String getTimeRange(Date startTime, Date endTime) {
		StringBuilder timeRange = new StringBuilder();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if ( startTime != null && endTime != null )
		{
			String start = format.format(startTime);
			String end = format.format(endTime);
			
			timeRange.append(" and end_time >= '")
					 .append(start).append("' and ")
					 .append("start_time <= '")
					 .append(end).append("' ");					 
		}
		else if ( startTime != null ) 
		{
			String start = format.format(startTime);
			
			timeRange.append(" and end_time >= '")
					 .append(start).append("'");
		}
		else 
		{
			String end = format.format(endTime);
			
			timeRange.append("and end_time <= '")
					 .append(end).append("'");
		}
		
		return timeRange.toString();
	}

	@Override
	public Collection<EventValueObj> searchEventByTimeRange(Date startTime, Date endTime, int loginUserId) {
		Session session = getSession();
		Set<Event> events = new HashSet();
		
		StringBuilder queryStr = new StringBuilder();
		 queryStr.append(getEventSelect())
				 .append(getTimeRange(startTime, endTime))
				 .append(getPrivateEventString(false))
		 		 .append(getPastActiveString(true));
		
		 List<Event> publicResults = executeStatement(session, queryStr.toString(), loginUserId);
		 events.addAll(publicResults); 
		 
		 queryStr.setLength(0);
		 queryStr.append(getEventSelect())
		 .append(getTimeRange(startTime, endTime))
		 .append(getPastActiveString(true))
		 .append(getPrivateEventString(true))
		 .append(getEventInviteeString(loginUserId));
		 
		 List<Event> privateResults = executeStatement(session, queryStr.toString(), loginUserId);
		 events.addAll(privateResults);
		
		return DetachedEntityConverter.convertTo(events, loginUserId);
	}

	@Override
	public Collection<EventValueObj> searchEventByCategoryAndTimeRange(Date startTime, Date endTime, 
									List<Integer> categoriesIds, int loginUserId) {
		
		Session session = getSession();
		Set<Event> events = new HashSet();
		
		StringBuilder queryStr = new StringBuilder();
		 queryStr.append(getEventSelect())
				 .append(getCategoryString(categoriesIds))
				 .append(getTimeRange(startTime, endTime))
				 .append(getPastActiveString(true))
				 .append(getPrivateEventString(false));
		
		 List<Event> publicResults = executeStatement(session, queryStr.toString(), loginUserId);
		 
		 events.addAll(publicResults); 
		 
		 queryStr.setLength(0);
		 queryStr.append(getEventSelect())
		 .append(getCategoryString(categoriesIds))
		 .append(getTimeRange(startTime, endTime))
		 .append(getPrivateEventString(true))
		 .append(getPastActiveString(true))
		 .append(getEventInviteeString(loginUserId));
		 
		 List<Event> privateResults = executeStatement(session, queryStr.toString(), loginUserId);
		 events.addAll(privateResults);
		
		return DetachedEntityConverter.convertTo(events, loginUserId);
	}

	@Override
	public LocationCoordinates getCoordinates(List<LocationCoordinates> coordinates) {
		Session session = getSession();
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("Select count(*) from event where ")
			    .append("SQRT(power((:longitude - longitude), 2 ) + power( (:latitude - latitude), 2)) < :coors")
			    .append(" and end_time > :now"); 
		
		Query query = session.createNativeQuery(sBuilder.toString()); 
	
        for( LocationCoordinates cd : coordinates)
        {
        	
        	query.setParameter("longitude", cd.getLongitude());
        	query.setParameter("latitude", cd.getLatitude());
        	query.setParameter("coors", EventHubConstants.COORDINATES_LIMIT);
        	query.setParameter("now", new Timestamp(System.currentTimeMillis()));
        	int result = ((BigInteger)query.uniqueResult()).intValue();
        	if ( result == 0 )
        	{
        		return cd;
        	}
        }
        
        
		return null;
	}

	@Override
	public Collection<EventValueObj> getEventsByHostId(int hostId) {
		Session session = getSession();
		Set<EventValueObj> events = new HashSet<>();
		
		StringBuilder queryStr = new StringBuilder();
		queryStr.append(getEventSelect())
				 .append(" and host_id = ").append(hostId);
				 
		 List<Event> results = executeStatement(session, queryStr.toString(), hostId);
		 
		 return DetachedEntityConverter.convertTo(results, hostId);
	}
	
	
	

	@Override
	public void insertImage(int eventId, String imageName) {
		Session session = getSession();
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("update event set event_image ='")
				.append(imageName)
				.append("' where event_id =")
				.append(eventId);
		
		Query query = session.createNativeQuery(sBuilder.toString());
		int result = query.executeUpdate();
	}

	@Override
	public Collection<EventValueObj> searchEvents(Date startTime, Date endTime, 
											List<Integer> categoriesIds, String keyword, int loginUserId) {
		Session session = getSession();
		StringBuilder queryStr = new StringBuilder();
		queryStr.append(getEventSelect())
				.append(getPastActiveString(true));
		
		if (StringUtils.hasLength(keyword))
		{
			queryStr.append(getKeywordString(keyword)); 
		}
		
		if ( categoriesIds != null && categoriesIds.size() > 0 )
		{
			queryStr.append(getCategoryString(categoriesIds));
		}
		
		if ( startTime != null || endTime != null )
		{
			queryStr.append(getTimeRange(startTime, endTime));
		}
		
		
		StringBuilder statement = new StringBuilder();
		statement.append(queryStr.toString())
				 .append(getPrivateEventString(false));
		//all public events
		 List<Event> results = executeStatement(session, statement.toString(), loginUserId);
		 
		 statement.setLength(0);
		 statement.append(queryStr.toString())
		 		  .append(getPrivateEventString(true))
		 		  .append(getEventInviteeString(loginUserId));
		 
		 //add all the private events the login user is in the invitee list
		 results.addAll(executeStatement(session, statement.toString(), loginUserId));
		
		return DetachedEntityConverter.convertTo(results, loginUserId);
	}

	@Override
	public void updateHostNotis(int eventId, Boolean hostNotis) {
		Session session = getSession();
		
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("update event set hostNotis ='")
				.append(hostNotis ? 1 : 0)
				.append("' where event_id =")
				.append(eventId);
		
		Query query = session.createNativeQuery(sBuilder.toString());
		int result = query.executeUpdate();
		
	}

}
