package de.hnbk.arduapp.domain.classes;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Client")
public class Client implements SimpleDaoInterface, Describable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int clientID;

	@Column(nullable = false, length = 17, unique = true)
	String clientMac;

	@Column(length = 255, name = "ClientBeschreibung")
	String description;

	@ManyToOne(targetEntity = Room.class)
	@JoinColumn(name = "RaumID", nullable = false)
	Room room;

	@ManyToOne(targetEntity = MeasurementType.class)
	@JoinColumn(name = "MessartID", nullable = false)
	MeasurementType measurementType;

	@Override
	public String toString() {
		return "ID: " + clientID + " MAC: " + clientMac + " Beschreibung: " + description + " Raum: " + room
				+ " Messart: " + measurementType;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public String getClientMac() {
		return clientMac;
	}

	public void setClientMac(String clientMAC) {
		this.clientMac = clientMAC;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public MeasurementType getMeasurementType() {
		return measurementType;
	}

	public void setMeasurementType(MeasurementType measurementType) {
		this.measurementType = measurementType;
	}

	@Override
	public String getDescriptionCheckRegex() {
		return ".*";
	}

	@Override
	public String getDescriptionLabelText() {
		return "Client";
	}
}
