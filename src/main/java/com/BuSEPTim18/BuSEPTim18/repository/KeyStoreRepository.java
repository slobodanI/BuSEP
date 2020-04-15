package com.BuSEPTim18.BuSEPTim18.repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.stereotype.Repository;

import com.BuSEPTim18.BuSEPTim18.model.IssuerData;


@Repository
public class KeyStoreRepository {
	
	private KeyStore keyStoreEND;
	private KeyStore keyStoreINTERMEDIATE;
	private KeyStore keyStoreSELFSIGNED;
	//odlucili smo se da imamo jedan fajl zbog jednostavnosti
	private final String keyStorefileEND = "./pki/end.jks";
	private final String keyStorefileINTERMEDIATE = "./pki/intermediate.jks";
	private final String keyStorefileSELFSIGNED = "./pki/selfsigned.jks";
	private final String keyStorePassword = "password";
	
	public KeyStoreRepository() {
		try {
			keyStoreEND = KeyStore.getInstance("JKS", "SUN");
			keyStoreINTERMEDIATE = KeyStore.getInstance("JKS", "SUN");
			keyStoreSELFSIGNED = KeyStore.getInstance("JKS", "SUN");
			loadKeyStore();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void saveCertificateEND(String alias, PrivateKey privateKey, Certificate certificate) throws Exception {
		keyStoreEND.setKeyEntry(alias, privateKey, keyStorePassword.toCharArray(), new Certificate[] {certificate});
		saveKeyStoreEND();
	}
	
	public void saveCertificateINTERMEDIATE(String alias, PrivateKey privateKey, Certificate certificate) throws Exception {
		keyStoreINTERMEDIATE.setKeyEntry(alias, privateKey, keyStorePassword.toCharArray(), new Certificate[] {certificate});
		saveKeyStoreINTERMEDIATE();
	}
	
	public void saveCertificateSELFSIGNED(String alias, PrivateKey privateKey, Certificate certificate) throws Exception {
		keyStoreSELFSIGNED.setKeyEntry(alias, privateKey, keyStorePassword.toCharArray(), new Certificate[] {certificate});
		saveKeyStoreSELFSIGNED();
	}
	
	public List<X509Certificate> getCertificates() {	
		List<X509Certificate> certificates = new ArrayList<>();
		
		try {
			loadKeyStore();
			Enumeration<String> aliasesEND = keyStoreEND.aliases();
			
			while (aliasesEND.hasMoreElements()) {
				String aliasEND = aliasesEND.nextElement();
				
				if (keyStoreEND.isKeyEntry(aliasEND)) {		
					certificates.add(getCertificate(aliasEND).get());
					
				}
			}
			
			Enumeration<String> aliasesINTERMEDIATE = keyStoreINTERMEDIATE.aliases();
			
			while (aliasesINTERMEDIATE.hasMoreElements()) {
				String aliasINTERMEDIATE = aliasesINTERMEDIATE.nextElement();
				
				if (keyStoreINTERMEDIATE.isKeyEntry(aliasINTERMEDIATE)) {		
					certificates.add(getCertificate(aliasINTERMEDIATE).get());
					
				}
			}
			
			Enumeration<String> aliasesSELFSIGNED = keyStoreSELFSIGNED.aliases();
			
			while (aliasesSELFSIGNED.hasMoreElements()) {
				String aliasSELFSIGNED = aliasesSELFSIGNED.nextElement();
				
				if (keyStoreSELFSIGNED.isKeyEntry(aliasSELFSIGNED)) {		
					certificates.add(getCertificate(aliasSELFSIGNED).get());
					
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return certificates;
	}
	
	public Optional<X509Certificate> getCertificate(String alias) {
		try {
			loadKeyStore();
			X509Certificate certEND = (X509Certificate) keyStoreEND.getCertificate(alias);
			X509Certificate certINTERMEDIATE = (X509Certificate) keyStoreINTERMEDIATE.getCertificate(alias);
			X509Certificate certSELFSIGNED = (X509Certificate) keyStoreSELFSIGNED.getCertificate(alias);
			
			if(certEND != null) {
				return Optional.ofNullable(certEND);
			}
			
			if(certINTERMEDIATE != null) {
				return Optional.ofNullable(certINTERMEDIATE);
			}
			
			if(certSELFSIGNED != null) {
				return Optional.ofNullable(certSELFSIGNED);
			}
			
			// ako su svi null vrati bilo koji, nije bitno, kasnije ce biti null
			return Optional.ofNullable(certEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public IssuerData getIssuerData(String issuerSerialNumber) {
		try {
			loadKeyStore();
			//mozda dotati cert.getBasicCostraints != -1
//			X509Certificate cert = (X509Certificate) keyStore.getCertificate(issuerSerialNumber);
//			PrivateKey privateKey = (PrivateKey) keyStore.getKey(issuerSerialNumber, keyStorePassword.toCharArray());
			
			// serialNmber je isti kao alias
			X509Certificate cert = getCertificate(issuerSerialNumber).orElse(null);
			PrivateKey privateKey1 = (PrivateKey) keyStoreSELFSIGNED.getKey(issuerSerialNumber, keyStorePassword.toCharArray());
			PrivateKey privateKey2 = (PrivateKey) keyStoreINTERMEDIATE.getKey(issuerSerialNumber, keyStorePassword.toCharArray());
			
			PrivateKey privateKey = null;
			
			if(privateKey1 != null) {
				privateKey = privateKey1;
			} else if(privateKey2 != null) {
				privateKey = privateKey2;
			}
						
			if (cert == null || privateKey == null) {
				return null;
			}
			
			X500Name issuerName = new JcaX509CertificateHolder(cert).getSubject();
			
			return new IssuerData(privateKey, issuerName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void saveKeyStoreEND() throws Exception {
		keyStoreEND.store(new FileOutputStream(keyStorefileEND), keyStorePassword.toCharArray());		
	}	
	
	private void saveKeyStoreINTERMEDIATE() throws Exception {
		keyStoreINTERMEDIATE.store(new FileOutputStream(keyStorefileINTERMEDIATE), keyStorePassword.toCharArray());
	}
	
	private void saveKeyStoreSELFSIGNED() throws Exception {
		keyStoreSELFSIGNED.store(new FileOutputStream(keyStorefileSELFSIGNED), keyStorePassword.toCharArray());
	}
	
	private void loadKeyStore() throws Exception {
		keyStoreEND.load(new FileInputStream(keyStorefileEND), keyStorePassword.toCharArray());
		keyStoreINTERMEDIATE.load(new FileInputStream(keyStorefileINTERMEDIATE), keyStorePassword.toCharArray());
		keyStoreSELFSIGNED.load(new FileInputStream(keyStorefileSELFSIGNED), keyStorePassword.toCharArray());
	}
}
