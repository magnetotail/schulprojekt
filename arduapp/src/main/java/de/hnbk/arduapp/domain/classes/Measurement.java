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
public class Measurement  implements SimpleDaoInterface{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;

	@Column(nullable = false)
	Timestamp measurementTime;

	@Column(nullable = false)
	double value;
	
	@ManyToOne(targetEntity = Client.class)
	@JoinColumn(name = "ClientID", nullable = false)
	Client client;
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getMeasurementTime() {
		return measurementTime;
	}

	public void setMeasurementTime(Timestamp measurementTime) {
		this.measurementTime = measurementTime;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Wert: " + value + " Messart: " + client.getMeasurementType();
	}

}
