package de.hnbk.arduapp.domain.repositories;

import org.springframework.data.repository.CrudRepository;

import de.hnbk.arduapp.domain.classes.Location;

public interface LocationRepository extends CrudRepository<Location, Integer> {

}
