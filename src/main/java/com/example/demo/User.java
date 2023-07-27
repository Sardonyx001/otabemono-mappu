package com.example.demo;

import lombok.Data;

@Data
public class User {
    private String fullName;
    private String fullNameKatakana;
    private String fullNameRomaji;
    private String gender;
    private String phone;
    private String email;
    private String postalCode;
    private String address;
    private String dateOfBirth;
    private String birthPlace;
    private String password;

    public User() {
    }

    public User(String fullName, String fullNameKatakana, String fullNameRomaji, String gender, String phone, String email,
                String postalCode, String address, String dateOfBirth, String birthPlace, String password) {
        this.fullName = fullName;
        this.fullNameKatakana = fullNameKatakana;
        this.fullNameRomaji = fullNameRomaji;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.postalCode = postalCode;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.birthPlace = birthPlace;
        this.password = password;
    }
}
