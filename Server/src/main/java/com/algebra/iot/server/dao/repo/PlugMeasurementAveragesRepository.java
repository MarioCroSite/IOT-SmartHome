package com.algebra.iot.server.dao.repo;

import com.algebra.iot.server.dao.model.PlugMeasurementAverages;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlugMeasurementAveragesRepository extends CrudRepository<PlugMeasurementAverages,Integer> {
    @Query(value="SELECT * from iot.plug_measurement_averages where iot.plug_measurement_averages.type= :type AND iot.plug_measurement_averages.date BETWEEN :from AND :to", nativeQuery = true)
    List<PlugMeasurementAverages> findByType(@Param("type") String type, @Param("from") Long from, @Param("to") Long to);
//    List<PlugMeasurementAverages> findFirst7ByType(String type);
//    List<PlugMeasurementAverages> findFirst30ByType(String type);
}
