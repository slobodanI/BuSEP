package com.BuSEPTim18.BuSEPTim18.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BuSEPTim18.BuSEPTim18.dto.CertificateDTO;
import com.BuSEPTim18.BuSEPTim18.dto.CertificateNewDTO;
import com.BuSEPTim18.BuSEPTim18.model.CertificateHolderType;
import com.BuSEPTim18.BuSEPTim18.model.IssuerData;
import com.BuSEPTim18.BuSEPTim18.model.RevokedCertificate;
import com.BuSEPTim18.BuSEPTim18.model.SubjectData;
import com.BuSEPTim18.BuSEPTim18.repository.CertificateHolderTypeRepository;
import com.BuSEPTim18.BuSEPTim18.repository.KeyStoreRepository;
import com.BuSEPTim18.BuSEPTim18.repository.RevokedCertificateRepository;
import com.BuSEPTim18.BuSEPTim18.utils.CertificateGenerator;
import com.BuSEPTim18.BuSEPTim18.utils.CertificateUtil;


@Service
public class CertificateService {
	
	@Autowired
	CertificateGenerator certificateGenerator;
	
	@Autowired
	KeyStoreRepository certificateRepository;
	
	@Autowired
	CertificateHolderTypeRepository certificateHolderTypeRepository;
	
	@Autowired
	RevokedCertificateRepository revokedCertificateRepository;
	
	public List<CertificateDTO> getAll() {
		
		List<X509Certificate> listCert = new ArrayList<X509Certificate>();
		List<CertificateDTO> listCertDTO = new ArrayList<CertificateDTO>();
		List<CertificateHolderType> listCertHolderType = new ArrayList<CertificateHolderType>();
		
		listCert = certificateRepository.getCertificates();
		System.out.println("***** IMA IH U FAJLU: " + listCert.size());
//		for(X509Certificate certifikat : listCert) {
//			System.out.println("Serijski broj" + certifikat.getSerialNumber());
//		}
		
		listCertHolderType = certificateHolderTypeRepository.findAll();
		
		for (X509Certificate cert : listCert) {
			
			CertificateDTO tempDTO = new CertificateDTO(cert);
			
			if(!listCertHolderType.isEmpty()) {
				for(CertificateHolderType c : listCertHolderType) {
					if(c.getSerialNumber().equals(cert.getSerialNumber().toString())){
						System.out.println("Jesu equal");
	//					listCertDTO.add(new CertificateDTO(cert,c.getHolderType()));
						tempDTO.setHolderType(c.getHolderType());
					}
	//				else {
	//					listCertDTO.add(new CertificateDTO(cert));
	//					System.out.println("Nisu equal");
	//				}
					listCertDTO.add(tempDTO);
				}
			}else {
				listCertDTO.add(tempDTO);
//				System.out.println("Prazna Lista ");
			}
		}
		
//		for (X509Certificate cert : listCert) {
//			listCertDTO.add(new CertificateDTO(cert));
//		}
		
		return 	listCertDTO;
	}
	
	public List<CertificateDTO> getAllCa() {
		
		List<X509Certificate> listCert = new ArrayList<X509Certificate>();
		List<CertificateDTO> listCertDTO = new ArrayList<CertificateDTO>();
	
		
		listCert = certificateRepository.getCertificates();
		
		for (X509Certificate cert : listCert) {
			// ako moze da izdaje druge sertifikate
			if(cert.getBasicConstraints() != -1) {
				// ako je validan
				if(checkValidity(cert.getSerialNumber().toString())) {
					listCertDTO.add(new CertificateDTO(cert));

				}
			}			
		}
		
		return listCertDTO;
	}
	
