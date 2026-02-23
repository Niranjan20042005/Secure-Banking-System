package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.entity.Account;
import java.util.List;

public interface AccountRepo extends JpaRepository<Account,Integer>{

	Account findFirstByEmail(String email);

	Account findByEmail(String email);
    Account findByAccountNo(String accountNo);
    
    Account findByToken(String token);
    
    @Query(value="SELECT SUM(balance) FROM account", nativeQuery=true)
    Double getTotalBalance();

}

