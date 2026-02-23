package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile,Integer>{

    Profile findByUsername(String username); // 🔥 important
}