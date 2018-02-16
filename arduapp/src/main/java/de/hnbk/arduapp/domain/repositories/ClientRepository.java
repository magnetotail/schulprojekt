package de.hnbk.arduapp.domain.repositories;

import org.springframework.data.repository.CrudRepository;

import de.hnbk.arduapp.domain.classes.Client;

public interface ClientRepository extends CrudRepository<Client, Integer> {
	
	Client findClientByClientMac(String clientMac);

}
