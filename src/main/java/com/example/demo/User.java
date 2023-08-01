package com.example.demo;

import java.beans.JavaBean;
import java.io.Serializable;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

@Data
@JavaBean
public class User implements Serializable{
    // 連番,
    @CsvBindByName(column="\u9023\u756A") 
    @CsvBindByPosition(position=0)
    private String UUID;
    // 氏名,
    @CsvBindByName(column="\u6C0F\u540D")
    @CsvBindByPosition(position=1)
    private String fullName;
    // 氏名（カタカナ）,
    @CsvBindByName(column="\u6C0F\u540D\uFF08\u30AB\u30BF\u30AB\u30CA\uFF09")
    @CsvBindByPosition(position=2)
    private String fullNameKatakana;
    // 氏名（ローマ字）,
    @CsvBindByName(column="\u6C0F\u540D\uFF08\u30ED\u30FC\u30DE\u5B57\uFF09")
    @CsvBindByPosition(position=3)
    private String fullNameRomaji;
    // 性別,
    @CsvBindByName(column="\u6027\u5225")
    @CsvBindByPosition(position=4)
    private String gender;
    // 携帯電話,
    @CsvBindByName(column="\u643A\u5E2F\u96FB\u8A71")
    @CsvBindByPosition(position=5)
    private String phone;
    // メールアドレス,
    @CsvBindByName(column="\u30E1\u30FC\u30EB\u30A2\u30C9\u30EC\u30B9")
    @CsvBindByPosition(position=6)
    private String email;
    // 郵便番号,
    @CsvBindByName(column="\u90F5\u4FBF\u756A\u53F7")
    @CsvBindByPosition(position=7)
    private String postalCode;
    // 住所 || 都道府県
    @CsvBindByName(column = "\u90FD\u9053\u5E9C\u770C",required=false)
    @CsvBindByPosition(position=8)
    private String address;
    // 市
    @CsvBindByName(column = "\u5E02",required=false)
    @CsvBindByPosition(position=9)
    private String addressCity = "";
    // 町村
    @CsvBindByName(column = "\u753A\u6751",required=false)
    @CsvBindByPosition(position=10)
    private String addressTown = "";
    // 番地
    @CsvBindByName(column = "\u756A\u5730",required=false)
    @CsvBindByPosition(position=11)
    private String addressStreet = "";
    // ビル部屋番号
    @CsvBindByName(column = "\u30D3\u30EB\u90E8\u5C4B\u756A\u53F7",required=false)
    @CsvBindByPosition(position=12)
    private String addressBuilding = "";
    // 生年月日,
    @CsvBindByName(column="\u751F\u5E74\u6708\u65E5")
    @CsvBindByPosition(position=13)
    private String dateOfBirth;
    // 出身地,
    @CsvBindByName(column="\u51FA\u8EAB\u5730")
    @CsvBindByPosition(position=14)
    private String birthPlace;
    // パスワード
    @CsvBindByName(column="\u30D1\u30B9\u30EF\u30FC\u30C9")
    @CsvBindByPosition(position=15)
    private String password;
    
    public User() {
    }

}
