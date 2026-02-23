package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.demo.service.OtpService;
import java.util.*;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/bank")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class OtpController {

    @Autowired
    private OtpService otpService;

    // 🟢 SEND OTP
    @PostMapping("/send-otp")
    public Map<String,String> sendOtp(@RequestBody Map<String,String> req, HttpSession session){

        String email = req.get("email");
        String msg = otpService.sendOtp(email);

        // store email for verification
        session.setAttribute("otpEmail", email);

        // ⭐ keep session 30 minutes
        session.setMaxInactiveInterval(30 * 60);

        return Map.of("message", msg);
    }

    // 🟢 VERIFY OTP
    @PostMapping("/verify-otp")
    public Map<String,String> verifyOtp(@RequestBody Map<String,String> req, HttpSession session){

        String email = req.get("email");
        String otp = req.get("otp");

        boolean valid = otpService.verifyOtp(email, otp);

        if(valid){

            // ⭐ LOGIN SESSION CREATED HERE
            session.setAttribute("user", email);

            // ⭐ keep session 30 minutes
            session.setMaxInactiveInterval(30 * 60);

            return Map.of("message","OTP verified success");
        }
        else{
            return Map.of("message","Invalid OTP");
        }
    }
}


