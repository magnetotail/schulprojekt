package de.hnbk.arduapp.domain.classes;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Messung")
public class Measurement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int MeasurementID;

	@Column(nullable = false)
	Timestamp MeasurementTime;
	
	
	@ManyToOne(targetEntity = Client.class)
	@JoinColumn(name = "ClientID", nullable = false)
	Client client;
	
	public int getMeasurementID() {
		return MeasurementID;
	}

	public void setMeasurementID(int measurementID) {
		MeasurementID = measurementID;
	}

	public Timestamp getMeasurementTime() {
		return MeasurementTime;
	}

	public void setMeasurementTime(Timestamp measurementTime) {
		MeasurementTime = measurementTime;
	}

}
