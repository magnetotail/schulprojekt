package de.hnbk.arduapp.gui.view;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

import de.hnbk.arduapp.gui.GuiUtils;
import info.clearthought.layout.TableLayout;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3530704208955662203L;

	private JButton testButton;

	public MainFrame() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		init();
	}

	private void init() {
		double[] cols = { TableLayout.PREFERRED, 10, TableLayout.PREFERRED };
		double[] rows = { 25, 10, 25, TableLayout.PREFERRED };
		this.setLayout(new TableLayout(cols, rows));
		testButton = new JButton("Test");
		getContentPane().add(testButton, "0,0");
		try {
			setIconImage(ImageIO.read(getClass().getResource("/thermometer_icon.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		pack();
		GuiUtils.centerWindow(this);
	}

	public JButton getTestButton() {
		return testButton;
	}

}
