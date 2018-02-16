package de.hnbk.arduapp.domain.repositories;

import org.springframework.data.repository.CrudRepository;

import de.hnbk.arduapp.domain.classes.Location;
import de.hnbk.arduapp.domain.classes.Room;

public interface RoomRepository extends CrudRepository<Room, Integer>{
	
	public Room findRoomByDescriptionAndLocation(String description, Location location);

}
