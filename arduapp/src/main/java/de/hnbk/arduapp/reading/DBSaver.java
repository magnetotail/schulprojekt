package de.hnbk.arduapp.reading;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.ConfigurableApplicationContext;

import de.hnbk.arduapp.domain.classes.Measurement;
import de.hnbk.arduapp.domain.repositories.MeasurementRepository;
import de.hnbk.arduapp.gui.AppModel;

public class DBSaver implements Runnable {

	private ConfigurableApplicationContext context;

	private static Logger logger = Logger.getLogger(DBSaver.class.getName());

	public DBSaver(ConfigurableApplicationContext context) {
		this.context = context;
	}

	@Override
	public void run() {
		try {
			logger.log(Level.INFO, "Database save thread started..");
			if (AppModel.getInstance().getClient() == null) {
				logger.log(Level.INFO, "No client set in model. Exiting saving to database.");
				return;
			}
			MeasurementRepository measurementRepository = context.getBean(MeasurementRepository.class);
			List<Measurement> measurements = AppModel.getInstance().getMeasurements();
			List<Measurement> copiedMeasurements = new ArrayList<>(measurements); 
			copiedMeasurements.forEach(DBSaver::setMeasurementClientIfNull);
			logger.log(Level.INFO, "Starting save...");
			measurementRepository.save(copiedMeasurements);
			logger.log(Level.INFO, "Saved " + copiedMeasurements.size() + " measurements.");
			logger.log(Level.INFO, "Clearing measurements...");
			measurements.removeAll(copiedMeasurements);
			logger.log(Level.INFO, "Saving successful!");
		}catch(Exception e) {
			logger.log(Level.SEVERE,"Fehler", e);
		}
	}

	private static void setMeasurementClientIfNull(Measurement measurement) {
		if (measurement.getClient() == null) {
			measurement.setClient(AppModel.getInstance().getClient());
		}
	}
}