	//nije korisceno
public List<CertificateDTO> getAllEnd() {
		
		List<X509Certificate> listCert = new ArrayList<X509Certificate>();
		List<CertificateDTO> listCertDTO = new ArrayList<CertificateDTO>();
		
		listCert = certificateRepository.getCertificates();
		
		for (X509Certificate cert : listCert) {
			if(cert.getBasicConstraints() == -1) {			
				if(checkValidity(cert.getSerialNumber().toString())) {
					listCertDTO.add(new CertificateDTO(cert,""));
				}
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

			X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData, true, null);	

			certificateRepository.saveCertificateSELFSIGNED(certificate.getSerialNumber().toString(), keyPair.getPrivate(), certificate);
		
			return new CertificateDTO(certificate,"");
		} catch(Exception e) {
			return null;
		}
	}
	
	// dodaje sertifikat, ako je isCa true, dodaj intermediate sertifikat, ako je false, dodaj end sertifikat
	public X509Certificate addCetrificate(String issuerSerialNumber, CertificateNewDTO newCert, boolean isCa,String holderType) {
		IssuerData issuerData = certificateRepository.getIssuerData(issuerSerialNumber);
		List<CertificateHolderType> holderTypeList = certificateHolderTypeRepository.findAll();
		
		// ovo mi treba za proveru datuma validnosti, u odnosu na issuera
		X509Certificate cert = certificateRepository.getCertificate(issuerSerialNumber).orElse(null);
		if(cert == null) {
			return null;
		}
				
		if (issuerData == null || getOne(issuerSerialNumber).getBasicConstraints() == -1) {
			return null; // vrati bad request
		}
		
		if(holderType == null) return null;
		if(!holderType.equals("Servis") && !holderType.equals("Podsistem") && !holderType.equals("Korisnik")) return null;
		

		try {
			X500Name x500name = CertificateUtil.generateX500Name(newCert);
			KeyPair keyPair = CertificateUtil.generateKeyPair();
			
			LocalDate dt = LocalDate.parse(newCert.getExpirationDate());
			
			SubjectData subjectData = new SubjectData(keyPair.getPublic(), x500name, LocalDate.now(), dt);
			
			X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData, isCa, cert.getNotAfter());
			
			if(isCa == true) {
				certificateRepository.saveCertificateINTERMEDIATE(certificate.getSerialNumber().toString(), keyPair.getPrivate(), certificate);
			} else {
				certificateRepository.saveCertificateEND(certificate.getSerialNumber().toString(), keyPair.getPrivate(), certificate);
			}
			
			
			CertificateHolderType certHolderType =  new CertificateHolderType(certificate.getSerialNumber().toString(),holderType);
			holderTypeList.add(certHolderType);
			certificateHolderTypeRepository.saveAll(holderTypeList);
			
			
			return certificate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void revokeCertificate(String serialNumber) {		
		X509Certificate certificate = getOne(serialNumber);
		
		List<RevokedCertificate> revokedCertificates = revokedCertificateRepository.findAll();
		
//		for(RevokedCertificate revokedCertificate: revokedCertificates) {
//			System.out.println("******");
//			System.out.println("revokedCertificate serial number: " + revokedCertificate.getSerialNumber());
//			System.out.println("******");
//		}
		
		//vec se nalazi u listi
		for(RevokedCertificate rc : revokedCertificates) {
			if(certificate.getSerialNumber().toString().equals(rc.getSerialNumber())) {				
				return;
			}
		}
		// name certifikata koji se ponistava
		String[] split = certificate.getSubjectX500Principal().getName().split(",");
		String issuer = split[0];
		
		//svi sertifikati
		List<CertificateDTO> allCertificates = getAll();
		//sva deca certifikata kog povlacim
		List<CertificateDTO> revokeList = allCertificates
				.stream()
				.filter(c -> c.getIssuerName().equals(issuer))
				.collect(Collectors.toList());
		
		
//		System.out.println("***** broj dece: " + revokeList.size());		
//		for(CertificateDTO revokedCertificate: revokeList) {
//			System.out.println("************");
//			System.out.println("DETE serial number: " + revokedCertificate.getSerialNumber());
//			System.out.println("************");
//		}
		
		//svi sertifikati bez dece
		allCertificates.removeAll(revokeList);
		
		findCertsToRevoke(revokedCertificates, revokeList, allCertificates);
				
//		for(RevokedCertificate revokedCertificate: revokedCertificates) {
//			System.out.println("************");
//			System.out.println("DETE serial number: " + revokedCertificate.getSerialNumber());
//			System.out.println("DETE serial number: " + revokedCertificate.getIssuerName());
//			System.out.println("************");
//		}
		
		//dodajem ovog koji se povlaci
		RevokedCertificate rcert = new RevokedCertificate(certificate.getSerialNumber().toString(), issuer);
		revokedCertificates.add(rcert);
		//i njegovu prvu decu
		for(CertificateDTO cDTO : revokeList) {
			RevokedCertificate rcert2 = new RevokedCertificate(cDTO.getSerialNumber().toString(), cDTO.getIssuerName() );
			revokedCertificates.add(rcert2);
		}
//		System.out.println("///////////////////////// BROJ: revokedCertificates: " + revokedCertificates.size());
//		for(RevokedCertificate revokedCertificate: revokedCertificates) {
//			System.out.println("************");
//			System.out.println("DETE serial number: " + revokedCertificate.getSerialNumber());
//			System.out.println("DETE serial number: " + revokedCertificate.getIssuerName());
//			System.out.println("************");
//		}
//		for(RevokedCertificate revCert: revokedCertificates) {
//			revokedCertificateRepository.save(revCert);
//		}
		revokedCertificateRepository.saveAll(revokedCertificates);
		
	}
	
	/**
	 * 
	 * @param revokedCertificates - lista vec povucenih sertifikata, PLUS novi sertifikati koji se povlace
	 * @param revokeList - lista dece(od trenurnog sertifikata) koja ce se revoke-ovati 
	 * @param allCertificates - sertifikati iz .jks fajla BEZ revokeList sertifikata
	 */
	public void findCertsToRevoke(List<RevokedCertificate> revokedCertificates, List<CertificateDTO> revokeList, List<CertificateDTO> allCertificates) {		
		
//		int revokedCertificatesSizeBefore = revokedCertificates.size();
		
		for(CertificateDTO revokeCert : revokeList) {
			List<CertificateDTO> childRevokeList = allCertificates
					.stream()
					.filter(c -> c.getIssuerName().equals(revokeCert.getSubjectName()))
					.collect(Collectors.toList());
					
//			System.out.println("++++++CHILD REVOKE LIST SIZE: " + childRevokeList.size());
			for(CertificateDTO cDTO : childRevokeList) {
				RevokedCertificate rcert3 = new RevokedCertificate(cDTO.getSerialNumber().toString(), cDTO.getIssuerName());
				revokedCertificates.add(rcert3);
			}
			
			allCertificates.removeAll(childRevokeList);
			
			//ako ne izbacujem nikog iz ovog chain-a return
			//ipak ne koristiti ovo
//			if(revokedCertificatesSizeBefore == revokedCertificates.size() ) {
//				System.out.println("-------RETURN U REKURZIJI");
//				return;
//			}
			
			findCertsToRevoke(revokedCertificates, childRevokeList, allCertificates);
			
		}
	}

	public boolean checkValidity(String serialNumber) {
		X509Certificate certificate = getOne(serialNumber);
		List<RevokedCertificate> certificates = revokedCertificateRepository.findAll();
		
		if(certificate == null) {
			return false;
		}
		
		try {
			certificate.checkValidity();
		} catch (CertificateExpiredException | CertificateNotYetValidException  e) {
			System.out.println("---PROVERA SERTIFIKATA: NE VALJA DATUM!");
			return false;
		} 
		
		for (RevokedCertificate revocedCert : certificates) {
			if (revocedCert.getSerialNumber().equals(certificate.getSerialNumber().toString())) {
				System.out.println("---PROVERA SERTIFIKATA: POVUCEN JE!");
				return false;
			}
		} 
		
		return true;
	}
	
}
