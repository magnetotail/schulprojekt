package de.hnbk.arduapp.gui.view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.data.repository.CrudRepository;

import de.hnbk.arduapp.domain.classes.Describable;
import de.hnbk.arduapp.domain.classes.SimpleDaoInterface;
import info.clearthought.layout.TableLayout;

/**
 * Class to create or change a simple DAO with a description. Extends
 * {@link AbstractCancelableDialog}. When extended for more complex DAO it is
 * possible to add other components by invoking
 * {@link AbstractCancelableDialog#addComponentPanel(JPanel)}.
 * <p>
 * Please overwrite {@link SimpleChangeCreationDialog#fillDAO()} and invoke the
 * super method, to properly fill the item;
 * 
 * @author magnetotail
 */
@SuppressWarnings("serial")
public class SimpleChangeCreationDialog<DAO extends SimpleDaoInterface & Describable> extends AbstractCancelableDialog {

	private final Logger logger = Logger.getLogger(SimpleChangeCreationDialog.class.getName());

	/**
	 * textfield to enter the description
	 */
	private JTextField descriptionField;

	/**
	 * item to change or create
	 */
	private DAO item;

	/**
	 * repository
	 */
	private CrudRepository<DAO, Integer> repository;

	/**
	 * type of the created/changed item
	 */
	private Class<DAO> type;

	/**
	 * creates a new dialog to create a simple DAO. The DAO will be new created
	 * 
	 * @param owner owner of the dialog
	 * @param type type of the DAO to create or edit
	 * @param repository repository of the DAO
	 */
	public SimpleChangeCreationDialog(JDialog owner, Class<DAO> type, CrudRepository<DAO, Integer> repository) {
		this(owner, type, null, repository);
	}

	/**
	 * creates a new dialog to create a simple DAO. The DAO will be new created
	 * 
	 * @param owner owner of the dialog
	 * @param type type of the DAO to create or edit
	 * @param repository repository of the DAO
	 * @param itemToChange the item to change. May be <code>null</code> in which
	 *        case a new item will be created
	 */
	public SimpleChangeCreationDialog(JDialog owner, Class<DAO> type, DAO itemToChange, CrudRepository<DAO, Integer> repository) {
		super(owner);
		this.repository = repository;
		this.type = type;
		if (itemToChange == null) {
			logger.log(Level.FINE, "Creating new item");
			this.item = createEmptyItem();
		} else {
			logger.log(Level.FINE, "Editing" + itemToChange.getId() + itemToChange);
			this.item = itemToChange;
		}
		init();
	}

	/**
	 * create an empty item
	 * 
	 * @return empty item
	 */
	protected DAO createEmptyItem() {
		DAO item = null;
		try {
			item = type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return item;
	}

	private void init() {
		TableLayout layout = new TableLayout();
		layout.insertColumn(0, TableLayout.FILL);
		layout.insertColumn(0, TableLayout.FILL);
		layout.insertRow(0, GuiConstants.COMPONENT_HEIGHT);

		JPanel componentPanel = new JPanel();
		componentPanel.setLayout(layout);

		JLabel descriptionLabel = new JLabel(item.getDescriptionLabelText());
		componentPanel.add(descriptionLabel, "0,0");

		descriptionField = new JTextField();
		descriptionField.setText(item.getDescription());

		componentPanel.add(descriptionField, "1,0");

		addComponentPanel(componentPanel, TableLayout.PREFERRED);
		addCloseAction(new SaveAction());

	}

	/**
	 * @param itemToFill
	 * @return
	 */
	protected DAO fillDAO(DAO itemToFill) {
		itemToFill.setDescription(descriptionField.getText());
		return itemToFill;
	}

	@Override
	protected boolean check() {
		if (!descriptionField.getText().matches(item.getDescriptionCheckRegex())) {
			JOptionPane.showMessageDialog(this, "Beschreibung passt nicht auf den Regex \"" + item.getDescriptionCheckRegex() + "\"");
			return false;
		}
		return true;
	}

	/**
	 * When closing the Dialog, this class will do following actions if the Dialog
	 * was not cancelled:<br>
	 * <ul>
	 * <li>get all get methods from the type of the DAO</li>
	 * <li>invoke all getters and open an error dialog if one of them returns
	 * null</li>
	 * <li>save the item</li>
	 * </ul>
	 * 
	 * @author magnetotail
	 */
	private class SaveAction implements CloseAction {

		@Override
		public void performAction() {
			if (!wasCancelled()) {
				fillDAO(item);
				List<Method> getters = Arrays.stream(type.getDeclaredMethods())
						.filter(m -> m.getName().contains("get") && !m.getName().equals("get" + type.getSimpleName() + "Id")).collect(Collectors.toList());
				for (Method getter : getters) {
					try {
						if ((getter.invoke(item) == null)) { // field is not filled
							logger.log(Level.INFO, getter + " lieferte null!");
							JOptionPane.showMessageDialog(SimpleChangeCreationDialog.this, "Bitte alle Felder füllen.", "Fehler", JOptionPane.ERROR_MESSAGE);
							return;
						}
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
						return;
					}
					repository.save(item);
				}
			}
		}
	}

}