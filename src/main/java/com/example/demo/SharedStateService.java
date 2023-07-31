package com.example.demo;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;


@Service
@Scope("singleton")
public class SharedStateService {
    @Setter @Getter private List<String> foodQueryList;
}
