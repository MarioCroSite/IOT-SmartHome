package com.algebra.iot.server.dao.repo;

import com.algebra.iot.server.dao.model.AirMeasurementAverages;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AirMeasurementAveragesRepository extends CrudRepository<AirMeasurementAverages,Integer> {
    @Query(value = "SELECT * from iot.air_measurement_averages where iot.air_measurement_averages.type= :type AND iot.air_measurement_averages.date BETWEEN :from AND :to", nativeQuery = true)
    List<AirMeasurementAverages> findByType(@Param("type") String type, @Param("from") Long from, @Param("to") Long to);
//    List<AirMeasurementAverages> findFirst7ByType(String type);
//    List<AirMeasurementAverages> findFirst30ByType(String type);
}
