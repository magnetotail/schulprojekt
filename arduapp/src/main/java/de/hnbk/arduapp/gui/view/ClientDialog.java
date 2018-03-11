package de.hnbk.arduapp.gui.view;

import static de.hnbk.arduapp.gui.view.GuiConstants.COMPONENT_GAP;
import static de.hnbk.arduapp.gui.view.GuiConstants.COMPONENT_HEIGHT;
import static de.hnbk.arduapp.gui.view.GuiConstants.OUTER_PADDING;
import static de.hnbk.arduapp.gui.view.GuiConstants.SMALL_BUTTON_LENGTH;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.hnbk.arduapp.domain.classes.Client;
import de.hnbk.arduapp.domain.classes.MeasurementType;
import de.hnbk.arduapp.domain.classes.Room;
import info.clearthought.layout.TableLayout;

public class ClientInfoInputDialog extends AbstractCancelableDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextField clientDescriptionField;

	private JComboBox<MeasurementType> measurementTypeCombobox;

	private JButton configureTypesButton;

	private JComboBox<Room> roomCombobox;

	private JButton addRoomButton;
	
	private Client constructedClient;

	public ClientInfoInputDialog(JFrame owner) {
		super(owner);
		initDialog();
	}

	private void initDialog() {
		createComponentPanel();
	}

	public JTextField getClientNameField() {
		return clientDescriptionField;
	}

	public JComboBox<MeasurementType> getMeasurementTypeCombobox() {
		return measurementTypeCombobox;
	}

	public JButton getConfigureTypesButton() {
		return configureTypesButton;
	}

	public JComboBox<Room> getRoomCombobox() {
		return roomCombobox;
	}

	public JButton getAddRoomButton() {
		return addRoomButton;
	}

	
	public Client getConstructedClient() {
		return constructedClient;
	}

	protected void createComponentPanel() {
		JPanel componentPanel = new JPanel();
		double[] cols = {OUTER_PADDING, TableLayout.PREFERRED, COMPONENT_GAP, TableLayout.PREFERRED, COMPONENT_GAP, SMALL_BUTTON_LENGTH, OUTER_PADDING };
		double[] rows = { OUTER_PADDING, COMPONENT_HEIGHT, COMPONENT_GAP, COMPONENT_HEIGHT, COMPONENT_GAP, COMPONENT_HEIGHT, OUTER_PADDING};
		componentPanel.setLayout(new TableLayout(cols, rows));

		JLabel clientDescriptionLabel = new JLabel("Client Beschreibung:");
		componentPanel.add(clientDescriptionLabel, "1,1");
		clientDescriptionField = new JTextField();
		clientDescriptionField.setPreferredSize(new Dimension(150, COMPONENT_HEIGHT));
		componentPanel.add(clientDescriptionField, "3,1, 5,1");
		
		JLabel roomLabel = new JLabel("Raum:");
		componentPanel.add(roomLabel,"1,3");
		roomCombobox = new JComboBox<>();
		componentPanel.add(roomCombobox, "3,3");
		addRoomButton = new JButton();
		componentPanel.add(addRoomButton, "5,3");
		
		JLabel typeLabel = new JLabel("Messart:");
		componentPanel.add(typeLabel, "1,5");
		measurementTypeCombobox = new JComboBox<>();
		componentPanel.add(measurementTypeCombobox, "3,5");
		configureTypesButton = new JButton();
		componentPanel.add(configureTypesButton, "5,5");
		addComponentPanel(componentPanel);
	}

	@Override
	protected boolean check() {
		return true;
	}
}
