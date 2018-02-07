package de.hnbk.arduapp.domain.classes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Messart")
public class MeasurementType {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MessartID")
	int MeasurementTypeID;

	@Column(name = "Bezeichnung")
	String description;
	
	@Override
	public String toString() {
		return "ID: " + MeasurementTypeID + " Bezeichnung: " + description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMeasurementTypeID() {
		return MeasurementTypeID;
	}

	public void setMeasurementTypeID(int measurementTypeID) {
		MeasurementTypeID = measurementTypeID;
	}

}
