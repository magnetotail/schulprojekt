package de.hnbk.arduapp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import de.hnbk.arduapp.GetNetworkAddress.AddressType;
import de.hnbk.arduapp.domain.repositories.RoomRepository;

@Controller
public class MainController {

	AppModel model;
	
	MainFrame frame;
	
	@Autowired
	RoomRepository roomRepo;
	
	@Autowired
	ConfigurableApplicationContext context;
	
	public MainController(){
		System.out.println("FOOOOOOOOOOOOOOOO!!!!!!!!!!!!!!");
		frame = new MainFrame();
		model = new AppModel();
		initFrame();

		System.out.println(GetNetworkAddress.GetAddress(AddressType.MAC));
		
		frame.setVisible(true);
	}

	private void initFrame() {
		frame.getTestButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				roomRepo.findAll().forEach(System.out::println);
			}
		});
		
		
	}
	
	
	
	
	
}
