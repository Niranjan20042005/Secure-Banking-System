package com.demo.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.demo.entity.Account;
import com.demo.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction,Integer>{
	List<Transaction> findByFromAcc(String fromAcc);
    List<Transaction> findByToAcc(String toAcc);
}

