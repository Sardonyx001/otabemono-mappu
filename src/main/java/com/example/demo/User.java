package com.example.demo;

import java.beans.JavaBean;
import java.io.Serializable;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
@JavaBean
public class User implements Serializable{
    // 連番,
    @CsvBindByName(column="\u9023\u756A") 
    private String UUID;
    // 氏名,
    @CsvBindByName(column="\u6C0F\u540D")
    private String fullName;
    // 氏名（カタカナ）,
    @CsvBindByName(column="\u6C0F\u540D\uFF08\u30AB\u30BF\u30AB\u30CA\uFF09")
    private String fullNameKatakana;
    // 氏名（ローマ字）,
    @CsvBindByName(column="\u6C0F\u540D")
    private String fullNameRomaji;
    // 性別,
    @CsvBindByName(column="\u6027\u5225")
    private String gender;
    // 携帯電話,
    @CsvBindByName(column="\u643A\u5E2F\u96FB\u8A71")
    private String phone;
    // メールアドレス,
    @CsvBindByName(column="\u30E1\u30FC\u30EB\u30A2\u30C9\u30EC\u30B9")
    private String email;
    // 郵便番号,
    @CsvBindByName(column="\u90F5\u4FBF\u756A\u53F7")
    private String postalCode;
    // 住所|都道府県|市|町村|番地|ビル部屋番号
    @CsvBindByName(column = "\u90FD\u9053\u5E9C\u770C",required=false)
    private String address;
    @CsvBindByName(column = "\u5E02",required=false)
    private String addressCity = "";
    @CsvBindByName(column = "\u753A\u6751",required=false)
    private String addressTown = "";
    @CsvBindByName(column = "\u756A\u5730",required=false)
    private String addressStreet = "";
    @CsvBindByName(column = "\u30D3\u30EB\u90E8\u5C4B\u756A\u53F7",required=false)
    private String addressBuilding = "";

    // 生年月日,
    @CsvBindByName(column="\u751F\u5E74\u6708\u65E5")
    private String dateOfBirth;
    // 出身地,
    @CsvBindByName(column="\u51FA\u8EAB\u5730")
    private String birthPlace;
    // パスワード
    @CsvBindByName(column="\u30D1\u30B9\u30EF\u30FC\u30C9")
    private String password;

    public User() {
    }

    // public User(String fullName, String fullNameKatakana, String fullNameRomaji, String gender, String phone, String email,
    //             String postalCode, String address, String dateOfBirth, String birthPlace, String password) {
    //     this.fullName = fullName;
    //     this.fullNameKatakana = fullNameKatakana;
    //     this.fullNameRomaji = fullNameRomaji;
    //     this.gender = gender;
    //     this.phone = phone;
    //     this.email = email;
    //     this.postalCode = postalCode;
    //     this.address = address;
    //     this.dateOfBirth = dateOfBirth;
    //     this.birthPlace = birthPlace;
    //     this.password = password;
    // }
}
