package de.hnbk.arduapp.gui.view;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import de.hnbk.arduapp.gui.GuiUtils;
import info.clearthought.layout.TableLayout;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3530704208955662203L;

	private JButton testButton;

	private JLabel savedCountLabel;

	private JLabel cachedCountLabel;

	public MainFrame() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		init();
	}

	private void init() {
		double[] cols = { TableLayout.PREFERRED, 10, TableLayout.PREFERRED };
		double[] rows = { 25, 10, 25, GuiConstants.COMPONENT_GAP, GuiConstants.COMPONENT_HEIGHT,
				GuiConstants.COMPONENT_GAP, GuiConstants.COMPONENT_HEIGHT, TableLayout.PREFERRED };
		this.setLayout(new TableLayout(cols, rows));
		testButton = new JButton("Test");
		getContentPane().add(testButton, "0,0");
		JLabel savedLabel = new JLabel("Gespeichert:");
		JLabel cachedLabel = new JLabel("Cached:");
		savedCountLabel = new JLabel("");
		cachedCountLabel = new JLabel("");
		getContentPane().add(savedLabel, "0,2");
		getContentPane().add(savedCountLabel, "2,2");
		getContentPane().add(cachedLabel, "0,4");
		getContentPane().add(cachedCountLabel,"2,4");

		try {
			setIconImage(ImageIO.read(getClass().getResource("/thermometer_icon.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		pack();
		GuiUtils.centerWindow(this);
	}

	public JLabel getSavedCountLabel() {
		return savedCountLabel;
	}

	public JLabel getCachedCountLabel() {
		return cachedCountLabel;
	}

	public JButton getTestButton() {
		return testButton;
	}

}
