package net.eventhub.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="user_push_token")
public class UserPushToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="push_token", length = 1024, nullable = false, unique = true)
	private String pushToken;
	
	@Column(name="enabled", nullable = false, columnDefinition = "boolean default true")
	private Boolean enabled;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	

}
