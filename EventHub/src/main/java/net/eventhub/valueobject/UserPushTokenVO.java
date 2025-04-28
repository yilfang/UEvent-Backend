package net.eventhub.valueobject;

public class UserPushTokenVO {
	
	private Integer id;
	private String pushToken;
	private Boolean enabled;
	private Integer UserId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPushToken() {
		return pushToken;
	}
	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Integer getUserId() {
		return UserId;
	}
	public void setUserId(Integer userId) {
		UserId = userId;
	}
	
	

}
