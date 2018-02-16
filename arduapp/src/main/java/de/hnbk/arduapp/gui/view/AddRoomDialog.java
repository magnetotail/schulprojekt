package de.hnbk.arduapp.gui.view;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.hnbk.arduapp.domain.classes.Room;
import de.hnbk.arduapp.domain.repositories.RoomRepository;
import info.clearthought.layout.TableLayout;

public class AddRoomDialog extends AbstractDaoDialog<Room> {


	private Room generatedRoom;

	private JTextField roomNameField;

	public AddRoomDialog(JDialog owner, RoomRepository roomRepo) {
		super(owner, roomRepo);
		
	}

	public Room getGeneratedRoom() {
		return generatedRoom;
	}

	protected void createComponentPanel() {
		JPanel componentPanel = new JPanel();
		double[] cols = {GuiConstants.OUTER_PADDING, TableLayout.FILL, GuiConstants.COMPONENT_GAP, TableLayout.FILL, GuiConstants.OUTER_PADDING};
		double[] rows = {GuiConstants.OUTER_PADDING, GuiConstants.COMPONENT_HEIGHT, GuiConstants.OUTER_PADDING};
		componentPanel.setLayout(new TableLayout(cols, rows));
		JLabel roomLabel = new JLabel("Raum:");
		componentPanel.add(roomLabel, "1,1");
		roomNameField = new JTextField();
		componentPanel.add(roomNameField, "3,1");
		TableLayout layout = (TableLayout) componentPanel.getLayout();

		addComponentPanel(componentPanel, TableLayout.FILL);
		
	}

	
	
	@Override
	protected boolean check() { 
		return true;
	}
	
	@Override
	public RoomRepository getRepository() {
		return (RoomRepository) super.getRepository();
	}

}
