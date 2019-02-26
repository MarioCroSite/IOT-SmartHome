package com.algebra.iot.server.dao.model;

import javax.persistence.*;

@Entity
public class PlugMeasurement {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Double power;

    private Long date;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Gateway gateway;

    public PlugMeasurement() {
    }

    public PlugMeasurement(Double power, Long date, Gateway gateway) {
        this.power = power;
        this.date = date;
        this.gateway = gateway;
    }

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
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
