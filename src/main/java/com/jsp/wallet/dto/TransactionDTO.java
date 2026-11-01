package com.jsp.wallet.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;
    private String type;
    private BigDecimal amount;
    private LocalDateTime dateTime;

    public TransactionDTO() {
    }

    public TransactionDTO(Long id, String type, BigDecimal amount, LocalDateTime dateTime) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}