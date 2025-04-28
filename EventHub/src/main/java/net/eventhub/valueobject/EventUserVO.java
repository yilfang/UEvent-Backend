package net.eventhub.valueobject;

import net.eventhub.domain.InvitationStatus;
import net.eventhub.domain.User;

public class EventUserVO {
	
	private Integer userId;
	private String userName;
	private String userEmail;
	private Integer eventId;
	private EventValueObj event;
	private Integer updatesId;
	private String updates;
	private InvitationStatus status;
	private Integer statusId;
	
	
	public EventUserVO()
	{
		
	}
	
	public EventUserVO(String updates, Integer eventId) {
		this.eventId = eventId;
		this.updates = updates;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public void setUserFields(User user)
	{
		setUserId(user.getId());
		setUserName(user.getDisplayName());
		setUserEmail(user.getEmail());
	}

	public EventValueObj getEvent() {
		return event;
	}

	public void setEvent(EventValueObj event) {
		this.event = event;
	}

	public String getUpdates() {
		return updates;
	}
	public void setUpdates(String updates) {
		this.updates = updates;
	}


	public InvitationStatus getStatus() {
		return status;
	}

	public void setStatus(InvitationStatus status) {
		this.status = status;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getUpdatesId() {
		return updatesId;
	}

	public void setUpdatesId(Integer updatesId) {
		this.updatesId = updatesId;
	}
	
}
