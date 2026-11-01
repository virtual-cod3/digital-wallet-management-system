package com.jsp.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.wallet.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByWalletId(Long walletId);

}