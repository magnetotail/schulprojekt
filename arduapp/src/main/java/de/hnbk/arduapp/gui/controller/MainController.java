package de.hnbk.arduapp.gui.controller;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
import de.hnbk.arduapp.gui.view.AbstractCheckableDialog.CancelType;
import de.hnbk.arduapp.gui.view.ClientDialog;
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

	ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(1);

	private MainFrame frame;

	public MainController() {
		frame = new MainFrame();
	}

	@PostConstruct
	private void init() {
		if (SystemTray.isSupported()) {
			logger.log(Level.FINE, "Systemtray supported, creating entry");
			SystemTray tray = SystemTray.getSystemTray();
			try {
				tray.add(new TrayIcon(new BufferedImage(2, 2, BufferedImage.TYPE_3BYTE_BGR)));
			} catch (AWTException e) {
				logger.log(Level.SEVERE, "Error while creating Systemtray icon");
				e.printStackTrace();
			}
		}
		try {
			initRepos();
			initFrame();
			initApplication();
			frame.setVisible(true);
			ScheduledFuture<?> f = threadPoolExecutor.scheduleAtFixedRate(new DBSaver(context), 10, 50, TimeUnit.SECONDS);
			while (f.getDelay(TimeUnit.SECONDS) > 0) {
				System.out.println(f.getDelay(TimeUnit.SECONDS));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ArduApplication.closeSplashscreen();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
			ArduApplication.closeSplashscreen();
			System.exit(0);
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
			ClientDialog infoDialog = new ClientDialog(frame, CancelType.NOT_CANCELLABLE);
			ClientDialogController controller = new ClientDialogController(context, infoDialog);
			ArduApplication.closeSplashscreen();
			controller.start();
			currentClient = controller.getConstructedClient();
			currentClient.setClientMac(macAddress);
			clientRepo.save(currentClient);
		}
		AppModel.getInstance().setClient(currentClient);
		logger.log(Level.INFO, "Set client to '" + currentClient + "'");
	}

	private void initFrame() {
		frame.getTestButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ClientDialog inputDialog = new ClientDialog(frame);
				ClientDialogController controller = new ClientDialogController(context, inputDialog, AppModel.getInstance().getClient());
				controller.start();

			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
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
