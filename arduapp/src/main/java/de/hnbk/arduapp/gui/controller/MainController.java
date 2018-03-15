package de.hnbk.arduapp.gui.controller;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import de.hnbk.arduapp.AddressResolver;
import de.hnbk.arduapp.AddressResolver.AddressType;
import de.hnbk.arduapp.ArduApplication;
import de.hnbk.arduapp.SerialReader;
import de.hnbk.arduapp.domain.classes.Client;
import de.hnbk.arduapp.domain.classes.Location;
import de.hnbk.arduapp.domain.classes.MeasurementType;
import de.hnbk.arduapp.domain.classes.Room;
import de.hnbk.arduapp.domain.repositories.ClientRepository;
import de.hnbk.arduapp.domain.repositories.LocationRepository;
import de.hnbk.arduapp.domain.repositories.MeasurementTypeRepository;
import de.hnbk.arduapp.domain.repositories.RoomRepository;
import de.hnbk.arduapp.gui.AppModel;
import de.hnbk.arduapp.gui.view.CancelType;
import de.hnbk.arduapp.gui.view.ClientDialog;
import de.hnbk.arduapp.gui.view.MainFrame;
import de.hnbk.arduapp.reading.DBSaver;

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

	private MainFrame mainFrame;

	private SerialReader reader;

	public MainController() {
		mainFrame = new MainFrame();
	}

	@PostConstruct
	private void init() {
		try {
//			initRepos();
			initFrame();
			reader = new SerialReader();
			reader.start();
			threadPoolExecutor.scheduleAtFixedRate(new DBSaver(context), 2, 5, TimeUnit.MINUTES);
			initApplication();
			mainFrame.setVisible(true);
			ArduApplication.closeSplashscreen();
			initSystemTray();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
			ArduApplication.closeSplashscreen();
			System.exit(0);
		}
	}

	private void initSystemTray() {
		if (SystemTray.isSupported()) {                     
			logger.log(Level.DEBUG, "Systemtray supported, creating entry");
			SystemTray tray = SystemTray.getSystemTray();
			try {
				TrayIcon trayIcon = new TrayIcon(ImageIO.read(getClass().getResource("/black-thermometer-16.png")));
				PopupMenu popup = new PopupMenu();
				MenuItem openItem = new MenuItem("Anwendung öffnen");
				openItem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						mainFrame.setVisible(true);
					}
				});
				popup.add(openItem);
				MenuItem closeApplicationItem = new MenuItem("Anwendung beenden");
				closeApplicationItem.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						int answer = JOptionPane.showConfirmDialog(mainFrame, "Sind Sie sicher, dass Sie die Anwendung beenden möchten?", "Anwendung beenden", JOptionPane.YES_NO_OPTION);
						if(answer == JOptionPane.YES_OPTION) {
							logger.log(Level.INFO, "Exiting due to user exit.");
							System.exit(0);
						}
					}
				});
				popup.add(closeApplicationItem);
				trayIcon.setPopupMenu(popup);
				trayIcon.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
							if (!mainFrame.isVisible()) {
								logger.log(Level.INFO, "Opening main frame.");
								mainFrame.setVisible(true);
							}
						}
					}
				});
				tray.add(trayIcon);
			} catch (AWTException | IOException e) {
				logger.log(Level.FATAL, "Error while creating Systemtray icon", e);
			}
		} else {
			logger.log(Level.INFO, "No Systemtray supported!");
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
			ClientDialog infoDialog = new ClientDialog(mainFrame, CancelType.NOT_CANCELLABLE);
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
		mainFrame.getTestButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ClientDialog inputDialog = new ClientDialog(mainFrame);
				ClientDialogController controller = new ClientDialogController(context, inputDialog,
						AppModel.getInstance().getClient());
				controller.start();

			}
		});

		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mainFrame.setVisible(false);
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
