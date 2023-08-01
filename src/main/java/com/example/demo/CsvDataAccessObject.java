package com.example.demo;

import com.opencsv.CSVReader;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

public class CsvDataAccessObject {
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
        try (Writer writer =  
                new OutputStreamWriter(
                new FileOutputStream(
                    ResourceUtils.getFile(filePath),true),
                    "Shift_JIS"))
        {
            StatefulBeanToCsv<User> beanWriter = 
                new StatefulBeanToCsvBuilder<User>(writer).build();
            beanWriter.write(user);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
