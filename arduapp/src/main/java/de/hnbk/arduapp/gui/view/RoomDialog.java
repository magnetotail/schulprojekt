package de.hnbk.arduapp.gui.view;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;

import de.hnbk.arduapp.domain.classes.Location;
import de.hnbk.arduapp.domain.classes.Room;
import de.hnbk.arduapp.domain.repositories.RoomRepository;
import info.clearthought.layout.TableLayout;

public class RoomDialog extends AbstractDaoDialog<Room> {


	private Room generatedRoom;

	private JTextField roomNameField;

	public RoomDialog(JDialog owner, RoomRepository roomRepo) {
		super(owner, roomRepo, Room.class);
		
	}
	
	@Override
	protected SimpleCreationDialog createCreationDialog() {
		return new CreateRoomDialog(this, createEmptyItem());
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

	private class CreateRoomDialog extends SimpleCreationDialog {

		private JComboBox<Location> locationCombobox;
		
		public CreateRoomDialog(JDialog owner, Room itemToChange) {
			super(owner, itemToChange);
			init();
		}
		
		private void init() {
			JPanel locationPanel = new JPanel();
			//@formatter:off
			double[] cols = {TableLayout.FILL, GuiConstants.COMPONENT_GAP, TableLayout.FILL, GuiConstants.COMPONENT_GAP, GuiConstants.SMALL_BUTTON_LENGTH};
			double[] rows = {GuiConstants.COMPONENT_HEIGHT};
			//@formatter:on
			locationPanel.setLayout(new TableLayout(cols, rows));
			locationPanel.add(new JLabel("Standort"), "0,0");
			initComboBox();
		}
		
		private void initComboBox(){
			locationCombobox = new JComboBox<>();
		}

		@Override
		protected Room fillDAO(Room itemToFill) {
			super.fillDAO(itemToFill);
			itemToFill.setLocation(locationCombobox.getItemAt(locationCombobox.getSelectedIndex()));
			return itemToFill;
		}
		
		
		@Override
		protected boolean check() {
			if(locationCombobox.getSelectedItem() != null) {
				return super.check();
			}
			return false;
		}
		
		
	}
	
	
}
