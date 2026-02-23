package com.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.demo.entity.Profile;
import com.demo.repository.ProfileRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    ProfileRepository repo;

    // SAVE OR UPDATE PROFILE
    @PostMapping("/save")
    public Map<String,String> saveProfile(@RequestBody Profile profile){

        Map<String,String> res = new HashMap<>();

        Profile existing = repo.findByUsername(profile.getUsername());

        if(existing!=null){
            existing.setName(profile.getName());
            existing.setAge(profile.getAge());
            existing.setPhone(profile.getPhone());
            existing.setEmail(profile.getEmail());
            existing.setAddress(profile.getAddress());
            existing.setPhoto(profile.getPhoto());
            repo.save(existing);
        }else{
            repo.save(profile);
        }

        res.put("status","success");
        return res;
    }

    // GET PROFILE FOR DASHBOARD
    @GetMapping("/get/{username}")
    public Profile getProfile(@PathVariable String username){
        return repo.findByUsername(username);
    }
}
