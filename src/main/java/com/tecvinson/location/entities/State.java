package com.tecvinson.location.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "States")
public class State extends CommonFields {

    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
