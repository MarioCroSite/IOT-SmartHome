package com.algebra.iot.server.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class PlugMeasurementAverages {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Double averagePower;

    private Long date;

    @JsonIgnore
    private String type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Gateway gateway;

    public PlugMeasurementAverages(){}

    public PlugMeasurementAverages(Double hourlyPower, Long date, String type, Gateway gateway) {
        this.averagePower = hourlyPower;
        this.date = date;
        this.type=type;
        this.gateway = gateway;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAveragePower() {
        return averagePower;
    }

    public void setAveragePower(Double averagePower) {
        this.averagePower = averagePower;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }
}
