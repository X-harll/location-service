package com.tecvinson.location.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Countries")
public class Country extends CommonFields {

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false,unique = true)
    private String countryCode;

    @Column(nullable = false,unique = true)
    private String phoneCode;

    private String flag;

    @ManyToOne
    @JoinColumn(name = "continent_id")
    private Continent continent;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }
}
