package com.algebra.iot.server.dao.model;

import javax.persistence.*;

@Entity
public class Gateway {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Boolean sensorRunning;

    private Boolean plugRunning;

    public Gateway(){}

    public Gateway(String name) {
        this.name = name;
        this.sensorRunning =false;
        this.plugRunning=false;
    }

    public Boolean getPlugRunning() {
        return plugRunning;
    }

    public void setPlugRunning(Boolean plugRunning) {
        this.plugRunning = plugRunning;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSensorRunning() { return sensorRunning; }

    public void setSensorRunning(Boolean sensorRunning) {
        this.sensorRunning = sensorRunning;
    }

}
