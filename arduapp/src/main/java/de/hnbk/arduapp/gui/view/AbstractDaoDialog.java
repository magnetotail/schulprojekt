package de.hnbk.arduapp.gui.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.springframework.data.repository.CrudRepository;

import de.hnbk.arduapp.domain.classes.Describable;
import de.hnbk.arduapp.domain.classes.SimpleDaoInterface;
import info.clearthought.layout.TableLayout;

public class AbstractDaoDialog<DAO extends SimpleDaoInterface & Describable> extends AbstractCancelableDialog {

	private CrudRepository<DAO, Integer> repository;

	private JList<DAO> itemList;

	private JButton editButton;

	private DAO generatedItem;

	private Class<DAO> type;

	public AbstractDaoDialog(JDialog owner, CrudRepository<DAO, Integer> repository, Class<DAO> type) {
		super(owner);
		this.repository = repository;
		this.type = type;
		init();
	}

	public AbstractDaoDialog(JFrame owner, CrudRepository<DAO, Integer> repository, Class<DAO> type) {
		super(owner);
		this.repository = repository;
		this.type = type;
		init();
	}

	protected DAO createEmptyItem() {
		try {
			return type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected boolean check() {
		return true;
	}

	protected SimpleCreationDialog createCreationDialog() {
		return new SimpleCreationDialog(this, createEmptyItem());
	}

	private void init() {
		setResizable(false);
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
//		itemList.setPreferredSize(new Dimension(150, 50));
		JScrollPane listPanel = new JScrollPane(itemList);
		listPanel.setPreferredSize(new Dimension(150, 50));
		componentPanel.add(listPanel, "0,0 0,2");

		itemList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getClickCount() >= 2) {

				}
			}
		});

		editButton = new JButton();

		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleCreationDialog dialog = new SimpleCreationDialog(AbstractDaoDialog.this, createEmptyItem());
				dialog.setVisible(true);
				refreshList();
			}
		});
		componentPanel.add(editButton, "2,0");

		itemList.setModel(new DefaultListModel<>());
		addComponentPanel(componentPanel, TableLayout.FILL);
		refreshList();
	}

	public void addListItem(DAO item) {
		DefaultListModel<DAO> listModel = (DefaultListModel<DAO>) itemList.getModel();
		listModel.addElement(item);
	}

	protected void refreshList() {
		DefaultListModel<DAO> listModel = (DefaultListModel<DAO>) itemList.getModel();
		listModel.clear();
		for (DAO item : repository.findAll()) {
			listModel.addElement(item);
		}
	}

	public CrudRepository<DAO, Integer> getRepository() {
		return repository;
	}

	public DAO getGeneratedItem() {
		return generatedItem;
	}

	public class SimpleCreationDialog extends AbstractCancelableDialog {
		
		private final Logger logger = Logger.getLogger(SimpleCreationDialog.class.getName());

		private JTextField descriptionField;

		private DAO changingItem;

		public SimpleCreationDialog(JDialog owner, DAO itemToChange) {
			super(owner);
			this.changingItem = itemToChange;
			TableLayout layout = new TableLayout();
			layout.insertColumn(0, TableLayout.FILL);
			layout.insertColumn(0, TableLayout.FILL);
			layout.insertRow(0, GuiConstants.COMPONENT_HEIGHT);

			JPanel componentPanel = new JPanel();
			componentPanel.setLayout(layout);

			JLabel descriptionLabel = new JLabel(itemToChange.getDescriptionLabelText());
			componentPanel.add(descriptionLabel, "0,0");

			descriptionField = new JTextField();
			descriptionField.setText(changingItem.getDescription());

			componentPanel.add(descriptionField, "1,0");

			addComponentPanel(componentPanel, TableLayout.PREFERRED);
			addCloseAction(new CloseAction() {

				@Override
				public void performAction() {
					fillDAO(changingItem);
					if (!wasCancelled()) {
						List<Method> getters = Arrays.stream(type.getDeclaredMethods())
								.filter(m -> m.getName().contains("get")
										&& !m.getName().equals("get" + type.getSimpleName() + "Id"))
								.collect(Collectors.toList());
						for (Method getter : getters) {
							try {
								if ((getter.invoke(changingItem) == null)) { // Feld ist nicht gesetzt
									logger.log(Level.INFO, getter + " lieferte null!");
									JOptionPane.showMessageDialog(SimpleCreationDialog.this,
											"Bitte alle Felder füllen.", "Fehler", JOptionPane.ERROR_MESSAGE);
									return;
								}
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
								e.printStackTrace();
								return;
							}
							repository.save(changingItem);
						}
					}
				}
			});
		}

		protected DAO fillDAO(DAO itemToFill) {
			itemToFill.setDescription(descriptionField.getText());
			return itemToFill;
		}

		@Override
		protected boolean check() {
			if (!descriptionField.getText().matches(changingItem.getDescriptionCheckRegex())) {
				JOptionPane.showMessageDialog(this,
						"Beschreibung passt nicht auf den Regex \"" + changingItem.getDescriptionCheckRegex() + "\"");
				return false;
			}
			return true;
		}

	}
}
