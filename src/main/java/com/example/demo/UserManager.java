package com.example.demo;

import java.util.List;

public class UserManager {
    public boolean Authenticate(List<String[]> csvData, String usernameToFind, String passwordToFind) {
        String[] csvDataHeader = csvData.get(0);
        String usernameLabel = "\u6C0F\u540D\uFF08\u30ED\u30FC\u30DE\u5B57\uFF09"; // 「氏名（ローマ字）」 encoded in SHIFT-JIS
        Integer usernameIndex = 0;
        String passwordLabel = "パスワード";
        Integer passwordIndex = 0 ;
        Integer i = 0;
        for(String entry: csvDataHeader){
            if(entry.equals(usernameLabel)){
                usernameIndex = i;
                System.out.println(usernameIndex);
            }
            if(entry.equals(passwordLabel)){
                passwordIndex = i;
                System.out.println(passwordIndex);
            }
            i++;
        }
        for (String[] row : csvData) {
            String username = row[usernameIndex];
            String password = row[passwordIndex];
            if (username.equals(usernameToFind) && password.equals(passwordToFind)) {
                return true;
            }

        }
        return false;
    }

}
