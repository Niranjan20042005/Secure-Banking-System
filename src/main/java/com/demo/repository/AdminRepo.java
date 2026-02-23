package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.entity.Admin;

public interface AdminRepo extends JpaRepository<Admin,Integer>{
    Admin findByEmail(String email);
}
