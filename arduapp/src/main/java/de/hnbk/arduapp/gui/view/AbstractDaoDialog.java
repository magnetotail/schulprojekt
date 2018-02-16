package de.hnbk.arduapp.gui.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.data.repository.CrudRepository;

import de.hnbk.arduapp.domain.classes.DaoInterface;
import de.hnbk.arduapp.domain.classes.Describable;
import info.clearthought.layout.TableLayout;

public abstract class AbstractDaoDialog<DAO extends DaoInterface & Describable> extends AbstractCancelableDialog {

	private CrudRepository<DAO, Integer> repository;

	private JList<DAO> itemList;
	
	private JButton editButton;

	public AbstractDaoDialog(JDialog owner, CrudRepository<DAO, Integer> repository) {
		super(owner);
		this.repository = repository;
		init();
	}

	public AbstractDaoDialog(JFrame owner, CrudRepository<DAO, Integer> repository) {
		super(owner);
		this.repository = repository;
		init();
	}

	private void init() {
		JPanel componentPanel = new JPanel();

		//@formatter:off
		double[] cols = { TableLayout.FILL, GuiConstants.COMPONENT_GAP,	GuiConstants.SMALL_BUTTON_LENGTH };
		double[] rows = { 	GuiConstants.COMPONENT_HEIGHT, 
							GuiConstants.COMPONENT_GAP, 
							TableLayout.FILL 
							};
		//@formatter:on
		
		componentPanel.setLayout(new TableLayout(cols, rows));
		
		itemList = new JList<>();
		itemList.setPreferredSize(new Dimension(150, 50));
		componentPanel.add(itemList, "0,0 0,2");
		
		editButton = new JButton();
		
		editButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(itemList.getSelectedValue() != null) {
					DescriptionDialog dialog = new DescriptionDialog(AbstractDaoDialog.this, itemList.getSelectedValue());
					dialog.setVisible(true);
				}
			}
		});
		componentPanel.add(editButton, "2,0");

		itemList.setModel(new DefaultListModel<>());
		addComponentPanel(componentPanel, TableLayout.FILL);
		refreshList();
	}
	
	private void refreshList() {
		DefaultListModel<DAO> listModel = (DefaultListModel<DAO>) itemList.getModel();
		listModel.clear();
		for(DAO item : repository.findAll()) {
			listModel.addElement(item);
		}
	}

	public CrudRepository<DAO, Integer> getRepository() {
		return repository;
	}

	private class DescriptionDialog extends AbstractCancelableDialog{
		
		private JTextField descriptionField;
		
		private DAO changingItem;
		
		public DescriptionDialog(JDialog owner, DAO itemToChange) {
			super(owner);
			this.changingItem = itemToChange;
			TableLayout layout = new TableLayout();
			layout.insertColumn(0, TableLayout.PREFERRED);
			layout.insertColumn(0, TableLayout.FILL);
			layout.insertRow(0, GuiConstants.COMPONENT_HEIGHT);
			
			JPanel componentPanel = new JPanel();
			componentPanel.setLayout(layout);
			
			JLabel descriptionLabel = new JLabel("Beschreibung:");
			componentPanel.add(descriptionLabel, "0,0");
			
			descriptionField = new JTextField();
			descriptionField.setText(changingItem.getDescription());
			
			componentPanel.add(descriptionField, "1,0");
			
			addComponentPanel(componentPanel, TableLayout.PREFERRED);
		}
		
		@Override
		protected boolean check() {
			return changingItem.getDescriptionCheckRegex().matches(descriptionField.getText());
		}
		
	}
}

