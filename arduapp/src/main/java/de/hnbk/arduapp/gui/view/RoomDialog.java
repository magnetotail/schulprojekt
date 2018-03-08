package de.hnbk.arduapp.gui.view;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.hnbk.arduapp.domain.classes.Room;
import de.hnbk.arduapp.domain.repositories.LocationRepository;
import de.hnbk.arduapp.domain.repositories.RoomRepository;
import info.clearthought.layout.TableLayout;

@SuppressWarnings("serial")
public class RoomDialog extends DaoDialog<Room> {


	private JTextField roomNameField;
	
	private LocationRepository locationRepository;

	public RoomDialog(JDialog owner, RoomRepository roomRepo, LocationRepository locationRepository) {
		super(owner, roomRepo, Room.class);
		this.locationRepository = locationRepository;
	}
	
	@Override
	protected CreateRoomDialog createCreationDialog() {
		return new CreateRoomDialog(this, getRepository(), locationRepository);
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
