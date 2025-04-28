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
@Table(name="event_attendee")
public class EventAttendee implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	
	@ManyToOne
	@JoinColumn(name = "event_id",referencedColumnName = "event_id", nullable = false)
	private Event event;
	
	@ManyToOne
	@JoinColumn(name = "attendee_id", nullable = false)
	private User attendee;
	
	public EventAttendee() {
		
	}

	public EventAttendee(Event event, User attendee) {
		super();
		this.event = event;
		this.attendee = attendee;
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

	public User getAttendee() {
		return attendee;
	}

	public void setAttendee(User attendee) {
		this.attendee = attendee;
	}
	
	public Integer getUserId()
	{
		return getAttendee().getId();
	}
	
	
}
