package com.BuSEPTim18.BuSEPTim18.contorller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BuSEPTim18.BuSEPTim18.dto.CertificateDTO;


@RestController
@RequestMapping("api/certificate")
public class CertificateController {
	
//	@GetMapping
//	public List<CertificateDTO> getCertificates(@RequestParam(value = "type", required = false, defaultValue = "all") String filter) {		
//		if (filter.equals("all")) {
//			return certificateService.getAll();
//		} else if (filter.equals("ca")) {
//			return certificateService.getAllCa();
//		} else {
//			throw new BadRequestException();
//		}
//	}
	
}
