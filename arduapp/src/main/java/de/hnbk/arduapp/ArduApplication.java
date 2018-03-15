package de.hnbk.arduapp;

import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import de.hnbk.arduapp.domain.repositories.MeasurementTypeRepository;
import de.hnbk.arduapp.domain.repositories.RoomRepository;
import de.hnbk.arduapp.gui.AppModel;
import de.hnbk.arduapp.gui.GuiUtils;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = RoomRepository.class)
public class ArduApplication implements ApplicationRunner {

	private static JFrame splashscreen;

	@Autowired
	RoomRepository roomRepo;

	@Autowired
	MeasurementTypeRepository measurementTypeRepo;

	@Autowired
	ConfigurableApplicationContext context;

	public ArduApplication() {

	}

	public static void main(String[] args) throws Exception {
		// SpringApplication app = new SpringApplication(ArduApplication.class);
		// app.run(args);
		System.out.println(UIManager.getSystemLookAndFeelClassName());
		openSplashscreen();
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SpringApplicationBuilder builder = new SpringApplicationBuilder(ArduApplication.class);
		builder.headless(false);
		builder.build().run(args);
	}

	private static void openSplashscreen() {
		splashscreen = new JFrame("SPLASH");
		splashscreen.setUndecorated(true);
		splashscreen.setSize(500, 300);
		JLabel splushlabel = new JLabel("SPLUSH");
		splashscreen.add(splushlabel);
		GuiUtils.centerWindow(splashscreen);
		splashscreen.setVisible(true);
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
	}

	/**
	 * Closes and disposes the splashscreen.
	 * Multiple invocations have no effect
	 */
	public static void closeSplashscreen() {
		if (splashscreen.isVisible()) {
			splashscreen.setVisible(false);
			splashscreen.dispose();
		}
	}
}
