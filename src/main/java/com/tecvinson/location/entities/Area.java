package com.tecvinson.location.entities;

import jakarta.persistence.*;



@Entity
@Table(name = "Areas")
public class Area extends CommonFields{

    private String name;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

}
