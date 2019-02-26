package com.algebra.iot.server.dao.repo;

import com.algebra.iot.server.dao.model.AirMeasurement;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface AirMeasurementRepository extends CrudRepository<AirMeasurement,Integer> {
    AirMeasurement findFirstByOrderByIdDesc();
    Set<AirMeasurement> findByDateBetween(Long from, Long to);

}
