package de.hnbk.arduapp.domain.repositories;

import org.springframework.data.repository.CrudRepository;

import de.hnbk.arduapp.domain.classes.MeasurementType;

public interface MeasurementTypeRepository extends CrudRepository<MeasurementType, Integer> {

}
