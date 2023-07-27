package com.example.demo;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CsvReader {
    public List<String[]> readCsvFile(String filePath) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(
            new InputStreamReader(
                new FileInputStream(ResourceUtils.getFile(filePath)), 
                "Shift_JIS")
            )) {
            return reader.readAll();
        }
    }
}
