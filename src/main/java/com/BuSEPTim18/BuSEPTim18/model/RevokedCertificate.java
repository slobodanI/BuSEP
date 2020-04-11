package com.BuSEPTim18.BuSEPTim18.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity(name = "revokedcertificate")
public class RevokedCertificate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "serialnumber")
    protected String serialNumber;

//    @Column(name = "issuerserialnumber")
//    protected String issuerSerialnumber;
    
    @Column(name = "issuername")
    protected String issuerName;
	
    public RevokedCertificate() {
		// TODO Auto-generated constructor stub
	}

//	public RevokedCertificate(String serialNumber, String issuerSerialnumber, String issuerName) {
//		this.serialNumber = serialNumber;
//		this.issuerSerialnumber = issuerSerialnumber;
//		this.issuerName = issuerName;
//	}
	
	public RevokedCertificate(String serialNumber, String issuerName) {
		this.serialNumber = serialNumber;
		this.issuerName = issuerName;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

//	public String getIssuerSerialnumber() {
//		return issuerSerialnumber;
//	}
//
//	public void setIssuerSerialnumber(String issuerSerialnumber) {
//		this.issuerSerialnumber = issuerSerialnumber;
//	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}   
    
	
}
