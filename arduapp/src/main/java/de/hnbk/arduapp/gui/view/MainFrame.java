package de.hnbk.arduapp.gui.view;

import javax.swing.JButton;
import javax.swing.JFrame;

import info.clearthought.layout.TableLayout;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3530704208955662203L;

	private JButton testButton;
	
	public MainFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		init();
	}

	private void init() {
		System.out.println(void.class.getName());
		double[] cols = {TableLayout.PREFERRED, 10, TableLayout.PREFERRED};
		double[] rows = {25, 10, 25, TableLayout.PREFERRED};
		this.setLayout(new TableLayout(cols,rows));
		testButton = new JButton("Test");
		getContentPane().add(testButton, "0,0");
		
	}

	public JButton getTestButton() {
		return testButton;
	}

}
