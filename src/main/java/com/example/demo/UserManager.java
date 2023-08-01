package com.example.demo;

import java.io.IOException;
import java.util.List;

import com.opencsv.exceptions.CsvException;

public class UserManager {
    private final String userDataFilePath = "/Users/jam/Documents/\u5927\u5B66/\u30BD\u30D5\u30C8\u30A6\u30A7\u30A2\u958B\u767A\u6F14\u7FD2\uFF12/demo/src/main/resources/static/userData.csv";

    public boolean Authenticate(String usernameToFind, String passwordToFind) throws IOException, CsvException {
        CsvDAO csvReader = new CsvDAO();
        List<String[]> csvData = csvReader.readCsvFile("classpath:static/userData.csv");
        String[] csvDataHeader = csvData.get(0);
        String usernameLabel = "\u6C0F\u540D\uFF08\u30ED\u30FC\u30DE\u5B57\uFF09"; // 「氏名（ローマ字）」 encoded in SHIFT-JIS
        Integer usernameIndex = 0;
        String passwordLabel = "パスワード";
        Integer passwordIndex = 0 ;
        Integer i = 0;
        for(String entry: csvDataHeader){
            if(entry.equals(usernameLabel)){
                usernameIndex = i;
            }
            if(entry.equals(passwordLabel)){
                passwordIndex = i;
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

    public void saveUser(User user){
        CsvDAO csvWriter = new CsvDAO();
        try {
            csvWriter.writeToCsvFile(userDataFilePath, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
