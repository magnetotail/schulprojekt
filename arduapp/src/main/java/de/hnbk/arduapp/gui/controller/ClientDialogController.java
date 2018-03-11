package de.hnbk.arduapp.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.context.ConfigurableApplicationContext;

import de.hnbk.arduapp.domain.classes.Client;
import de.hnbk.arduapp.domain.classes.MeasurementType;
import de.hnbk.arduapp.domain.repositories.LocationRepository;
import de.hnbk.arduapp.domain.repositories.MeasurementTypeRepository;
import de.hnbk.arduapp.domain.repositories.RoomRepository;
import de.hnbk.arduapp.gui.view.ClientDialog;
import de.hnbk.arduapp.gui.view.DaoDialog;
import de.hnbk.arduapp.gui.view.RoomDialog;

public class ClientDialogController {

//	private static Logger logger = Logger.getLogger(ClientInfoInputDialogController.class.getName());
	
	private ConfigurableApplicationContext context;
	
	private ClientDialog dialog;

	private Client client;
	
	public ClientDialogController(ConfigurableApplicationContext context, ClientDialog dialog) {
		this.context = context;
		this.dialog = dialog;
		this.client = new Client();
		initDialog();
		
	}
	
	public ClientDialogController(ConfigurableApplicationContext context, ClientDialog dialog, Client client) {
		this(context, dialog);
		this.client = client;
	}

	private void initDialog() {
		dialog.getSetRoomButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				RoomDialog roomDialog = new RoomDialog(dialog, context.getBean(RoomRepository.class), context.getBean(LocationRepository.class));
				roomDialog.setVisible(true);
				if(!roomDialog.wasCancelled()) {
					dialog.getRoomLabel().setText(roomDialog.getSelectedItem().getDescription());
					client.setRoom(roomDialog.getSelectedItem());
				}
			}
		});
		dialog.getConfigureTypesButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MeasurementTypeRepository measureRepo =  context.getBean(MeasurementTypeRepository.class);
				DaoDialog<MeasurementType> daoDialog = new DaoDialog<>(dialog, measureRepo, MeasurementType.class);
				daoDialog.setVisible(true);
				if(!daoDialog.wasCancelled()) {
					MeasurementType type = daoDialog.getSelectedItem();
					dialog.getMeasurementTypeLabel().setText(daoDialog.getSelectedItem().getDescription());
					client.setMeasurementType(type);
				}
				
			}
		});
	}
	
	public void start() {
		dialog.setVisible(true);
	}

	public Client getConstructedClient() {
		return client;
	}
	
	
}
