package com.BuSEPTim18.BuSEPTim18.service;

import java.io.StringWriter;
import java.security.KeyPair;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BuSEPTim18.BuSEPTim18.dto.CertificateDTO;
import com.BuSEPTim18.BuSEPTim18.dto.CertificateNewDTO;
import com.BuSEPTim18.BuSEPTim18.model.IssuerData;
import com.BuSEPTim18.BuSEPTim18.model.SubjectData;
import com.BuSEPTim18.BuSEPTim18.repository.KeyStoreRepository;
import com.BuSEPTim18.BuSEPTim18.utils.CertificateGenerator;
import com.BuSEPTim18.BuSEPTim18.utils.CertificateUtil;

@Service
public class CertificateService {
	
	@Autowired
	CertificateGenerator certificateGenerator;
	
	@Autowired
	KeyStoreRepository certificateRepository;
	
	public List<CertificateDTO> getAll() {
		
		List<X509Certificate> listCert = new ArrayList<X509Certificate>();
		List<CertificateDTO> listCertDTO = new ArrayList<CertificateDTO>();
		
		listCert = certificateRepository.getCertificates();
		
		for (X509Certificate cert : listCert) {
			listCertDTO.add(new CertificateDTO(cert));
		}
		
		return 	listCertDTO;
	}
	
	public List<CertificateDTO> getAllCa() {
		
		List<X509Certificate> listCert = new ArrayList<X509Certificate>();
		List<CertificateDTO> listCertDTO = new ArrayList<CertificateDTO>();
		
		listCert = certificateRepository.getCertificates();
		
		for (X509Certificate cert : listCert) {
			if(cert.getBasicConstraints() != -1) {
				listCertDTO.add(new CertificateDTO(cert));
			}			
		}
		
		return listCertDTO;
	}
	
	public X509Certificate getOne(String serialNumber) {
		return certificateRepository.getCertificate(serialNumber)
				.orElse(null);
	}
	
	public String download(String serialNumber) {
		X509Certificate cert = getOne(serialNumber);
		StringWriter streamWritter = new StringWriter();
		
		try {
			streamWritter.write(CertificateUtil.encode(cert.getEncoded()));
		} catch (CertificateEncodingException e) {
			e.printStackTrace();
		}
	   
		return streamWritter.toString();
	}
	
	public CertificateDTO addSelfSignedCa(CertificateNewDTO newCert) {		
		try {
			X500Name x500name = CertificateUtil.generateX500Name(newCert);
			KeyPair keyPair = CertificateUtil.generateKeyPair();

			LocalDate dt = LocalDate.parse(newCert.getExpirationDate());
			
			SubjectData subjectData = new SubjectData(keyPair.getPublic(), x500name, LocalDate.now(), dt);
			IssuerData issuerData = new IssuerData(keyPair.getPrivate(), x500name);

			X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData, true);	

			certificateRepository.saveCertificate(certificate.getSerialNumber().toString(), keyPair.getPrivate(), certificate);
		
			return new CertificateDTO(certificate);
		} catch(Exception e) {
			return null;
		}
	}
	
	// dodaje sertifikat, ako je isCa true, dodaj intermediate sertifikat, ako je false, dodaj end sertifikat
	public X509Certificate addCetrificate(String issuerSerialNumber, CertificateNewDTO newCert, boolean isCa) {
		IssuerData issuerData = certificateRepository.getIssuerData(issuerSerialNumber);
		
		if (issuerData == null || getOne(issuerSerialNumber).getBasicConstraints() == -1) {
			return null; // vrati bad request
		}

		try {
			X500Name x500name = CertificateUtil.generateX500Name(newCert);
			KeyPair keyPair = CertificateUtil.generateKeyPair();
			
			LocalDate dt = LocalDate.parse(newCert.getExpirationDate());
			
			SubjectData subjectData = new SubjectData(keyPair.getPublic(), x500name, LocalDate.now(), dt);
			
			X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData, isCa);
			
			certificateRepository.saveCertificate(certificate.getSerialNumber().toString(), keyPair.getPrivate(), certificate);
			
			return certificate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
