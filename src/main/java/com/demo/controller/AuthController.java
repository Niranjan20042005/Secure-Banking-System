package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.demo.entity.User;
import com.demo.repository.UserRepo;
import com.demo.util.JwtUtil;
import com.demo.service.EmailService;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserRepo repo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtUtil jwt;

    @Autowired
    private EmailService mail;

    // OTP storage
    private Map<String,Integer> otpStore = new ConcurrentHashMap<>();
    private Map<String,Long> otpExpiry = new ConcurrentHashMap<>();
    private Map<String,Integer> otpAttempts = new ConcurrentHashMap<>();


    // ================= SIGNUP =================
    @PostMapping("/signup")
    public Object signup(@RequestBody User user){

        if(user.getUsername()==null || user.getPassword()==null || user.getEmail()==null)
            return Map.of("message","Username, email & password required");

        if(repo.findByUsername(user.getUsername()).isPresent())
            return Map.of("message","Username already exists");

        if(repo.findByEmail(user.getEmail()).isPresent())
            return Map.of("message","Email already registered");

        user.setPassword(encoder.encode(user.getPassword()));
        user.setAttempts(0);
        user.setLocked(false);
        user.setBalance(1000);

        repo.save(user);

        return Map.of("message","Signup successful");
    }




    // ================= LOGIN WITH USERNAME =================
    @PostMapping("/login")
    public Object login(@RequestBody User u){

        Map<String,Object> res = new HashMap<>();

        if(u.getUsername()==null || u.getPassword()==null){
            res.put("message","Enter username & password");
            return res;
        }

        Optional<User> op = repo.findByUsername(u.getUsername());

        if(op.isEmpty()){
            res.put("message","User not found");
            return res;
        }

        User user = op.get();

        if(user.isLocked()){
            res.put("message","Account locked");
            return res;
        }

        if(!encoder.matches(u.getPassword(), user.getPassword())){

            user.setAttempts(user.getAttempts()+1);

            if(user.getAttempts()>=3){
                user.setLocked(true);
            }

            repo.save(user);
            res.put("message","Wrong password");
            return res;
        }

        user.setAttempts(0);
        repo.save(user);

        // OTP generate
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);

        otpStore.put(user.getUsername(), otp);
        otpExpiry.put(user.getUsername(), System.currentTimeMillis()+300000);
        otpAttempts.put(user.getUsername(),0);

        try{
            mail.sendOtp(user.getEmail(), otp); // send to email
        }catch(Exception e){
            res.put("message","Email sending failed");
            return res;
        }

        res.put("message","OTP sent to email");
        res.put("username", user.getUsername()); // 🔥 IMPORTANT
        return res;
    }



    // ================= VERIFY OTP =================
    @PostMapping("/verify")
    public Object verify(@RequestParam String username, @RequestParam int otp){

        if(!otpStore.containsKey(username))
            return Map.of("message","Please login first");

        long exp = otpExpiry.getOrDefault(username,0L);
        if(System.currentTimeMillis() > exp){

            otpStore.remove(username);
            otpExpiry.remove(username);
            otpAttempts.remove(username);

            return Map.of("message","OTP expired. Login again");
        }

        int tries = otpAttempts.getOrDefault(username,0);
        if(tries>=3){

            otpStore.remove(username);
            otpExpiry.remove(username);
            otpAttempts.remove(username);

            return Map.of("message","Too many wrong OTP attempts");
        }

        int storedOtp = otpStore.get(username);

        if(storedOtp == otp){

            String token = jwt.generateToken(username);

            otpStore.remove(username);
            otpExpiry.remove(username);
            otpAttempts.remove(username);

            return Map.of(
                    "message","Login successful",
                    "token",token,
                    "username",username   // 🔥 RETURN USERNAME
            );
        }else{
            otpAttempts.put(username, tries+1);
            return Map.of("message","Invalid OTP");
        }
    }



    // ================= RESEND OTP =================
    @PostMapping("/resend")
    public Map<String,String> resendOTP(@RequestParam String username){

        Map<String,String> res = new HashMap<>();

        Optional<User> op = repo.findByUsername(username);

        if(op.isEmpty()){
            res.put("message","User not found");
            return res;
        }

        User user = op.get();

        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);

        otpStore.put(username, otp);
        otpExpiry.put(username, System.currentTimeMillis()+300000);
        otpAttempts.put(username, 0);

        try{
            mail.sendOtp(user.getEmail(), otp);
        }catch(Exception e){
            res.put("message","Email send failed");
            return res;
        }

        res.put("message","New OTP sent");
        return res;
    }

}
