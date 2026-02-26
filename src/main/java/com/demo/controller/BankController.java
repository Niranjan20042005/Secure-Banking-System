package com.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.demo.entity.Account;
import com.demo.entity.Transaction;
import com.demo.entity.User;
import com.demo.repository.AccountRepo;
import com.demo.repository.AdminRepo;
import com.demo.repository.TransactionRepo;
import com.demo.repository.UserRepo;
import com.demo.util.AESUtil;
import com.demo.util.OtpStore;

@RestController
@RequestMapping("/bank")
@CrossOrigin(origins="*")
public class BankController {
	
	
	// ✅ OTP MEMORY STORE (ONLY ONCE)
    private  Map<String,String> otpStore = new HashMap<>();

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private TransactionRepo tRepo;

    @Autowired
    private JavaMailSender mailSender;
    
    
    @Autowired
    private AdminRepo adminRepo;
    
    @Autowired
    private UserRepo userRepo;
    
    
    
    
    
 // admin otp store
    private String adminOtp = "";
    private String adminEmailSession = "";


    // ================= CREATE ACCOUNT =================
    @PostMapping("/createAccount")
    public Map<String,Object> createAccount(@RequestBody Map<String,Object> req){

        Map<String,Object> res=new HashMap<>();

        try{
            String name=req.get("name").toString();
            String email=req.get("email").toString();
            String type=req.get("type").toString();
            String accountNo=req.get("accountNo").toString();
            String ifsc=req.get("ifsc").toString();
            double balance=Double.parseDouble(req.get("balance").toString());

            Account acc=new Account();
            acc.setName(name);
            acc.setEmail(email);
            acc.setAccountNo(accountNo);
            acc.setIfsc(ifsc);
            acc.setType(type);
            acc.setBalance(balance);

            Account saved=accountRepo.save(acc);

            res.put("status","success");
            res.put("accountNo",saved.getAccountNo());
            res.put("customerId","CUST"+saved.getId());
            res.put("ifsc",saved.getIfsc());

        }catch(Exception e){
            res.put("status","error");
            res.put("message","Database error");
        }

        return res;
    }

    // ================= MY ACCOUNT =================
    @GetMapping("/myaccount/{email}")
    public Map<String,Object> myAccount(@PathVariable String email){

        Map<String,Object> res=new HashMap<>();

        try{
            Account acc = accountRepo.findByEmail(email);

            if(acc==null){
                res.put("status","fail");
                res.put("message","Account not found");
                return res;
            }

            res.put("status","success");
            res.put("name",acc.getName());
            res.put("email",acc.getEmail());
            res.put("accountNo",acc.getAccountNo());
            res.put("ifsc",acc.getIfsc());
            res.put("type",acc.getType());
            res.put("balance",acc.getBalance());
            res.put("customerId","CUST"+acc.getId());

        }catch(Exception e){
            res.put("status","error");
            res.put("message","Server error");
        }

        return res;
    }

    // ================= BALANCE =================
    @GetMapping("/balance/{accountNo}")
    public Map<String,Object> balance(@PathVariable String accountNo){

        Map<String,Object> res=new HashMap<>();

        Account acc=accountRepo.findByAccountNo(accountNo);

        if(acc==null){
            res.put("status","fail");
            res.put("message","Account not found");
            return res;
        }

        res.put("status","success");
        res.put("balance",acc.getBalance());
        res.put("name",acc.getName());

        return res;
    }
    
    // ================= MEMORY OTP =================
    @GetMapping("/sendOtp/{email}")
    public String sendOtp(@PathVariable String email){

        String otp = String.valueOf(new Random().nextInt(900000)+100000);
        otpStore.put(email, otp);

        System.out.println("========== DEBUG START ==========");
        System.out.println("Sending OTP to: " + email);
        System.out.println("Generated OTP: " + otp);

        try{
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("Your Bank OTP");
            msg.setText("Your OTP is: "+otp);

            mailSender.send(msg);

            System.out.println("✅ MAIL SENT SUCCESSFULLY");

        }catch(Exception e){

            System.out.println("❌ MAIL FAILED");
            e.printStackTrace();              // 🔥 VERY IMPORTANT
            System.out.println("ERROR MSG: " + e.getMessage());
        }

        System.out.println("========== DEBUG END ==========");

        return "OTP SENT";
    }

