package com.BuSEPTim18.BuSEPTim18.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import com.BuSEPTim18.BuSEPTim18.dto.CertificateNewDTO;


public class CertificateUtil {
		
	public static X500Name generateX500Name(CertificateNewDTO newCert) {
		
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
	    builder.addRDN(BCStyle.CN, newCert.getCommonName());
	    builder.addRDN(BCStyle.SURNAME, newCert.getSurname());
	    builder.addRDN(BCStyle.GIVENNAME, newCert.getGivenname());
	    builder.addRDN(BCStyle.O, newCert.getOrganization());
	    builder.addRDN(BCStyle.OU, newCert.getOrganizationalUnit());
	    builder.addRDN(BCStyle.C, newCert.getCountryCode());
	    builder.addRDN(BCStyle.E, newCert.getEmail());
	    //UID (USER ID) je ID korisnika
	    builder.addRDN(BCStyle.UID, UUID.randomUUID().toString());
		
	    return builder.build();
    }
		
	public static KeyPair generateKeyPair() {
        try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); 
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(2048, random);
			return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
        return null;
	}
}
