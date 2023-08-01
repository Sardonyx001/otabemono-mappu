package com.example.demo;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.util.ResourceUtils;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvDAO {
    public List<String[]> readCsvFile(String filePath) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(
            new InputStreamReader(
                new FileInputStream(ResourceUtils.getFile(filePath)), 
                "Shift_JIS")
            )) {
            return reader.readAll();
        }
    }


    public void writeToCsvFile(String filePath,User user) throws IOException, CsvException {
        try (
            Writer writer =  
                new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(
                    ResourceUtils.getFile(filePath),true),
                    "Shift_JIS"))){
            List<String> userDataAsCsvLine = Arrays.asList(
                user.getUUID(),",",
                user.getFullName(),",",
                user.getFullNameKatakana(),",",
                user.getFullNameRomaji(),",",
                user.getGender(),",",
                user.getPhone(),",",
                user.getEmail(),",",
                user.getPostalCode(),",",
                user.getAddress(),",",
                user.getAddressCity(),",",
                user.getAddressTown(),",",
                user.getAddressStreet(),",",
                user.getAddressBuilding(),",",
                user.getDateOfBirth(),",",
                user.getBirthPlace(),",",
                user.getPassword()
            );
            System.out.println("user data"+userDataAsCsvLine.toString());
            writer.append(userDataAsCsvLine.stream().collect(Collectors.joining()));
            System.out.println("Data has been written.");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
