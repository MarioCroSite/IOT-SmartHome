package com.algebra.iot.server.rest;

import com.algebra.iot.server.dao.model.Gateway;
import com.algebra.iot.server.dao.repo.GatewayRepository;
import com.algebra.iot.server.mqtt.MqttManager;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("api/gateway")
public class GatewayStateController {

    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    MqttManager mqttManager;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Gateway> getGateway(@PathVariable Integer id) {
        Optional<Gateway> gatewayOpt = gatewayRepository.findById(id);
        if(gatewayOpt.isPresent()){
            return new ResponseEntity<>(gatewayOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/all", produces = "application/json")
    public Iterable<Gateway> getAll() {
        return gatewayRepository.findAll();
    }


    @GetMapping(value = "/{id}/air/state/{isOn}")
    private ResponseEntity<Gateway> setAirSensorState(@PathVariable Integer id, @PathVariable boolean isOn){
        Optional<Gateway> gatewayOpt = gatewayRepository.findById(id);

        if(gatewayOpt.isPresent()){
            try{
                mqttManager.turnAirOnOff(isOn ? "1" : "0");
            }
            catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Gateway gateway = gatewayOpt.get();
            gateway.setSensorRunning(isOn);
            gatewayRepository.save(gateway);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}/plug/state/{isOn}")
    private ResponseEntity<Gateway> setPlugState(@PathVariable Integer id, @PathVariable boolean isOn){
        Optional<Gateway> gatewayOpt = gatewayRepository.findById(id);

        if(gatewayOpt.isPresent()){
            try{
                mqttManager.turnPlugOnOff(String.valueOf(isOn));
            }
            catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Gateway gateway = gatewayOpt.get();
            gateway.setSensorRunning(isOn);
            gatewayRepository.save(gateway);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}/plug/state")
    private ResponseEntity<Boolean> getPlugState(@PathVariable Integer id){
        Optional<Gateway> gatewayOpt = gatewayRepository.findById(id);

        if(gatewayOpt.isPresent()) {
            return new ResponseEntity<>(gatewayOpt.get().getPlugRunning(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}/air/state")
    private ResponseEntity<Boolean> getAirState(@PathVariable Integer id){
        Optional<Gateway> gatewayOpt = gatewayRepository.findById(id);

        if(gatewayOpt.isPresent()) {
            return new ResponseEntity<>(gatewayOpt.get().getSensorRunning(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
