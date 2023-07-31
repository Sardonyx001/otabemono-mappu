package com.example.demo;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Setter
@Getter
@Service
@Scope("singleton")
public class SharedStateService {

    private List<String> foodQueryList;

    private boolean homeQueryInputError = false;

    
}
