package ru.yarm.banksample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication//(exclude = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration)
public class BanksampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BanksampleApplication.class, args);
	}


}