    @GetMapping("/verifyOtp/{email}/{otp}")
    public String verifyOtp(@PathVariable String email,@PathVariable String otp){

        String storedOtp = otpStore.get(email);

        if(storedOtp!=null && storedOtp.equals(otp)){
            otpStore.remove(email);
            return "VERIFIED";
        }else{
            return "WRONG";
        }
    }

    // ================= DATABASE OTP =================
    @GetMapping("/sendDbOtp/{email}")
    public Map<String,String> sendDbOtp(@PathVariable String email){

        Map<String,String> map = new HashMap<>();

        Account acc = accountRepo.findByEmail(email);

        if(acc==null){
            map.put("status","fail");
            return map;
        }

        int otp = (int)(Math.random()*900000)+100000;
        acc.setOtp(String.valueOf(otp));
        accountRepo.save(acc);

        SimpleMailMessage msg=new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Bank OTP Login");
        msg.setText("Your OTP is: "+otp);
        mailSender.send(msg);

        map.put("status","success");
        return map;
    }

    @GetMapping("/verifyDbOtp/{email}/{otp}")
    public Map<String,String> verifyDbOtp(@PathVariable String email,@PathVariable String otp){

        Map<String,String> map=new HashMap<>();
        Account acc = accountRepo.findByEmail(email);

        if(acc!=null && acc.getOtp()!=null && acc.getOtp().equals(otp)){
            map.put("status","success");
        }else{
            map.put("status","fail");
        }

        return map;
    }
    
    
    
    
    @GetMapping("/bank/balance/{accno}")
    public Map<String,Object> getBalance(@PathVariable Integer accno){

        Map<String,Object> map = new HashMap<>();

        Account acc = accountRepo.findById(accno).orElse(null);

        if(acc==null){
            map.put("status","error");
            map.put("message","Account not found");
        }else{
            map.put("status","success");
            map.put("balance",acc.getBalance());
        }

        return map;
    }

    
    // SendTransferOtp
    
    @GetMapping("/sendTransferOtp/{sender}/{receiver}/{amount}")
    public Map<String,String> sendTransferOtp(
    @PathVariable String sender,
    @PathVariable String receiver,
    @PathVariable double amount){

    Map<String,String> res = new HashMap<>();

    Account s = accountRepo.findByAccountNo(sender);
    Account r = accountRepo.findByAccountNo(receiver);

    if(s==null){
     res.put("msg","Sender account not found");
     return res;
    }

    if(r==null){
     res.put("msg","Receiver account not found");
     return res;
    }

    if(s.getBalance() < amount){
     res.put("msg","Insufficient balance");
     return res;
    }

 // generate otp
    String otp = String.valueOf(new Random().nextInt(9000)+1000);
    otpStore.put(sender, otp);

    // send email
    SimpleMailMessage mail = new SimpleMailMessage();
    mail.setTo(s.getEmail()); // sender email from DB
    mail.setSubject("Money Transfer OTP");
    mail.setText("Your OTP for money transfer is: "+otp);

    mailSender.send(mail);

    res.put("msg","OTP sent successfully to Gmail");
    
    return res;
    }
    
    
    // VerifyTransferOtp
    
    @Transactional
    @PostMapping("/verifyTransferOtp")
    public Map<String,String> verifyTransferOtp(@RequestBody Map<String,String> data){

    String sender=data.get("sender");
    String receiver=data.get("receiver");
    double amount=Double.parseDouble(data.get("amount"));
    String otp=data.get("otp");

    Map<String,String> res=new HashMap<>();

    // check otp
    if(!otpStore.containsKey(sender) || 
       !otpStore.get(sender).equals(otp)){
     res.put("msg","Invalid OTP");
     return res;
    }

    Account s = accountRepo.findByAccountNo(sender);
    Account r = accountRepo.findByAccountNo(receiver);

    if(s==null || r==null){
     res.put("msg","Account error");
     return res;
    }

    if(s.getBalance()<amount){
     res.put("msg","Insufficient balance");
     return res;
    }

    // deduct
    s.setBalance(s.getBalance()-amount);

    // add
    r.setBalance(r.getBalance()+amount);

    accountRepo.save(s);
    accountRepo.save(r);

    // save transaction
    Transaction t=new Transaction();
    t.setFromAcc(sender);
    t.setToAcc(receiver);
    t.setAmount(amount);
    t.setType("TRANSFER");
    t.setDate(java.time.LocalDateTime.now().toString());
    tRepo.save(t);


    otpStore.remove(sender);

    res.put("msg","Money transferred successfully");
    return res;

    }

    
    // ================= TRANSFER MONEY =================

