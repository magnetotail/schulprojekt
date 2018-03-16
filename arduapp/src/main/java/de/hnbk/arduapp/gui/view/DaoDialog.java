package de.hnbk.arduapp.gui.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.data.repository.CrudRepository;

import de.hnbk.arduapp.domain.classes.Describable;
import de.hnbk.arduapp.domain.classes.SimpleDaoInterface;
import info.clearthought.layout.TableLayout;

/**
 * Class to select a DAO. After closing the item can be obtained by invoking
 * {@link DaoDialog#getSelectedItem()}
 * 
 * @author magnetotail
 * @param <DAO>
 */
public class DaoDialog<DAO extends SimpleDaoInterface & Describable> extends AbstractCheckableDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Repository for the item
	 */
	private CrudRepository<DAO, Integer> repository;

	/**
	 * List to show all items
	 */
	private JList<DAO> itemList;

	/**
	 * Button to create a new item
	 */
	private JButton createButton;

	/**
	 * Selected Item
	 */
	private DAO selectedItem;

	/**
	 * Type of the item
	 */
	private Class<DAO> type;

	/**
	 * Creates a dialog to select a DAO.
	 * 
	 * @param owner
	 *            owner of the dialog
	 * @param repository
	 *            repository of the DAOs to display
	 * @param type
	 *            type of the DAOs to display
	 */
	public DaoDialog(JDialog owner, CrudRepository<DAO, Integer> repository, Class<DAO> type) {
		super(owner);
		this.repository = repository;
		this.type = type;
		init();
	}

	/**
	 * Creates a dialog to select a DAO.
	 * 
	 * @param owner
	 *            owner of the dialog
	 * @param repository
	 *            repository of the DAOs to display
	 * @param type
	 *            type of the DAOs to display
	 */
	public DaoDialog(JFrame owner, CrudRepository<DAO, Integer> repository, Class<DAO> type) {
		super(owner);
		this.repository = repository;
		this.type = type;
		init();
	}

	@Override
	protected boolean check() {
		return true;
	}

	protected SimpleChangeCreationDialog<DAO> createCreationDialog() {
		return new SimpleChangeCreationDialog<DAO>(this, type, repository);
	}

	protected SimpleChangeCreationDialog<DAO> createEditDialog(DAO item) {
		return new SimpleChangeCreationDialog<DAO>(this, type, item, repository);
	}

	private void init() {
		setResizable(false);
		JPanel componentPanel = new JPanel();

		// @formatter:off
		double[] cols = { TableLayout.FILL, GuiConstants.COMPONENT_GAP, GuiConstants.SMALL_BUTTON_LENGTH };
		double[] rows = { GuiConstants.COMPONENT_HEIGHT, GuiConstants.COMPONENT_GAP, TableLayout.FILL };
		// @formatter:on

		componentPanel.setLayout(new TableLayout(cols, rows));

		itemList = new JList<>();
		// itemList.setPreferredSize(new Dimension(150, 50));
		JScrollPane listPanel = new JScrollPane(itemList);
		listPanel.setPreferredSize(new Dimension(150, 50));
		componentPanel.add(listPanel, "0,0 0,2");
		itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		itemList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					selectedItem = itemList.getSelectedValue();
				}
			}
		});

		itemList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getClickCount() >= 2 && e.getButton() == MouseEvent.BUTTON1) {
					SimpleChangeCreationDialog<DAO> editDialog = createEditDialog(itemList.getSelectedValue());
					editDialog.setVisible(true);
				}
			}
		});

		createButton = new JButton("+");

		createButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleChangeCreationDialog<DAO> dialog = createCreationDialog();
				dialog.setVisible(true);
				refreshList();
			}
		});
		componentPanel.add(createButton, "2,0");

		itemList.setModel(new DefaultListModel<>());
		addComponentPanel(componentPanel, TableLayout.FILL);
		refreshList();
	}

	protected void refreshList() {
		DefaultListModel<DAO> listModel = (DefaultListModel<DAO>) itemList.getModel();
		listModel.clear();
		if (repository.count() > 0) {
			for (DAO item : repository.findAll()) {
				listModel.addElement(item);
			}
		}
	}

	public CrudRepository<DAO, Integer> getRepository() {
		return repository;
	}

	/**
	 * Can be used to obtain the selected item from the dialog
	 * 
	 * @return item that was selected in the itemlist before the dialog was closed
	 */
	public DAO getSelectedItem() {
		return selectedItem;
	}
	
}
