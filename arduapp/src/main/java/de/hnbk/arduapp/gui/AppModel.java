package de.hnbk.arduapp.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hnbk.arduapp.domain.classes.Client;
import de.hnbk.arduapp.domain.classes.Measurement;

public class AppModel {

	
	private static AppModel INSTANCE;
	private Client client;
	private List<Measurement> measurements;
	
	static {
		INSTANCE = new AppModel();
	}
	
	private AppModel() {
		measurements = Collections.synchronizedList(new ArrayList<>());
	}

	public synchronized Client getClient() {
		return client;
	}

	public List<Measurement> getMeasurements() {
		return measurements;
	}

	public static AppModel getInstance() {
		return INSTANCE;
	}

	public synchronized void setClient(Client client) {
		this.client = client;
	};
	

}
