package com.BuSEPTim18.BuSEPTim18.service;

import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.BuSEPTim18.BuSEPTim18.dto.CertificateDTO;

@Service
public class DataValidationService {
	
	
	public static boolean isHtml(String input) {
        boolean isHtml = false;
        if (input != null) {
            if (!input.equals(HtmlUtils.htmlEscape(input))) {
                isHtml = true;
            }
        }
        return isHtml;
    }
	
	public String escape(String input) {
		return HtmlUtils.htmlEscape(input);
	}
	
	public CertificateDTO escapeCertDto(CertificateDTO cert) {
		CertificateDTO escapedCert = cert;
		escapedCert.setCommonName(this.escape(cert.getCommonName()));
		escapedCert.setGivenname(this.escape(cert.getGivenname()));
		escapedCert.setEmail(this.escape(cert.getEmail()));
		escapedCert.setIssuerName(this.escape(cert.getIssuerName()));
		escapedCert.setOrganization(this.escape(cert.getOrganization()));
		escapedCert.setOrganizationalUnit(this.escape(cert.getOrganizationalUnit()));
		escapedCert.setSubjectName(this.escape(cert.getSubjectName()));
		escapedCert.setSurname(this.escape(cert.getSurname()));
		
		return escapedCert;
		
	}

}
