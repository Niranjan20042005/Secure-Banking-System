package com.demo.entity;

import jakarta.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fromAcc;
    private String toAcc;
    private double amount;
    private String type;
    private String username;
    private String date;
    private String accountNumber;

    private String sender;
    private String receiver;
    
    

    
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	// ===== ID =====
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // ===== FROM ACC =====
    public String getFromAcc() {
        return fromAcc;
    }
    public void setFromAcc(String fromAcc) {
        this.fromAcc = fromAcc;
    }

    // ===== TO ACC =====
    public String getToAcc() {
        return toAcc;
    }
    public void setToAcc(String toAcc) {
        this.toAcc = toAcc;
    }

    // ===== AMOUNT =====
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    // ===== TYPE =====
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    // ===== USERNAME =====
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    // ===== DATE =====
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    // ===== SENDER =====
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

    // ===== RECEIVER =====
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
