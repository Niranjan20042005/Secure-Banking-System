package com.demo.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    private String secret = "bankprojectsupersecurejwtsecretkey2026secure";

    private Key getKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ================= GENERATE TOKEN =================
    // now using USERNAME (not email)
    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)   // 🔥 store username
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10)) // 10 hrs
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ================= EXTRACT USERNAME =================
    public String extractUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ================= VALIDATE TOKEN =================
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
