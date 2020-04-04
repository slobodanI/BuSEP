package com.BuSEPTim18.BuSEPTim18.dto;

import java.math.BigInteger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import com.BuSEPTim18.BuSEPTim18.model.CertificateStatus;


public class CertificateDTO {
	
	private String commonName;
	private String givenname;
	private String surname;
	private String organization;
	private String organizationalUnit;
	private String countryCode;
	private String email;
	private BigInteger serialNumber;
	private CertificateStatus status;
	
	public CertificateDTO(X509Certificate certificate) {
		try {
			X500Name name = new JcaX509CertificateHolder(certificate).getSubject();
			RDN[] rnds = name.getRDNs();

			for (RDN rdn: rnds) {
				AttributeTypeAndValue[] values = rdn.getTypesAndValues();
				for (AttributeTypeAndValue val : values) {
					if (val.getType().equals(BCStyle.CN)) {
						commonName = val.getValue().toString();
					} else if (val.getType().equals(BCStyle.GIVENNAME)) {
						givenname = val.getValue().toString();
					} else if (val.getType().equals(BCStyle.SURNAME)) {
						surname = val.getValue().toString();
					} else if (val.getType().equals(BCStyle.O)) {
						organization = val.getValue().toString();
					} else if (val.getType().equals(BCStyle.OU)) {
						organizationalUnit = val.getValue().toString();
					} else if (val.getType().equals(BCStyle.C)) {
						countryCode = val.getValue().toString();
					} else if (val.getType().equals(BCStyle.E)) {
						email = val.getValue().toString();
					}
				}
			}

			serialNumber = certificate.getSerialNumber();
		} catch (CertificateEncodingException e) {
			e.printStackTrace();
		}
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getGivenname() {
		return givenname;
	}

	public void setGivenname(String givenname) {
		this.givenname = givenname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOrganizationalUnit() {
		return organizationalUnit;
	}

	public void setOrganizationalUnit(String organizationalUnit) {
		this.organizationalUnit = organizationalUnit;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigInteger getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(BigInteger serialNumber) {
		this.serialNumber = serialNumber;
	}

	public CertificateStatus getStatus() {
		return status;
	}

	public void setStatus(CertificateStatus status) {
		this.status = status;
	}
	
	
	
}
