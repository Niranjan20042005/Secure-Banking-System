package com.demo.entity;

import jakarta.persistence.*;

@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}