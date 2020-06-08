package com.BuSEPTim18.BuSEPTim18.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.BuSEPTim18.BuSEPTim18.service.CustomUserDetailsService;
import com.BuSEPTim18.BuSEPTim18.utils.CertificateGenerator;

@Configuration
public class Config {
	
	@Bean
	public CertificateGenerator certificateGenerator() {
		return new CertificateGenerator();
	}
		
}
