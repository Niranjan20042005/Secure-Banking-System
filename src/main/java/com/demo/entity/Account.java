package com.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name="account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;
    @Column(name="account_no")
    private String accountNo;
    private String customer_id;
	private String ifsc;
    private String type;
    
    @Column(name="balance")
    private double balance;
    private String otp;
    private String password;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	// getters setters
    public int getId(){ return id; }
    public void setId(int id){ this.id=id; }

    public String getName(){ return name; }
    public void setName(String name){ this.name=name; }

    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email=email; }

    public String getAccountNo(){ return accountNo; }
    public void setAccountNo(String accountNo){ this.accountNo=accountNo; }

    public String getIfsc(){ return ifsc; }
    public void setIfsc(String ifsc){ this.ifsc=ifsc; }

    public String getType(){ return type; }
    public void setType(String type){ this.type=type; }

    public double getBalance(){ return balance; }
    public void setBalance(double balance){ this.balance=balance; }
    
    public String getCustomer() {
		return customer_id;
	}
	public void setCustomer(String customer_id) {
		this.customer_id = customer_id;
	}
}