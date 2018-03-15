package de.hnbk.arduapp.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hnbk.arduapp.domain.classes.Client;
import de.hnbk.arduapp.domain.classes.Measurement;
import gnu.io.CommPortIdentifier;

public class AppModel {

	
	private static AppModel INSTANCE;
	private Client client;
	private List<Measurement> measurements;
	private CommPortIdentifier comPort;
	
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
	}

	public synchronized CommPortIdentifier getComPort() {
		return comPort;
	};
	
	public synchronized void setComPort(CommPortIdentifier comPort) {
		this.comPort = comPort;
	}

}
