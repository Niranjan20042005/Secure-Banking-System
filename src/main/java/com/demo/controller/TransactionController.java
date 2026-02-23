package com.demo.controller;

import com.demo.entity.Transaction;
import com.demo.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionRepo repo;

    // GET ALL TRANSACTIONS
    @GetMapping("/transactions")
    public List<Transaction> getTransactions(){
        return repo.findAll();
    }
}


