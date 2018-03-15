package de.hnbk.arduapp.gui.view;

import static de.hnbk.arduapp.gui.view.GuiConstants.COMPONENT_GAP_BIG;
import static de.hnbk.arduapp.gui.view.GuiConstants.OUTER_PADDING;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.hnbk.arduapp.gui.GuiUtils;
import info.clearthought.layout.TableLayout;

@SuppressWarnings("serial")
public abstract class AbstractCheckableDialog extends JDialog {

	private JButton okButton;
	private boolean wasCanceled = true;
	private JButton cancelButton;

	private JPanel componentPanelPanel;
	private List<CloseAction> closeActionList = new ArrayList<CloseAction>();
	private CancelType cancelType;

	public AbstractCheckableDialog(JFrame owner) {
		this(owner, CancelType.CANCELLABLE);
	}

	public AbstractCheckableDialog(JDialog owner) {
		this(owner, CancelType.CANCELLABLE);
	}

	public AbstractCheckableDialog(JFrame owner, CancelType cancelType) {
		super(owner);
		this.cancelType = cancelType;
		initDialog();
	}

	public AbstractCheckableDialog(JDialog owner, CancelType cancelType) {
		super(owner);
		this.cancelType = cancelType;
		initDialog();
	}

	protected abstract boolean check();

	private void initDialog() {
		setModal(true);
		Container contentPane = getContentPane();
		this.setLayout(new BorderLayout());
		try {
			setIconImage(ImageIO.read(getClass().getResource("/thermometer_icon.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		componentPanelPanel = new JPanel();
		double[] colsComponents = { OUTER_PADDING, TableLayout.FILL, OUTER_PADDING };
		double[] rowsComponents = { OUTER_PADDING, OUTER_PADDING };

		componentPanelPanel.setLayout(new TableLayout(colsComponents, rowsComponents));
		contentPane.add(componentPanelPanel, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();

		double[] colsButtons;
		if (cancelType == CancelType.CANCELLABLE) {
			colsButtons = new double[] { OUTER_PADDING, TableLayout.FILL, COMPONENT_GAP_BIG, TableLayout.FILL, OUTER_PADDING };
		} else {
			colsButtons = new double[] { OUTER_PADDING, TableLayout.FILL, OUTER_PADDING };
		}
		double[] rowsButtons = { TableLayout.FILL };
		buttonPanel.setLayout(new TableLayout(colsButtons, rowsButtons));

		okButton = new JButton("OK");
		buttonPanel.add(okButton, "1,0");

		if (cancelType == CancelType.CANCELLABLE) {
			cancelButton = new JButton("Abbrechen");
			buttonPanel.add(cancelButton, "3,0");

			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					close(true);
				}
			});
		}
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (cancelType == CancelType.CANCELLABLE) {
					close(true);
				}
			}

		});
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (check()) {
					close(false);
				}
			}
		});

		pack();
		GuiUtils.centerWindow(this);
	}

	/**
	 * Appends a panel to the end of the dialog. The panel will get the constraint
	 * {@link TableLayout.PREFFERED}
	 * 
	 * @param componentPanel panel to append to the dialog
	 */
	protected void addComponentPanel(JPanel componentPanel) {
		addComponentPanel(componentPanel, TableLayout.PREFERRED);
	}

	public void addCloseAction(CloseAction action) {
		closeActionList.add(action);
	}

	public void removeCloseAction(CloseAction action) {
		closeActionList.remove(action);
	}

	protected void addComponentPanel(JPanel componentPanel, double rowHeight) {
		TableLayout componentPanelPanelLayout = (TableLayout) componentPanelPanel.getLayout();
		componentPanelPanelLayout.insertRow(1, GuiConstants.COMPONENT_GAP);
		componentPanelPanelLayout.insertRow(1, rowHeight);
		componentPanelPanel.add(componentPanel, "1," + 1);
		componentPanelPanelLayout.layoutContainer(this);
		pack();
		GuiUtils.centerWindow(this);
	}

	private void close(boolean wasCancelled) {
		this.wasCanceled = wasCancelled;
		for (CloseAction action : closeActionList) {
			action.performAction();
		}
		setVisible(false);
		dispose();

	}

	public boolean wasCancelled() {
		return wasCanceled;
	}

}