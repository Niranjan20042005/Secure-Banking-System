package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.demo.entity.Profile;
import com.demo.repository.ProfileRepository;
import com.demo.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/profile")
@CrossOrigin("*")
public class ProfileController {

    @Autowired
    ProfileRepository repo;

    @Autowired
    JwtUtil jwt;

    // ================= SAVE PROFILE =================
    @PostMapping("/save")
    public Map<String,String> save(@RequestBody Profile p,
                                   HttpServletRequest request){

        String token = request.getHeader("Authorization");

        if(token==null || !token.startsWith("Bearer "))
            return Map.of("message","Unauthorized");

        token = token.substring(7);
        String username = jwt.extractUsername(token);

        if(username==null)
            return Map.of("message","Invalid token");

        Profile existing = repo.findByUsername(username);

        if(existing!=null){
            existing.setName(p.getName());
            existing.setAge(p.getAge());
            existing.setPhone(p.getPhone());
            existing.setEmail(p.getEmail());
            existing.setAddress(p.getAddress());
            existing.setPhoto(p.getPhoto());
            repo.save(existing);
        }else{
            p.setUsername(username); // 🔥 set username from token
            repo.save(p);
        }

        return Map.of("message","Profile saved");
    }

    // ================= GET PROFILE =================
    @GetMapping("/get/{username}")
    public Object get(@PathVariable String username,
                      HttpServletRequest request){

        String token = request.getHeader("Authorization");

        if(token==null || !token.startsWith("Bearer "))
            return Map.of("message","Unauthorized");

        token = token.substring(7);

        String tokenUser = jwt.extractUsername(token);

        if(tokenUser==null || !tokenUser.equals(username))
            return Map.of("message","Invalid token");

        Profile p = repo.findByUsername(username);

        if(p==null)
            return Map.of("message","Profile not created");

        return p;
    }
}
