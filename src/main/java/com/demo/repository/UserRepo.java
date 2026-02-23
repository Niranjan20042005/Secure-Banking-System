package com.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.entity.User;


public interface UserRepo extends JpaRepository<User,Integer>{

	 Optional<User> findByUsername(String username);   // 🔥 add
	    Optional<User> findByEmail(String email);
}