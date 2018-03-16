package de.hnbk.arduapp.domain.repositories;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import de.hnbk.arduapp.domain.classes.MeasurementType;

public interface MeasurementTypeRepository extends CrudRepository<MeasurementType, Integer> {

	@Override
	Iterable<MeasurementType> findAll();
}
