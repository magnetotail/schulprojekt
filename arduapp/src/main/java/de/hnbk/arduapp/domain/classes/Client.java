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
public class Client implements Serializable{
	
	private static final long serialVersionUID = 5221781840460585049L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	int ClientID;
	
	@Column(nullable = false, length = 17, unique = true)
	String ClientMAC;
	
	@Column(length = 255, name = "ClientBeschreibung")
	String ClientDescription;

	@ManyToOne(targetEntity = Room.class)
	@JoinColumn(name = "RaumID", nullable = false)
	Room room;
	
	@ManyToOne(targetEntity = MeasurementType.class)
	@JoinColumn(name = "MessartID", nullable = false)
	MeasurementType measurementType;
	
	
	@Override
	public String toString() {
		return "ID: " + ClientID + " MAC: " + ClientMAC + " Beschreibung: " + ClientDescription + " Raum: " + room + " Messart: " + measurementType;
	}

	public int getClientID() {
		return ClientID;
	}

	public void setClientID(int clientID) {
		ClientID = clientID;
	}

	public String getClientMAC() {
		return ClientMAC;
	}

	public void setClientMAC(String clientMAC) {
		ClientMAC = clientMAC;
	}

	public String getClientDescription() {
		return ClientDescription;
	}

	public void setClientDescription(String clientDescription) {
		ClientDescription = clientDescription;
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
	
}
