package de.hnbk.arduapp.gui;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

public final class GuiUtils {

	private GuiUtils() {
		throw new AssertionError("Utility classes should not be instantiated");
	}

	public static void centerWindow(Window window) {
		Rectangle area = getScreenWorkingArea(window);
		double screenWidth = area.getWidth();
		double screenHeight = area.getHeight();
		window.setLocation((int) screenWidth / 2 - window.getWidth() / 2,
				(int) screenHeight / 2 - window.getHeight() / 2);
	}

	/**
	 * getScreenInsets, This returns the insets of the screen, which are defined by
	 * any task bars that have been set up by the user. This function accounts for
	 * multi-monitor setups. If a window is supplied, then the the monitor that
	 * contains the window will be used. If a window is not supplied, then the
	 * primary monitor will be used.
	 */
	static public Insets getScreenInsets(Window windowOrNull) {
		Insets insets;
		if (windowOrNull == null) {
			insets = Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getDefaultScreenDevice().getDefaultConfiguration());
		} else {
			insets = windowOrNull.getToolkit().getScreenInsets(windowOrNull.getGraphicsConfiguration());
		}
		return insets;
	}

	/**
	 * getScreenWorkingArea, This returns the working area of the screen. (The
	 * working area excludes any task bars.) This function accounts for
	 * multi-monitor setups. If a window is supplied, then the the monitor that
	 * contains the window will be used. If a window is not supplied, then the
	 * primary monitor will be used.
	 */
	static public Rectangle getScreenWorkingArea(Window windowOrNull) {
		Insets insets;
		Rectangle bounds;
		if (windowOrNull == null) {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			insets = Toolkit.getDefaultToolkit().getScreenInsets(ge.getDefaultScreenDevice().getDefaultConfiguration());
			bounds = ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		} else {
			GraphicsConfiguration gc = windowOrNull.getGraphicsConfiguration();
			insets = windowOrNull.getToolkit().getScreenInsets(gc);
			bounds = gc.getBounds();
		}
		bounds.x += insets.left;
		bounds.y += insets.top;
		bounds.width -= (insets.left + insets.right);
		bounds.height -= (insets.top + insets.bottom);
		return bounds;
	}
}
