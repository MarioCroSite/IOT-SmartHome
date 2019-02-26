package com.algebra.iot.server.rest;

import com.algebra.iot.server.dao.model.*;
import com.algebra.iot.server.dao.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/measurements")
public class GatewayMeasurementsController {

    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    AirMeasurementRepository airMeasurementRepository;

    @Autowired
    PlugMeasurementRepository plugMeasurementRepository;


    @Autowired
    AirMeasurementAveragesRepository airMeasurementAveragesRepository;

    @Autowired
    PlugMeasurementAveragesRepository plugMeasurementAveragesRepository;


    @GetMapping(value="/latest",produces = "application/json")
    public ResponseEntity<Map<String, Object>> getLatestMeasurements() {
        Map<String, Object> response = new HashMap<>();

        Optional<Gateway> gateway = gatewayRepository.findById(1);

        if(gateway.isPresent()){
            Map<String, Object> air = new HashMap<>();
            air.put("data", airMeasurementRepository.findFirstByOrderByIdDesc());
            air.put("state", gateway.get().getSensorRunning());

            Map<String, Object> plug = new HashMap<>();
            plug.put("data", plugMeasurementRepository.findFirstByOrderByIdDesc());
            plug.put("state", gateway.get().getPlugRunning());

            response.put("air", air);
            response.put("plug", plug);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(value="/averages", produces = "application/json")
    public Map<String, Object> getMeasurementAverages() {

        Map<String, List<PlugMeasurementAverages>> allPlugMeasurements = new HashMap<>();
        allPlugMeasurements.put("weekly", plugMeasurementAveragesRepository.findByType("DAILY",new Date().getTime()-608000000, new Date().getTime()));
        allPlugMeasurements.put("daily", plugMeasurementAveragesRepository.findByType("HOURLY",new Date().getTime()-88000000 , new Date().getTime()));
        allPlugMeasurements.put("monthly", plugMeasurementAveragesRepository.findByType("DAILY",new Date().getTime()-2639743000L , new Date().getTime()));

        Map<String, List<AirMeasurementAverages>> allAirMeasurements = new HashMap<>();
        allAirMeasurements.put("weekly", airMeasurementAveragesRepository.findByType("DAILY",new Date().getTime()-608000000, new Date().getTime()));
        allAirMeasurements.put("daily", airMeasurementAveragesRepository.findByType("HOURLY",new Date().getTime()-88000000, new Date().getTime()));
        allAirMeasurements.put("monthly", airMeasurementAveragesRepository.findByType("DAILY",new Date().getTime()-2639743000L , new Date().getTime()));

        Map<String, Object> returnObj = new HashMap<>();
        returnObj.put("air", allAirMeasurements);
        returnObj.put("plug", allPlugMeasurements);
        return returnObj;
    }

}
