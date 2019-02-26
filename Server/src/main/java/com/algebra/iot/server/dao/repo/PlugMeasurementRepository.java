package com.algebra.iot.server.dao.repo;

import com.algebra.iot.server.dao.model.PlugMeasurement;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface PlugMeasurementRepository extends CrudRepository<PlugMeasurement,Integer> {

    PlugMeasurement findFirstByOrderByIdDesc();
    Set<PlugMeasurement> findByDateBetween(Long from, Long to);
}
