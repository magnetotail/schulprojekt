package de.hnbk.arduapp.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hnbk.arduapp.domain.classes.Client;
import de.hnbk.arduapp.domain.classes.Measurement;
import de.hnbk.arduapp.domain.classes.Room;

public class AppModel {
	
	private Room room;
	private Client client;
	private List<Measurement> measurements;
	
	public AppModel(Room room, Client client) {
		this.room = room;
		this.client = client;
		measurements = Collections.synchronizedList(new ArrayList<>());
	}
	
	public AppModel() {
		this (null, null);
	}

	public Room getRoom() {
		return room;
	}

	public Client getClient() {
		return client;
	}

	public List<Measurement> getMeasurements() {
		return measurements;
	};
	

}
