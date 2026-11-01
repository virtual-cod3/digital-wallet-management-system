
package com.jsp.wallet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.jsp.wallet.entity.Transaction;
import com.jsp.wallet.dto.TransactionDTO;

import com.jsp.wallet.dto.TransferRequest;
import com.jsp.wallet.entity.Wallet;
import com.jsp.wallet.repository.WalletRepository;
import com.jsp.wallet.repository.TransactionRepository;
import com.jsp.wallet.exception.WalletNotFoundException;
import com.jsp.wallet.exception.InsufficientBalanceException;
import com.jsp.wallet.exception.InvalidWalletOperationException;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WalletService(
            WalletRepository walletRepository,
            TransactionRepository transactionRepository) {

        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
        		.orElseThrow(() ->
                new WalletNotFoundException("Wallet not found for user ID: " + userId));
    }

    @Transactional
    public Wallet deposit(Long userId, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
        	throw new InvalidWalletOperationException(
        	        "Deposit amount must be greater than zero");
        }

        Wallet wallet = getWalletByUserId(userId);

        wallet.setBalance(wallet.getBalance().add(amount));

        walletRepository.save(wallet);

        saveTransaction(wallet, amount, "DEPOSIT");

        return wallet;
    }

    @Transactional
    public Wallet withdraw(Long userId, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
        	throw new InvalidWalletOperationException(
        	        "Withdrawal amount must be greater than zero");
        }

        Wallet wallet = getWalletByUserId(userId);

        if (wallet.getBalance().compareTo(amount) < 0) {
        	throw new InsufficientBalanceException("Insufficient wallet balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));

        walletRepository.save(wallet);

        saveTransaction(wallet, amount, "WITHDRAW");

        return wallet;
    }

    private void saveTransaction(Wallet wallet, BigDecimal amount, String type) {

        Transaction transaction = new Transaction();

        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDateTime(LocalDateTime.now());
        transaction.setWallet(wallet);

        transactionRepository.save(transaction);
    }
    public List<TransactionDTO> getTransactionHistory(Long userId) {

        Wallet wallet = getWalletByUserId(userId);

        List<Transaction> transactions =
                transactionRepository.findByWalletId(wallet.getId());

        return transactions.stream()
                .map(transaction -> new TransactionDTO(
                        transaction.getId(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDateTime()
                ))
                .toList();
    }
    
    
    @Transactional
    public Wallet transfer(Long senderUserId, TransferRequest request) {

        Long receiverUserId = request.getReceiverUserId();
        BigDecimal amount = request.getAmount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
        	throw new InvalidWalletOperationException(
        	        "Cannot transfer money to yourself");
        }

        if (senderUserId.equals(receiverUserId)) {
            throw new InvalidWalletOperationException("Cannot transfer money to yourself");
        }
        
        Wallet senderWallet = getWalletByUserId(senderUserId);
        Wallet receiverWallet = getWalletByUserId(receiverUserId);

        if (senderWallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient wallet balance");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(amount));
        receiverWallet.setBalance(receiverWallet.getBalance().add(amount));

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        saveTransaction(senderWallet, amount, "TRANSFER_OUT");
        saveTransaction(receiverWallet, amount, "TRANSFER_IN");

        return senderWallet;
    }
}
