package com.jsp.wallet.dto;

public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;

    public UserResponse(Long id, String fullName,
                        String email, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}