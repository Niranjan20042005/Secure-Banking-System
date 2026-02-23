package com.demo.entity;

import jakarta.persistence.*;

@Entity
public class User {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int id;

 private String username;   // 🔥 ADD THIS
 private String name;
 private String email;
 private String password;
 private double balance;
 private int attempts;
 private boolean locked;

 // getters setters

 public int getId(){return id;}
 public void setId(int id){this.id=id;}

 public String getUsername(){return username;}
 public void setUsername(String username){this.username=username;}

 public String getName(){return name;}
 public void setName(String name){this.name=name;}

 public String getEmail(){return email;}
 public void setEmail(String email){this.email=email;}

 public String getPassword(){return password;}
 public void setPassword(String password){this.password=password;}

 public double getBalance(){return balance;}
 public void setBalance(double balance){this.balance=balance;}

 public int getAttempts(){return attempts;}
 public void setAttempts(int attempts){this.attempts=attempts;}

 public boolean isLocked(){return locked;}
 public void setLocked(boolean locked){this.locked=locked;}
}