    @PostMapping("/transfer")
    public String transfer(@RequestParam String senderAccNo,
                           @RequestParam String receiverAccNo,
                           @RequestParam double amount) {

    	Account sender = accountRepo.findByAccountNo(senderAccNo);
    	Account receiver = accountRepo.findByAccountNo(receiverAccNo);

        if(sender == null || receiver == null){
            return "Account not found";
        }

        if(sender.getBalance() < amount){
            return "Insufficient balance";
        }

        // 💰 balance update
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        accountRepo.save(sender);
        accountRepo.save(receiver);

        // 🟢 ADD TRANSACTION SAVE HERE
        Transaction t = new Transaction();
        t.setFromAcc(senderAccNo);
        t.setToAcc(receiverAccNo);
        t.setAmount(amount);
        t.setType("TRANSFER");
        t.setUsername(sender.getName()); // 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String todayDate = LocalDateTime.now().format(formatter);
        t.setDate(todayDate);
        tRepo.save(t);

        return "Money transferred successfully";
    }


    
 // ================= TRANSACTION HISTORY =================
    @GetMapping("/transactions")
    public java.util.List<Transaction> getAllTransactions(){
        return tRepo.findAll();
    }
    
    
    
    
 // ================= SEND OTP =================
    @PostMapping("/send-qr-otp")
    public String sendQrOtp(@RequestParam String account){

        try{
            Account acc = accountRepo.findByAccountNo(account);

            if(acc == null) return "Account not found";

            String otp = String.valueOf((int)(Math.random()*9000)+1000);

            // 🔥 store in same map
            otpStore.put(account.trim(), otp);

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(acc.getEmail());
            msg.setSubject("QR OTP");
            msg.setText("Your OTP: "+otp);
            mailSender.send(msg);

            System.out.println("OTP SENT = "+otp);
            System.out.println("STORE AFTER SEND = "+otpStore);

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

        account = account.trim();

        String storedOtp = otpStore.get(account);

        System.out.println("FULL MAP = "+otpStore);
        System.out.println("ACCOUNT = "+account);
        System.out.println("ENTERED OTP = "+otp);
        System.out.println("STORED OTP = "+storedOtp);

        if(storedOtp == null)
            return "OTP expired";

        if(!storedOtp.equals(otp))
            return "Wrong OTP";

        otpStore.remove(account);

        return "QR Verified Successfully";
    }
    
    
    
    
    //send otp of qr scanner
    
    
    @PostMapping("/sendscanOtp")
    public String sendOtp(@RequestBody Map<String,String> data){

        try{
            if(data==null){
                return "❌ No data received";
            }

            String userAccount = data.get("userAccount");
            String receiver = data.get("receiverAccount");
            String amount = data.get("amount");

            // null safety
            if(userAccount==null || userAccount.trim().isEmpty()){
                return "❌ Enter sender account number";
            }

            if(receiver==null || receiver.trim().isEmpty()){
                return "❌ Receiver account missing";
            }

            if(amount==null || amount.trim().isEmpty()){
                return "❌ Enter amount";
            }

            userAccount = userAccount.trim();
            receiver = receiver.trim();
            amount = amount.trim();

            System.out.println("SENDER = "+userAccount);

            Account acc = accountRepo.findByAccountNo(userAccount);

            if(acc == null){
                return "❌ Sender account not found";
            }

            if(acc.getEmail()==null || acc.getEmail().isEmpty()){
                return "❌ Email not linked to this account";
            }

            // generate OTP
            String otp = String.valueOf(new Random().nextInt(900000)+100000);

            otpStore.put(userAccount, otp);

            System.out.println("OTP = "+otp);
            System.out.println("EMAIL = "+acc.getEmail());
            System.out.println("OTP MAP = "+otpStore);

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(acc.getEmail());
            msg.setSubject("Secure Payment OTP");
            msg.setText("Your OTP for Rs."+amount+" transfer is: "+otp);

            mailSender.send(msg);

            return "OTP sent successfully to your Gmail";

        }catch(Exception e){
            e.printStackTrace();
            return "Server error while sending OTP";
        }
    }

    
    //verify qr code and send orp of scanner
    
    @PostMapping("/verifyscanOtp")
    @Transactional
    public String verifyOtp(@RequestBody Map<String,String> data){

        try{

            if(data==null){
                return "No data received";
            }

            String userAccount = data.get("userAccount");
            String receiver = data.get("receiverAccount");
            String amount = data.get("amount");
            String enteredOtp = data.get("otp");

            if(userAccount==null || enteredOtp==null){
                return "Missing data";
            }

            userAccount = userAccount.trim();
            enteredOtp = enteredOtp.trim();

            System.out.println("USER ACCOUNT = "+userAccount);
            System.out.println("ENTERED OTP = "+enteredOtp);
            System.out.println("FULL OTP MAP = "+otpStore);

            String storedOtp = otpStore.get(userAccount);

            if(storedOtp == null){
                return "Generate OTP first";
            }

            if(!storedOtp.equals(enteredOtp)){
                return "Wrong OTP";
            }

            Account sender = accountRepo.findByAccountNo(userAccount);
            Account receiverAcc = accountRepo.findByAccountNo(receiver);

            if(sender == null){
                return "Sender not found";
            }

            if(receiverAcc == null){
                return "Receiver not found";
            }

            double amt = Double.parseDouble(amount);

            if(sender.getBalance() < amt){
                return "Insufficient Balance";
            }

            sender.setBalance(sender.getBalance() - amt);
            receiverAcc.setBalance(receiverAcc.getBalance() + amt);

            accountRepo.save(sender);
            accountRepo.save(receiverAcc);

            Transaction t = new Transaction();
            t.setFromAcc(userAccount);
            t.setToAcc(receiver);
            t.setAmount(amt);
            t.setType("QR PAYMENT");
            t.setDate(LocalDateTime.now().toString());
            tRepo.save(t);

            otpStore.remove(userAccount);

            return "Transfer Successful ✅";

        }catch(Exception e){
            e.printStackTrace();
            return "Server error during transfer";
        }
    }
    
    
    
    /////admin login send otp
    @PostMapping("/admin/sendOtp")
    public String sendAdminOtp(@RequestBody Map<String,String> data){

        String email = data.get("email");

        // check admin email in database
        var admin = adminRepo.findByEmail(email);

        if(admin == null){
            return "Access Denied: Not Admin";
        }

        // generate otp
        adminOtp = String.valueOf(new Random().nextInt(900000)+100000);
        adminEmailSession = email;

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Admin Login OTP");
        msg.setText("Your Admin OTP is: "+adminOtp);

        mailSender.send(msg);

        return "OTP Sent to Admin Gmail";
    }
    
    
    
    ///admin login otp verify 
    @PostMapping("/admin/verifyOtp")
    public String verifyAdminOtp(@RequestBody Map<String,String> data){

        String otp = data.get("otp");

        if(adminOtp.equals(otp)){
            adminOtp = "";
            return "success";
        }else{
            return "Wrong OTP";
        }
    }
    
    // admin dtails of gmail
    @GetMapping("/admin/user/{email}")
    public Map<String,Object> getUserDetails(@PathVariable String email){

        Map<String,Object> res = new HashMap<>();

        try{

            User user = userRepo.findByEmail(email).orElse(null);
            Account acc = accountRepo.findByEmail(email);

            if(user == null || acc == null){
                res.put("status","fail");
                res.put("message","User not found");
                return res;
            }

            List<Transaction> sent = tRepo.findByFromAcc(acc.getAccountNo());
            List<Transaction> received = tRepo.findByToAcc(acc.getAccountNo());

            List<Transaction> all = new ArrayList<>();
            all.addAll(sent);
            all.addAll(received);

            res.put("status","success");
            res.put("name", user.getName());
            res.put("email", user.getEmail());
            res.put("accountNo", acc.getAccountNo());
            res.put("balance", acc.getBalance());
            res.put("transactions", all);

        }catch(Exception e){
            e.printStackTrace();
            res.put("status","error");
        }

        return res;
    }
    



}



