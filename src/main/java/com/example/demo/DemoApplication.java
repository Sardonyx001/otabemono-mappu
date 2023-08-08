package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DemoApplication {
// メイン関数
// アプリこ実行はここから始まる
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}