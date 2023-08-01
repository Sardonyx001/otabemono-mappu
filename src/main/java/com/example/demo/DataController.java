package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.util.List;

@Controller
public class DataController {
    @GetMapping("/csvdata")
    public String getCsvData(Model model) throws IOException, CsvException {
        CsvDataAccessObject csvReader = new CsvDataAccessObject();
        List<String[]> csvData = csvReader.readCsvFile("classpath:static/userData.csv");
        model.addAttribute("csvData", csvData);
        return "data";
    }
    
}
