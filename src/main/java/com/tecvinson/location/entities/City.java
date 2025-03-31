package com.tecvinson.location.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Cities")
public class City extends CommonFields{

    private String name;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
