package de.hnbk.arduapp;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	
	private Image image;

	public ImagePanel(Image image) {
		this.image = image;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
	}

}
