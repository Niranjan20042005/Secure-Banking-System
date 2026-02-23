package com.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.demo.repository.AccountRepo;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private AccountRepo accountRepo;

    @GetMapping("/total-accounts")
    public Map<String,Object> getTotalAccounts(){

        long totalAccounts = accountRepo.count();
        Double totalBalance = accountRepo.getTotalBalance();

        if(totalBalance == null)
            totalBalance = 0.0;

        Map<String,Object> map = new HashMap<>();
        map.put("totalAccounts", totalAccounts);
        map.put("totalBalance", totalBalance);

        return map;
    }
}