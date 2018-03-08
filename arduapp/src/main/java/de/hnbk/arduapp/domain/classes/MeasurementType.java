package de.hnbk.arduapp.domain.classes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Messart")
public class MeasurementType implements SimpleDaoInterface, Describable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MessartID")
	int id;

	@Column(name = "Bezeichnung")
	String description;

	@Override
	public String toString() {
		return description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getDescriptionCheckRegex() {
		return ".*";
	}
	
	@Override
	public String getDescriptionLabelText() {
		return "Messart";
	}

}
