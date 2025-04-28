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
@Table(name="event_invitee")
public class EventInvitee implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	
	@ManyToOne
	@JoinColumn(name = "event_id",referencedColumnName = "event_id", nullable=false)
	private Event event;
	
	@ManyToOne
	@JoinColumn(name = "invitee_id", nullable = false)
	private User invitee;
	
	@ManyToOne
	@JoinColumn(name = "status", nullable = false)
	private InvitationStatus status;
	
	public EventInvitee() {
		
	}

	public EventInvitee(Event event, User invitee) {
		super();
		this.event = event;
		this.invitee = invitee;
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

	public User getInvitee() {
		return invitee;
	}

	public void setInvitee(User invitee) {
		this.invitee = invitee;
	}

	public InvitationStatus getStatus() {
		return status;
	}

	public void setStatus(InvitationStatus status) {
		this.status = status;
	}	
}
