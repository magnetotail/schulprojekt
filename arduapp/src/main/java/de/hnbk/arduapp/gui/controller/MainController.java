package de.hnbk.arduapp.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import de.hnbk.arduapp.AddressResolver;
import de.hnbk.arduapp.AddressResolver.AddressType;
import de.hnbk.arduapp.ArduApplication;
import de.hnbk.arduapp.domain.classes.Client;
import de.hnbk.arduapp.domain.classes.Location;
import de.hnbk.arduapp.domain.classes.MeasurementType;
import de.hnbk.arduapp.domain.classes.Room;
import de.hnbk.arduapp.domain.repositories.ClientRepository;
import de.hnbk.arduapp.domain.repositories.LocationRepository;
import de.hnbk.arduapp.domain.repositories.MeasurementTypeRepository;
import de.hnbk.arduapp.domain.repositories.RoomRepository;
import de.hnbk.arduapp.gui.AppModel;
import de.hnbk.arduapp.gui.view.ClientInfoInputDialog;
import de.hnbk.arduapp.gui.view.MainFrame;

@Controller
public class MainController {

	private static Logger logger = Logger.getLogger(MainController.class.getName());

	@Autowired
	private RoomRepository roomRepo;

	@Autowired
	private ClientRepository clientRepo;

	@Autowired
	private MeasurementTypeRepository measurementTypeRepo;
	
	@Autowired
	private LocationRepository locationRepo;

	@Autowired
	private ConfigurableApplicationContext context;

	private AppModel model;

	private MainFrame frame;

	
	public MainController() {
		frame = new MainFrame();
		model = new AppModel();
	}

	@PostConstruct
	private void init() {
		try {
			initRepos();
			initApplication();
			initFrame();
			System.out.println(context.getBean(RoomRepository.class).count());
			frame.setVisible(true);
			ArduApplication.closeSplashscreen();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
			ArduApplication.closeSplashscreen();
		}
	}

	private void initApplication() throws IOException {
		String macAddress = AddressResolver.getAddress(AddressType.MAC);
		if (macAddress == null) {
			throw new IOException("No MAC-Address found. Make sure the computer is connected to a Network.");
		}
		logger.log(Level.INFO, "Searching for MAC-Address: " + macAddress + " in Database");
		Client currentClient = clientRepo.findClientByClientMac(macAddress);
		if (currentClient == null) { // There is no Configuration for the current client in the database
			logger.log(Level.INFO, "No client configuration found, creating new one");
//			ClientInfoInputDialog infoDialog = new ClientInfoInputDialog(frame);
//			infoDialog.setVisible(true);
//			currentClient = infoDialog.getConstructedClient();
//			currentClient.setClientMac(macAddress);
//			clientRepo.save(currentClient);
		}
	}

	private void initFrame() {
		frame.getTestButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				roomRepo.findAll().forEach(System.out::println);
				ClientInfoInputDialog inputDialog = new ClientInfoInputDialog(frame);
				ClientInfoInputDialogController controller = new ClientInfoInputDialogController(context, inputDialog);
				controller.start();
				
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				context.close();
			}
		});
	}
	
	private void initRepos() {
		initMeasurementTypeRepo();
		initRoomRepo();
	}
	
	private void initMeasurementTypeRepo() {
		MeasurementType type = new MeasurementType();
		type.setDescription("Temperatur");
		measurementTypeRepo.save(type);
		logger.log(Level.INFO, "Anzahl Messarten: " + measurementTypeRepo.count());
	}
	
	private void initRoomRepo() {
		Location loc = new Location();
		loc.setDescription("Frankenstraße");
		locationRepo.save(loc);
		Room room = new Room();
		room.setDescription("HNN 203");
		room.setLocation(loc);
		roomRepo.save(room);
		logger.log(Level.INFO, "Anzahl Räume: " + roomRepo.count());
		
	}

}
