package de.hnbk.arduapp;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;

import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import de.hnbk.arduapp.domain.classes.Room;
import de.hnbk.arduapp.domain.repositories.RoomRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = RoomRepository.class)
public class ArduApplication implements ApplicationRunner {
	

	@Autowired
	RoomRepository roomRepo;
	
	@Autowired
	ConfigurableApplicationContext context;
	
	public ArduApplication() {
	
	}

	public static void main(String[] args) throws Exception {
//		SpringApplication app = new SpringApplication(ArduApplication.class);
//		app.run(args);
		SpringApplicationBuilder builder = new SpringApplicationBuilder(ArduApplication.class);
		builder.headless(false);
		builder.run(args);
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {
		roomRepo.count();
		Room room = new Room();
		room.setDescription("HNN 203");
		roomRepo.save(room);
		System.out.println("Anzahl Räume: " + roomRepo.count());
		roomRepo.findAll().forEach(System.out::println);
		System.out.println("Started");
	}

}
