package de.hnbk.arduapp.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.DefaultComboBoxModel;

import org.springframework.context.ConfigurableApplicationContext;

import de.hnbk.arduapp.domain.classes.MeasurementType;
import de.hnbk.arduapp.domain.repositories.LocationRepository;
import de.hnbk.arduapp.domain.repositories.MeasurementTypeRepository;
import de.hnbk.arduapp.domain.repositories.RoomRepository;
import de.hnbk.arduapp.gui.view.AbstractCancelableDialog;
import de.hnbk.arduapp.gui.view.ClientInfoInputDialog;
import de.hnbk.arduapp.gui.view.DaoDialog;
import de.hnbk.arduapp.gui.view.RoomDialog;

public class ClientInfoInputDialogController {

//	private static Logger logger = Logger.getLogger(ClientInfoInputDialogController.class.getName());
	
	private ConfigurableApplicationContext context;
	
	private ClientInfoInputDialog dialog;
	
	public ClientInfoInputDialogController(ConfigurableApplicationContext context, ClientInfoInputDialog dialog) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(dialog);
		this.context = context;
		this.dialog = dialog;
		initDialog();
	}

	private void initDialog() {
		dialog.getAddRoomButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				AbstractCancelableDialog roomDialog = new RoomDialog(dialog, context.getBean(RoomRepository.class), context.getBean(LocationRepository.class));
				roomDialog.setVisible(true);
			}
		});
		
		dialog.getConfigureTypesButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MeasurementTypeRepository measureRepo =  context.getBean(MeasurementTypeRepository.class);
				DaoDialog<MeasurementType> daoDialog = new DaoDialog<>(dialog, measureRepo, MeasurementType.class);
				daoDialog.setVisible(true);
				DefaultComboBoxModel<MeasurementType> boxModel = (DefaultComboBoxModel<MeasurementType>) dialog.getMeasurementTypeCombobox().getModel();
				boxModel.removeAllElements();
				measureRepo.findAll().forEach(dao -> boxModel.addElement(dao));
			}
		});
	}
	
	public void start() {
		dialog.setVisible(true);
	}
	
	
}
