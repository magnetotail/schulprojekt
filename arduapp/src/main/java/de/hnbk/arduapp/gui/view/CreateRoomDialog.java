package de.hnbk.arduapp.gui.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.data.repository.CrudRepository;

import de.hnbk.arduapp.domain.classes.Location;
import de.hnbk.arduapp.domain.classes.Room;
import info.clearthought.layout.TableLayout;

@SuppressWarnings("serial")
class CreateRoomDialog extends SimpleChangeCreationDialog<Room> {

	private JComboBox<Location> locationCombobox;
	
	private CrudRepository<Location, Integer> locationRepository;

	public CreateRoomDialog(JDialog owner, CrudRepository<Room, Integer> repository, CrudRepository<Location, Integer> locationRepository) {
		super(owner, Room.class, repository);
		this.locationRepository = locationRepository;
		init();
	}

	public CreateRoomDialog(JDialog owner, Room itemToChange, CrudRepository<Room, Integer> repository) {
		super(owner, Room.class, itemToChange, repository);
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
		locationPanel.add(locationCombobox, "2,0");
		JButton addLocationButton = new JButton();
		addLocationButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleChangeCreationDialog<Location> createLocationDialog = new SimpleChangeCreationDialog<>(CreateRoomDialog.this, Location.class, locationRepository);
				createLocationDialog.setVisible(true);
				if(!createLocationDialog.wasCancelled()) {
					locationCombobox.addItem(createLocationDialog.getItem());
				}
			}
		});
		locationPanel.add(addLocationButton, "4,0");
		addComponentPanel(locationPanel);
	}

	private void initComboBox() {
		locationCombobox = new JComboBox<>();
		locationRepository.findAll().forEach(l -> locationCombobox.addItem(l));
	}

	@Override
	protected Room fillDAO(Room itemToFill) {
		super.fillDAO(itemToFill);
		itemToFill.setLocation(locationCombobox.getItemAt(locationCombobox.getSelectedIndex()));
		return itemToFill;
	}

	@Override
	protected boolean check() {
		if (locationCombobox.getSelectedItem() != null) {
			return super.check();
		}
		return false;
	}

}