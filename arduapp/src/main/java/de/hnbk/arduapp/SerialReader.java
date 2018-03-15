package de.hnbk.arduapp;

import java.awt.FlowLayout;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.hnbk.arduapp.domain.classes.Measurement;
import de.hnbk.arduapp.gui.AppModel;
import de.hnbk.arduapp.gui.view.AbstractCheckableDialog;
import de.hnbk.arduapp.gui.view.CancelType;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class SerialReader extends Thread {

	private InputStream comPortStream;

	private Pattern valuePattern = Pattern.compile("\\d+\\.\\d*");

	private Logger logger = Logger.getLogger(SerialReader.class.getName());

	private CommPort commPort;

	public SerialReader() {
		super("Serial Read Thread");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					comPortStream.close();
					commPort.close();
				} catch (IOException e) {
					logger.log(Level.FATAL, "Fehler", e);
				}
			}
		});
	}

	void connect(CommPortIdentifier portIdentifier) throws IOException, PortInUseException, UnsupportedCommOperationException {
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			commPort = portIdentifier.open(this.getClass().getName(), 2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				comPortStream = serialPort.getInputStream();
			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	// public static void main(String[] args) {
	// SerialReader com = new SerialReader();
	// AppModel.getInstance().setClient(new Client());
	// com.start();
	// }

	@Override
	public void run() {
		// try {
		// AppModel.getInstance().setComPort(CommPortIdentifier.getPortIdentifier("/dev/ttyACM0"));
		// } catch (NoSuchPortException e2) {
		// e2.printStackTrace();
		// }
		if (AppModel.getInstance().getComPort() == null) {
			ComPortSelectionDialog dialog = new ComPortSelectionDialog(null);
			dialog.setVisible(true);
			AppModel.getInstance().setComPort(dialog.getCommPort());
		}
		try {
			connect(AppModel.getInstance().getComPort());
		} catch (IOException | UnsupportedCommOperationException e1) {
			logger.log(Level.FATAL, "Error while opening com port!",e1);
			return;
		} catch (PortInUseException e1) {
			JOptionPane.showMessageDialog(null,"Fehler" , "Der Com-Port wird bereits benutzt!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		byte[] buffer = new byte[1024];
		int len = -1;
		try {
			StringBuffer valueBuilder = new StringBuffer();
			while (true) {
				while ((len = comPortStream.read(buffer)) > -1) {
					String input = new String(buffer, 0, len);
					valueBuilder.append(input);
					String values = valueBuilder.toString();
					valueBuilder.setLength(0);
					if (values.contains("\r\n")) {
						int count = 0;
						for (String value : values.split("\r\n")) {
							if (valuePattern.matcher(value).matches()) {
								Measurement measurement = new Measurement();
								measurement.setMeasurementTime(Timestamp.from(Instant.now()));
								measurement.setClient(AppModel.getInstance().getClient());
								measurement.setValue(Double.valueOf(value));
								AppModel.getInstance().getMeasurements().add(measurement);
								count++;
							} else {
								valueBuilder = new StringBuffer();
								valueBuilder.append(value).append("\r\n");
							}
						}
						logger.log(Level.DEBUG, "Put " + count + " measurement/s into model.");
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						logger.log(Level.WARN, "Sleep was interrupted");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Simple wrapper class, because {@link CommPortIdentifier#toString()} is not
	 * properly overridden.
	 * 
	 * @author magnetotail
	 */
	private static class CommPortWrapper {
		private CommPortIdentifier identifier;

		public CommPortWrapper(CommPortIdentifier identifier) {
			this.identifier = identifier;
		}

		public CommPortIdentifier getIdentifier() {
			return identifier;
		}

		@Override
		public String toString() {
			return identifier.getName();
		}
	}

	@SuppressWarnings("serial")
	private static class ComPortSelectionDialog extends AbstractCheckableDialog {

		public ComPortSelectionDialog(JDialog owner) {
			super(owner, CancelType.NOT_CANCELLABLE);
			init();
		}

		private JComboBox<CommPortWrapper> portBox;

		private void init() {
			JPanel componentPanel = new JPanel();
			componentPanel.setLayout(new FlowLayout());
			JLabel commPortLabel = new JLabel("Port:");
			componentPanel.add(commPortLabel);
			portBox = new JComboBox<>();
			@SuppressWarnings("unchecked")
			Enumeration<CommPortIdentifier> identifiers = CommPortIdentifier.getPortIdentifiers();
			while (identifiers.hasMoreElements()) {
				portBox.addItem(new CommPortWrapper(identifiers.nextElement()));
			}
			componentPanel.add(portBox);
			addComponentPanel(componentPanel);
		}

		public CommPortIdentifier getCommPort() {
			return portBox.getItemAt(portBox.getSelectedIndex()).getIdentifier();
		}

		@Override
		protected boolean check() {
			return portBox.getSelectedItem() != null;
		}

	}

}
