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
	
	private KeyStore keyStore;
	//odlucili smo se da imamo jedan fajl zbog jednostavnosti
	private final String keyStorefile = "./pki/keystore.jks";
	private final String keyStorePassword = "asdasd";
	
	public KeyStoreRepository() {
		try {
			keyStore = KeyStore.getInstance("JKS", "SUN");
			loadKeyStore();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void saveCertificate(String alias, PrivateKey privateKey, Certificate certificate) throws Exception {
		keyStore.setKeyEntry(alias, privateKey, keyStorePassword.toCharArray(), new Certificate[] {certificate});
		saveKeyStore();
	}
	
	public List<X509Certificate> getCertificates() {	
		List<X509Certificate> certificates = new ArrayList<>();
		
		try {
			loadKeyStore();
			Enumeration<String> aliases = keyStore.aliases();
			
			while (aliases.hasMoreElements()) {
				String alias = aliases.nextElement();
				
				if (keyStore.isKeyEntry(alias)) {		
					certificates.add(getCertificate(alias).get());
					
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
			X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);
			return Optional.ofNullable(cert);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public IssuerData getIssuerData(String issuerSerialNumber) {
		try {
			loadKeyStore();
			//mozda dotati cert.getBasicCostraints != -1
			X509Certificate cert = (X509Certificate) keyStore.getCertificate(issuerSerialNumber);
			PrivateKey privateKey = (PrivateKey) keyStore.getKey(issuerSerialNumber, keyStorePassword.toCharArray());
			
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
	
	private void saveKeyStore() throws Exception {
		keyStore.store(new FileOutputStream(keyStorefile), keyStorePassword.toCharArray());
	}
	
	private void loadKeyStore() throws Exception {
		keyStore.load(new FileInputStream(keyStorefile), keyStorePassword.toCharArray());
	}
}
