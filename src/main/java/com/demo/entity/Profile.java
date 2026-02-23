package com.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name="profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String name;
    private String age;
    private String phone;
    private String email;
    private String address;

    @Column(columnDefinition="LONGTEXT")
    private String photo;

    public int getId(){return id;}
    public void setId(int id){this.id=id;}

    public String getUsername(){return username;}
    public void setUsername(String username){this.username=username;}

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public String getAge(){return age;}
    public void setAge(String age){this.age=age;}

    public String getPhone(){return phone;}
    public void setPhone(String phone){this.phone=phone;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}

    public String getAddress(){return address;}
    public void setAddress(String address){this.address=address;}

    public String getPhoto(){return photo;}
    public void setPhoto(String photo){this.photo=photo;}
}