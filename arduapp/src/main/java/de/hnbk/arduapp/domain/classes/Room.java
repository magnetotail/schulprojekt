package de.hnbk.arduapp.domain.classes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Raum")
public class Room implements SimpleDaoInterface, Describable{

	@Id
	@Column(name = "RaumID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	int RoomId;

	@Column(length = 50, nullable = false)
	String description;

	@ManyToOne(targetEntity = Location.class)
	@JoinColumn(name = "LocationID", nullable = false)
	Location location;
	
	@Override
	public String toString() {
		return "ID: " + RoomId + " Name: " + description;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getRoomId() {
		return RoomId;
	}

	public void setRoomId(int roomId) {
		RoomId = roomId;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String getDescriptionCheckRegex() {
		return ".*";
	}

	@Override
	public String getDescriptionLabelText() {
		return "Raum";
	}

}
