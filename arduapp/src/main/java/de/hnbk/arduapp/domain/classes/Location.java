package de.hnbk.arduapp.domain.classes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Standort")
	
public class Location implements SimpleDaoInterface, Describable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int locationID;
	
	@Column(name = "Bezeichnung", nullable = false, unique = true)
	String description;

	public int getLocationID() {
		return locationID;
	}

	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getDescriptionCheckRegex() {
		return ".*";
	}

	@Override
	public String getDescriptionLabelText() {
		return "Standort";
	}

}
