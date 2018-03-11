package de.hnbk.arduapp.gui.view;

import static de.hnbk.arduapp.gui.view.GuiConstants.COMPONENT_GAP;
import static de.hnbk.arduapp.gui.view.GuiConstants.COMPONENT_HEIGHT;
import static de.hnbk.arduapp.gui.view.GuiConstants.OUTER_PADDING;
import static de.hnbk.arduapp.gui.view.GuiConstants.SMALL_BUTTON_LENGTH;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import info.clearthought.layout.TableLayout;

public class ClientDialog extends AbstractCheckableDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextField clientDescriptionField;

	private JLabel measurementTypeLabel;

	private JButton configureTypesButton;

	private JLabel roomLabel;

	private JButton setRoomButton;
	
	public ClientDialog(JFrame owner) {
		this(owner, CancelType.CANCELLABLE);
	}

	public ClientDialog(JFrame owner, CancelType cancelType) {
		super(owner, cancelType);
		initDialog();
	}

	private void initDialog() {
		createComponentPanel();
	}

	public JTextField getClientNameField() {
		return clientDescriptionField;
	}

	public JLabel getMeasurementTypeLabel() {
		return measurementTypeLabel;
	}

	public JLabel getRoomLabel() {
		return roomLabel;
	}

	public JButton getSetRoomButton() {
		return setRoomButton;
	}

	protected void createComponentPanel() {
		JPanel componentPanel = new JPanel();
		double[] cols = { OUTER_PADDING, TableLayout.PREFERRED, COMPONENT_GAP, TableLayout.PREFERRED, COMPONENT_GAP, SMALL_BUTTON_LENGTH, OUTER_PADDING };
		double[] rows = { OUTER_PADDING, COMPONENT_HEIGHT, COMPONENT_GAP, COMPONENT_HEIGHT, COMPONENT_GAP, COMPONENT_HEIGHT, OUTER_PADDING };
		componentPanel.setLayout(new TableLayout(cols, rows));

		JLabel clientDescriptionLabel = new JLabel("Client Beschreibung:");
		componentPanel.add(clientDescriptionLabel, "1,1");
		clientDescriptionField = new JTextField();
		clientDescriptionField.setPreferredSize(new Dimension(150, COMPONENT_HEIGHT));
		componentPanel.add(clientDescriptionField, "3,1, 5,1");

		JLabel roomShowLabel = new JLabel("Raum:");
		componentPanel.add(roomShowLabel, "1,3");
		roomLabel = new JLabel();
		componentPanel.add(roomLabel, "3,3");
		setRoomButton = new JButton();
		componentPanel.add(setRoomButton, "5,3");

		JLabel typeLabel = new JLabel("Messart:");
		componentPanel.add(typeLabel, "1,5");
		measurementTypeLabel = new JLabel();
		componentPanel.add(measurementTypeLabel, "3,5");
		configureTypesButton = new JButton();
		componentPanel.add(configureTypesButton, "5,5");
		addComponentPanel(componentPanel);
	}

	@Override
	protected boolean check() {
		return roomLabel.getText() != null && !"".equals(roomLabel.getText()) && measurementTypeLabel.getText() != null
				&& !"".equals(measurementTypeLabel.getText());
	}

	public JButton getConfigureTypesButton() {
		return configureTypesButton;
	}
}
