package com.algebra.iot.server.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
//@JsonIgnoreProperties(value={ "bookName", "bookCategory" }, allowGetters= true)
public class AirMeasurementAverages {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
  //  @JsonIgnore
    private Integer id;

    private Double averageCO2;
    private Double averageTemperature;
    private Double averageRelativeHumidity;

    @JsonIgnore
    private String type;

    private Long date;

    @JsonIgnore
    private Integer gatewayId;

    public AirMeasurementAverages() {

    }

    public AirMeasurementAverages(Double CO2, Double temperature, Double relativeHumidity, String type, Long date, Integer gatewayId) {
        this.averageCO2 = CO2;
        this.averageTemperature = temperature;
        this.averageRelativeHumidity = relativeHumidity;
        this.type=type;
        this.date = date;
        this.gatewayId = gatewayId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAverageCO2() {
        return averageCO2;
    }

    public void setAverageCO2(Double averageCO2) {
        this.averageCO2 = averageCO2;
    }

    public Double getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(Double averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public Double getAverageRelativeHumidity() {
        return averageRelativeHumidity;
    }

    public void setAverageRelativeHumidity(Double averageRelativeHumidity) {
        this.averageRelativeHumidity = averageRelativeHumidity;
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
