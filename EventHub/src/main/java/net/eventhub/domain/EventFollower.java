package net.eventhub.domain;

import java.io.Serializable;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="event_follower")
public class EventFollower implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	
	@ManyToOne
	@JoinColumn(name = "event_id",referencedColumnName = "event_id", nullable = false)
	private Event event;
	
	@ManyToOne
	@JoinColumn(name = "follower_id", nullable = false)
	private User follower;
	
	public EventFollower() {
		
	}

	public EventFollower(Event event, User follower) {
		super();
		this.event = event;
		this.follower = follower;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getFollower() {
		return follower;
	}

	public void setFollower(User follower) {
		this.follower = follower;
	}	
	
	
}
