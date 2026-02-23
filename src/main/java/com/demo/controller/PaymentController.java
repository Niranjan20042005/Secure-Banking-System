package com.demo.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import com.demo.entity.Account;
import com.demo.repository.AccountRepo;
import com.demo.util.OtpStore;

@RestController
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AccountRepo accountRepo;


    // ================= SEND OTP =================
    @PostMapping("/send-qr-otp")
    public String sendQrOtp(@RequestParam String account){

        try{
            Account acc = accountRepo.findByAccountNo(account);

            if(acc == null) return "Account not found";

            String otp = String.valueOf(new Random().nextInt(9000)+1000);

            OtpStore.store.put(account, otp);

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(acc.getEmail());
            msg.setSubject("QR OTP");
            msg.setText("Your OTP: "+otp);
            mailSender.send(msg);

            System.out.println("OTP SENT = "+otp);

            return "OTP SENT";

        }catch(Exception e){
            e.printStackTrace();
            return "MAIL ERROR";
        }
    }


    // ================= VERIFY OTP =================
    @PostMapping("/verify-qr-otp")
    public String verifyQr(@RequestParam String account,
                           @RequestParam String otp){

        String storedOtp = OtpStore.store.get(account);

        System.out.println("ACCOUNT = "+account);
        System.out.println("ENTERED OTP = "+otp);
        System.out.println("STORED OTP = "+storedOtp);

        if(storedOtp == null)
            return "OTP expired";

        if(!storedOtp.equals(otp))
            return "Wrong OTP";

        OtpStore.store.remove(account);

        return "QR Verified";
    }
}
