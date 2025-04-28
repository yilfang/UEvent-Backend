package net.eventhub.dao.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.eventhub.domain.Event;
import net.eventhub.domain.UserPushToken;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.EventValueObj;
import net.eventhub.valueobject.UserPushTokenVO;

public class DetachedEntityConverter {
	
	public static Collection<EventValueObj> convertTo(Collection<Event> events, Integer loginUserId)
	{
		List<EventValueObj> etObjs = new ArrayList<>();
		
		for(Event e : events)
		{
			EventValueObj et = VOEntityConverter.ToEventValueObj(e);
			et.setIsAttendee(e.isAttendee(loginUserId));
			et.setIsFollower(e.isFollower(loginUserId));
			et.setIsInvitee(e.isInvitee(loginUserId));
			et.setNumOfAttendee(e.getAttendee().size());
			et.setNumOfFollower(e.getFollowers().size());
			et.setNumOfInvitee(e.getInvitees().size());
			
			etObjs.add(et);
		}
		
		return etObjs;
	}
	
	public static Collection<UserPushTokenVO> convertTo(Collection<UserPushToken> tokens)
	{
		List<UserPushTokenVO> voList = new ArrayList<>();
		
		for(UserPushToken t : tokens)
		{
			voList.add(VOEntityConverter.fromUserPushToken(t));
		}
		
		return voList;
	}

}
