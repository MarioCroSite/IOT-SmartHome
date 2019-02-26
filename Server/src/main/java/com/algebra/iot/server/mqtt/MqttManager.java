package com.algebra.iot.server.mqtt;

import com.algebra.iot.server.dao.model.AirMeasurement;
import com.algebra.iot.server.dao.model.Gateway;
import com.algebra.iot.server.dao.model.PlugMeasurement;
import com.algebra.iot.server.dao.repo.AirMeasurementRepository;
import com.algebra.iot.server.dao.repo.GatewayRepository;
import com.algebra.iot.server.dao.repo.PlugMeasurementRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;

@Service
public class MqttManager implements MqttCallback {

//    @Value("${mqtt.airSensorState}")
    private String airSensorState ="air_state";

    private String plugStateTopic ="plug_state_switch";

    private String plugStateReportTopic ="plug_state_report";

//    @Value("${mqtt.airDataTopic}")
    private String plugDataTopic ="plug";

    private String airDataTopic ="air";

//    @Value("${mqtt.broker}")
    private String broker="tcp://localhost:443";

    @Autowired
    AirMeasurementRepository airMeasurementRepository;

    @Autowired
    PlugMeasurementRepository plugMeasurementRepository;

    @Autowired
    GatewayRepository gatewayRepository;

    private IMqttClient client = new MqttClient(broker,"IotMqttClient");

    private ObjectMapper objectMapper=new ObjectMapper();

    public MqttManager() throws MqttException {

    }

    @PostConstruct
    public void init() throws MqttException {
        connect();
    }

    private void connect() throws MqttException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

        client.setCallback(this);
        client.connect(getOptions());
        client.subscribe(airDataTopic);
        client.subscribe(plugDataTopic);
        client.subscribe(plugStateReportTopic);
    }

    public void turnAirOnOff(String value) throws MqttException {
        byte[] payload = value.getBytes();
        MqttMessage msg=new MqttMessage(payload);
        msg.setQos(0);
        msg.setRetained(true);
        client.publish(airSensorState,msg);
    }

    public void turnPlugOnOff(String value) throws MqttException {
        byte[] payload = value.getBytes();
        MqttMessage msg=new MqttMessage(payload);
        msg.setQos(0);
        msg.setRetained(true);
        client.publish(plugStateTopic,msg);
    }

    private MqttConnectOptions getOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        return options;
    }

    @Override
    public void connectionLost(Throwable throwable) {
            throwable.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        System.out.println(new String(mqttMessage.getPayload()));
        if(topic.equals(airDataTopic)){
            saveAirData(new String(mqttMessage.getPayload()));
        }
        else if(topic.equals(plugDataTopic)){
            savePlugData(new String(mqttMessage.getPayload()));
        }
        else if(topic.equals(plugStateReportTopic)){
            savePlugStatus(new Boolean(new String(mqttMessage.getPayload())));
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    @Transactional
    private void saveAirData(String payload) throws IOException {
        AirMeasurement airMeasurement =objectMapper.readValue(payload, AirMeasurement.class);
        airMeasurement.setDate(new Date().getTime());
        airMeasurementRepository.save(airMeasurement);
    }

    @Transactional
    private void savePlugData(String payload) {
        PlugMeasurement m = new PlugMeasurement(Double.parseDouble(payload),new Date().getTime(),null);
        plugMeasurementRepository.save(m);
    }

    @Transactional
    private void savePlugStatus(Boolean status) {
        Gateway gateway=gatewayRepository.findFirstByOrderByIdDesc();
        gateway.setPlugRunning(status);
        gatewayRepository.save(gateway);
    }
}
