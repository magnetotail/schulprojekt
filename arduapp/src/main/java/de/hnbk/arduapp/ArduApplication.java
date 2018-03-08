package de.hnbk.arduapp;

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
//		SpringApplication app = new SpringApplication(ArduApplication.class);
//		app.run(args);
		System.out.println(UIManager.getSystemLookAndFeelClassName());
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		System.out.println(void.class.getName());
		splashscreen = new JFrame("SPLASH");
		JLabel splushlabel = new JLabel("SPLUSH");
		splashscreen.add(splushlabel);
		splashscreen.setVisible(true);
		SpringApplicationBuilder builder = new SpringApplicationBuilder(ArduApplication.class);
		builder.headless(false);
		builder.build().run(args);
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {
	}

	
	public static void closeSplashscreen() {
		splashscreen.setVisible(false);
		splashscreen.dispose();
	}
}
