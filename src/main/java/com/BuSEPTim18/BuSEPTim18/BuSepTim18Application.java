package com.BuSEPTim18.BuSEPTim18;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BuSepTim18Application {

	public static void main(String[] args) {
		SpringApplication.run(BuSepTim18Application.class, args);
		Security.addProvider(new BouncyCastleProvider());
	}

}
