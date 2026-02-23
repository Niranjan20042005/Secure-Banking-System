package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    // store otp temporary
    private Map<String,String> otpStore = new HashMap<>();

    // send otp
    public String sendOtp(String email){

        String otp = String.valueOf(new Random().nextInt(900000)+100000);
        otpStore.put(email, otp);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Secure Bank Transfer OTP");
        msg.setText("Your OTP is: "+otp);

        mailSender.send(msg);

        return "OTP sent to email";
    }

    // verify otp
    public boolean verifyOtp(String email,String otp){
        String stored = otpStore.get(email);
        return stored!=null && stored.equals(otp);
    }
}
