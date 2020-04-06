package com.BuSEPTim18.BuSEPTim18.utils;

import java.math.BigInteger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import com.BuSEPTim18.BuSEPTim18.model.IssuerData;
import com.BuSEPTim18.BuSEPTim18.model.SubjectData;
import com.BuSEPTim18.BuSEPTim18.repository.KeyStoreRepository;


public class CertificateGenerator {
	
	private static long serialNumber = 0;
	
	public CertificateGenerator() {
		serialNumber = new KeyStoreRepository().getCertificates().size();
	}
	
	public X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, boolean isCA) throws CertIOException, ParseException {
		try {
			
			JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
			builder = builder.setProvider("BC");
			ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());
			
			serialNumber++;
			//lokacija sertifikata, na netu kaze da ne mora za CA da se koristi, ali ne smeta
			GeneralName location = new GeneralName(GeneralName.uniformResourceIdentifier, new DERIA5String("http://localhost:8080/api/certificate/" + serialNumber));
			
			SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = iso8601Formater.parse(subjectData.getStartDate().toString());
			Date endDate = iso8601Formater.parse(subjectData.getEndDate().toString());
			
			//Postavljaju se podaci za generisanje sertifiakta
			X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
					issuerData.getX500name(),
					new BigInteger(Long.toString(serialNumber)),
					startDate,
					endDate,
					subjectData.getX500name(),
					subjectData.getPublicKey())
					.addExtension(Extension.basicConstraints, true, new BasicConstraints(isCA)) // da li je certificat CA
					.addExtension(Extension.authorityInfoAccess, false, location)
					/*.addExtension(Extension.keyUsage, false, new KeyUsage(usage))*/; 
			
			//Generise se sertifikat
			X509CertificateHolder certHolder = certGen.build(contentSigner);

			//Builder generise sertifikat kao objekat klase X509CertificateHolder
			//Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se koristi certConverter
			JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
			certConverter = certConverter.setProvider("BC");

			//Konvertuje objekat u sertifikat
			return certConverter.getCertificate(certHolder);
		} catch (CertificateEncodingException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (OperatorCreationException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
