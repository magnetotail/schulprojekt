package de.hnbk.arduapp.domain.repositories;

import org.springframework.data.repository.CrudRepository;

import de.hnbk.arduapp.domain.classes.Measurement;

public interface MeasurementRepository extends CrudRepository<Measurement, Integer>{

}
