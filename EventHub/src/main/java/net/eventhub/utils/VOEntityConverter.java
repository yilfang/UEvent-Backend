package net.eventhub.utils;



import java.lang.reflect.InvocationTargetException;

//import org.apache.commons.beanutils.BeanUtils;

import org.springframework.beans.BeanUtils;
import net.eventhub.domain.Category;
import net.eventhub.domain.Event;
import net.eventhub.domain.EventAttendee;
import net.eventhub.domain.EventCategory;
import net.eventhub.domain.EventFollower;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.EventUpdate;
import net.eventhub.domain.InvitationStatus;
import net.eventhub.domain.User;
import net.eventhub.domain.UserPushToken;
import net.eventhub.valueobject.EventUserVO;
import net.eventhub.valueobject.EventValueObj;
import net.eventhub.valueobject.UserPushTokenVO;

public class VOEntityConverter {
	
	public static EventInvitee toEventInvitee(EventUserVO eventUser)
	{
		  Event event = new Event(); 
		  event.setEventId(eventUser.getEventId()); 
		  User user = new User(); 
		  user.setId(eventUser.getUserId());
		  InvitationStatus status = new InvitationStatus();
		  if ( eventUser.getStatusId() != null )
		  {
			  status.setId(eventUser.getStatusId());
		  }
		  else
		  {
			  status.setId(EventHubConstants.STATUS_NO_RESPONSE_ID);
		  }		  
		  
		  EventInvitee invitee = new EventInvitee(event, user);
		  invitee.setStatus(status);
		
		return invitee;
		
	}
	
	public static EventFollower toEventFollower(EventUserVO eventUser)
	{
		Event event = new Event();
		event.setEventId(eventUser.getEventId());
		User user = new User();
		user.setId(eventUser.getUserId());
		
		return new EventFollower(event, user);
		
	}
	
	public static EventAttendee toEventAttendee(EventUserVO eventUser)
	{
		Event event = new Event();
		event.setEventId(eventUser.getEventId());
		User user = new User();
		user.setId(eventUser.getUserId());
		
		return new EventAttendee(event, user);
		
	}
	
	public static Event toEvent(EventValueObj eventVO, boolean hasImage) 
	{
		Event event = new Event();
		
		BeanUtils.copyProperties(eventVO, event);
		
		if ( eventVO.getId() != null )
		{
			event.setEventId(eventVO.getId());
		}
		if ( eventVO.getMainCategoryId() != null )
		{
			event.addCategory(toEventCategory(eventVO.getMainCategoryId(), (short)1));	
		}
		if ( eventVO.getCategoryIds() != null && eventVO.getCategoryIds().size() > 0 )
		{
			for(int catId : eventVO.getCategoryIds())
			{
				event.addCategory(toEventCategory(catId, (short)0));
			}
		}
		
		if ( hasImage )
		{
			String imageName = eventVO.getName().split(" ")[0] + "." + eventVO.getImageExt();
			event.setEventImage(imageName);
		}
		
		if ( eventVO.getHostId() != null )
		{
			User user = new User(eventVO.getHostId());
			event.setHost(user);
		}
		
		if ( eventVO.getDisabled() == null )
		{
			event.setDisabled(false);
		}
		
		
		return event;
	}
	
	public static EventCategory toEventCategory(int cagetoryId, short isMain)
	{	
		EventCategory cat = new EventCategory();
		cat.setCategory(new Category(cagetoryId));
		cat.setMain(isMain);
		
		return cat;
	}
	
	public static EventValueObj ToEventValueObj(Event event) 
	{ 
		EventValueObj evo = new EventValueObj();
		BeanUtils.copyProperties(event, evo);
		
		evo.setId(event.getEventId());
		evo.setCategoryFields();
		
		return evo;
	}
	
	public static EventUpdate toEventUpdate(EventUserVO updates)
	{
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setUpdate(updates.getUpdates());
		Event event = new Event();
		event.setEventId(updates.getEventId());
		eventUpdate.setEvent(event);
		
		return eventUpdate;
	}
	
	public static EventUserVO fromEventFollower(EventFollower follower)
	{
		EventUserVO vo = new EventUserVO();
		vo.setEvent(ToEventValueObj(follower.getEvent()));
		vo.setUserFields(follower.getFollower());
		
		return vo;
	}
	
	public static EventUserVO fromUser(User user)
	{
		EventUserVO vo = new EventUserVO();
		
		vo.setUserFields(user);
		
		return vo;
	}
	
	public static EventUserVO fromEventInvitee(EventInvitee invitee)
	{
		EventUserVO vo = new EventUserVO();
		vo.setEvent(ToEventValueObj(invitee.getEvent()));
		vo.setUserFields(invitee.getInvitee());
		vo.setStatus(invitee.getStatus());
		
		return vo;
	}
	
	public static EventUserVO fromEventAttendee(EventAttendee attendee)
	{
		EventUserVO vo = new EventUserVO();
		vo.setEvent(ToEventValueObj(attendee.getEvent()));
		vo.setUserFields(attendee.getAttendee());
		
		return vo;
	}
	
	public static EventUserVO fromEventUpdates(EventUpdate updates)
	{
		EventUserVO vo = new EventUserVO();
		vo.setUpdatesId(updates.getId());
		vo.setEvent(ToEventValueObj(updates.getEvent()));
		vo.setUpdates(updates.getUpdate());
		
		return vo;
	}
	
	public static UserPushToken toUserPushToken(UserPushTokenVO vo)
	{
		UserPushToken token = new UserPushToken();
		token.setId(vo.getId());
		token.setPushToken(vo.getPushToken());
		token.setEnabled(vo.getEnabled());
		User user = new User();
		user.setId(vo.getUserId());
		token.setUser(user);
		
		return token;
	}
	
	public static UserPushTokenVO fromUserPushToken(UserPushToken token)
	{
		UserPushTokenVO vo = new UserPushTokenVO();
		vo.setId(token.getId());
		vo.setPushToken(token.getPushToken());
		vo.setEnabled(token.getEnabled());
		
		vo.setUserId(token.getUser().getId());
		
		return vo;
	}

}
