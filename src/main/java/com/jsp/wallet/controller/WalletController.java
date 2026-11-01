
package com.jsp.wallet.controller;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jsp.wallet.dto.DepositRequest;
import com.jsp.wallet.dto.WithdrawRequest;
import com.jsp.wallet.dto.TransactionDTO;
import com.jsp.wallet.dto.TransferRequest;
import com.jsp.wallet.entity.User;
import com.jsp.wallet.entity.Wallet;
import com.jsp.wallet.repository.UserRepository;
import com.jsp.wallet.service.WalletService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;
    private final UserRepository userRepository;

    public WalletController(
            WalletService walletService,
            UserRepository userRepository) {

        this.walletService = walletService;
        this.userRepository = userRepository;
    }

    private void verifyOwnership(Long userId, Authentication authentication) {

        User loggedInUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new AccessDeniedException("Authenticated user not found"));

        if (!loggedInUser.getId().equals(userId)) {
            throw new AccessDeniedException(
                    "You are not authorized to access this wallet");
        }
    }

    @GetMapping("/{userId}/balance")
    public Wallet getWalletBalance(
            @PathVariable Long userId,
            Authentication authentication) {

        verifyOwnership(userId, authentication);

        return walletService.getWalletByUserId(userId);
    }

    @PostMapping("/{userId}/deposit")
    public Wallet deposit(
            @PathVariable Long userId,
            @Valid @RequestBody DepositRequest request,
            Authentication authentication) {

        verifyOwnership(userId, authentication);

        return walletService.deposit(userId, request.getAmount());
    }

    @PostMapping("/{userId}/withdraw")
    public Wallet withdraw(
            @PathVariable Long userId,
            @Valid @RequestBody WithdrawRequest request,
            Authentication authentication) {

        verifyOwnership(userId, authentication);

        return walletService.withdraw(userId, request.getAmount());
    }

    @GetMapping("/{userId}/transactions")
    public List<TransactionDTO> getTransactionHistory(
            @PathVariable Long userId,
            Authentication authentication) {

        verifyOwnership(userId, authentication);

        return walletService.getTransactionHistory(userId);
    }

    @PostMapping("/{senderUserId}/transfer")
    public Wallet transfer(
            @PathVariable Long senderUserId,
            @Valid @RequestBody TransferRequest request,
            Authentication authentication) {

        verifyOwnership(senderUserId, authentication);

        return walletService.transfer(senderUserId, request);
    }
}
