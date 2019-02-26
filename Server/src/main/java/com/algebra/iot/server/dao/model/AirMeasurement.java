package com.algebra.iot.server.dao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class AirMeasurement {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("CO2")
    private Double CO2;

    @JsonProperty("temperature")
    private Double temperature;

    @JsonProperty("relativeHumidity")
    private Double relativeHumidity;

    private Long date;

    private Integer gatewayId;

    public AirMeasurement(){}

    public AirMeasurement(Double CO2, Double temparature, Double relativeHumidity, Long date, Integer gatewayId) {
        this.CO2 = CO2;
        this.temperature = temparature;
        this.relativeHumidity = relativeHumidity;
        this.date = date;
        this.gatewayId = gatewayId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getCO2() {
        return CO2;
    }

    public void setCO2(Double CO2) {
        this.CO2 = CO2;
    }

    public Double getTemparature() {
        return temperature;
    }

    public void setTemparature(Double temparature) {
        this.temperature = temparature;
    }

    public Double getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(Double relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Integer getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(Integer gatewayId) {
        this.gatewayId = gatewayId;
    }
}
