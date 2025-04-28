package net.eventhub.valueobject;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.json.bind.annotation.JsonbDateFormat;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.eventhub.domain.EventCategory;
import net.eventhub.domain.User;
import net.eventhub.utils.EventHubConstants;

@Configurable
public class EventValueObj {
	
	private Integer id;
		
	private String name;
	
	private Integer hostId;
	
	private User host;
	
	private String location;
	
	private String locationDetails;
	
	private String registrationLink;
	
	private BigDecimal longitude;
	
	private BigDecimal latitude;
	
	private String description;
	
	private String eventImage;
	
	private String organizer;	
	
	private String organizerWebsite;
	
	private String organizerEmail;
	
	private Boolean privateEvent;
	
	private Boolean virtualEvent;
	
	private String mainCategory;
	
	private Integer mainCategoryId;
	
	private String otherCategories;
	
	private String otherCategoryIds;
	
	private List<Integer> categoryIds;
	
	private Set<EventCategory>categories;
	
	private List<LocationCoordinates> coordinates;
	
	private Boolean isFollower;
	private Boolean isInvitee;
	private Boolean isAttendee;
	private Boolean disabled;
	private Boolean hostNotis;
	
	@JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss")//2022-04-28 14:45:15
	private Date startTime;
	
	@JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss")//2022-04-28 14:45:15
	private Date endTime;
	
	
	private String imageData;
	private String imageExt;
	private Boolean isImageDeleted;
	
	private Integer numOfFollower;
	private Integer numOfAttendee;
	private Integer numOfInvitee;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getHostId() {
		return hostId;
	}

	public void setHostId(Integer hostId) {
		this.hostId = hostId;
	}

	public User getHost() {
		return host;
	}

	public void setHost(User host) {
		this.host = host;
	}

	
	public String getOrganizerEmail() {
		return organizerEmail;
	}

	public void setOrganizerEmail(String organizerEmail) {
		this.organizerEmail = organizerEmail;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocationDetails() {
		return locationDetails;
	}

	public void setLocationDetails(String locationDetails) {
		this.locationDetails = locationDetails;
	}

	public String getRegistrationLink() {
		return registrationLink;
	}

	public void setRegistrationLink(String regLink) {
		this.registrationLink = regLink;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public String getOrganizerWebsite() {
		return organizerWebsite;
	}

	public void setOrganizerWebsite(String orgWebSite) {
		this.organizerWebsite = orgWebSite;
	}
	
	public String getEventImagePath()
	{
		if ( getEventImage() == null )
		{
			return null;
		}
		
		StringBuilder path = new StringBuilder();
		path.append(EventHubConstants.IMAGE_APP)
			.append(File.separator)
			.append(getId())
			.append(File.separator)
			.append(getEventImage());
			
		return  path.toString();
	}

	public String getEventImage() {
		return eventImage;
	}

	public void setEventImage(String eventImage) {
		this.eventImage = eventImage;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Boolean getPrivateEvent() {
		return privateEvent;
	}

	public void setPrivateEvent(Boolean privateEvent) {
		this.privateEvent = privateEvent;
	}

	public Boolean getVirtualEvent() {
		return virtualEvent;
	}

	public void setVirtualEvent(Boolean virtualEvent) {
		this.virtualEvent = virtualEvent;
	}

	public String getMainCategory() {
		return mainCategory;
	}

	public void setMainCategory(String mainCategory) {
		this.mainCategory = mainCategory;
	}

	public List<Integer> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public Integer getMainCategoryId() {
		return mainCategoryId;
	}

	public void setMainCategoryId(Integer mainCategoryId) {
		this.mainCategoryId = mainCategoryId;
	}

	public Set<EventCategory> getCategories() {
		return categories;
	}

	public void setCategories(Set<EventCategory> categories) {
		this.categories = categories;
	}

	public List<LocationCoordinates> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<LocationCoordinates> cordinates) {
		this.coordinates = cordinates;
	}

	public String getOtherCategories() {
		return otherCategories;
	}

	public void setOtherCategories(String otherCategories) {
		this.otherCategories = otherCategories;
	}

	public String getOtherCategoryIds() {
		return otherCategoryIds;
	}

	public void setOtherCategoryIds(String otherCategoryIds) {
		this.otherCategoryIds = otherCategoryIds;
	}
	
	
	
	public Boolean getIsFollower() {
		return isFollower;
	}

	public void setIsFollower(Boolean isFollower) {
		this.isFollower = isFollower;
	}

	public Boolean getIsInvitee() {
		return isInvitee;
	}

	public void setIsInvitee(Boolean isInvitee) {
		this.isInvitee = isInvitee;
	}

	public Boolean getIsAttendee() {
		return isAttendee;
	}

	public void setIsAttendee(Boolean isAttendee) {
		this.isAttendee = isAttendee;
	}
	

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getHostNotis() {
		return hostNotis;
	}

	public void setHostNotis(Boolean hostNotis) {
		this.hostNotis = hostNotis;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

	public String getImageExt() {
		return imageExt;
	}

	public void setImageExt(String imageExt) {
		this.imageExt = imageExt;
	}
	
	public Boolean getIsImageDeleted() {
		return isImageDeleted;
	}

	public void setIsImageDeleted(Boolean isImageDeleted) {
		this.isImageDeleted = isImageDeleted;
	}

	public Integer getNumOfFollower() {
		return numOfFollower;
	}

	public void setNumOfFollower(Integer numOfFollower) {
		this.numOfFollower = numOfFollower;
	}

	public Integer getNumOfAttendee() {
		return numOfAttendee;
	}

	public void setNumOfAttendee(Integer numOfAttendee) {
		this.numOfAttendee = numOfAttendee;
	}

	public Integer getNumOfInvitee() {
		return numOfInvitee;
	}

	public void setNumOfInvitee(Integer numOfInvitee) {
		this.numOfInvitee = numOfInvitee;
	}

	public void setCategoryFields()
	{
		if ( getCategories() != null && getCategories().size() > 0)
		{
			StringBuilder otherCat = new StringBuilder();
			StringBuilder otherCatIds = new StringBuilder();
			for(EventCategory cat : getCategories())
			{
				if ( cat.isMain() == 1)
				{
					setMainCategory(cat.getCategory().getName());
					setMainCategoryId(cat.getCategory().getId());
				}
				else
				{
					otherCat.append(cat.getCategory().getName()).append(",");
					otherCatIds.append(cat.getCategory().getId()).append(",");
				}
			}
			if ( otherCat.length() > 0 ) 
			{
				otherCat.deleteCharAt(otherCat.length()-1);
				otherCatIds.deleteCharAt(otherCatIds.length()-1);
			}
			
			
			setOtherCategories(otherCat.toString());
			setOtherCategoryIds(otherCatIds.toString());
			
			setCategories(null);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventValueObj other = (EventValueObj) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	

}
