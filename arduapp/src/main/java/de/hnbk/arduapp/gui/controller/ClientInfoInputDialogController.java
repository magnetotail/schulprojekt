package de.hnbk.arduapp.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.logging.Logger;

import org.springframework.context.ConfigurableApplicationContext;

import de.hnbk.arduapp.domain.repositories.RoomRepository;
import de.hnbk.arduapp.gui.view.AbstractCancelableDialog;
import de.hnbk.arduapp.gui.view.AddRoomDialog;
import de.hnbk.arduapp.gui.view.ClientInfoInputDialog;

public class ClientInfoInputDialogController {

	private static Logger logger = Logger.getLogger(ClientInfoInputDialogController.class.getName());
	
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
				
				AbstractCancelableDialog roomDialog = new AddRoomDialog(dialog, context.getBean(RoomRepository.class));
				roomDialog.setVisible(true);
			}
		});
	}
	
	public void start() {
		dialog.setVisible(true);
	}
	
	
}
