package com.BuSEPTim18.BuSEPTim18.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name= "CertificateHolderType")
public class CertificateHolderType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "serialnumber")
    protected String serialNumber;
    
    @Column(name="holderType")
    protected String holderType;
    
    public CertificateHolderType() {
    	
    }

	public CertificateHolderType(String serialNumber, String holderType) {
		super();
		this.serialNumber = serialNumber;
		this.holderType = holderType;
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


	public String getHolderType() {
		return holderType;
	}

	public void setHolderType(String holderType) {
		this.holderType = holderType;
	}
    
    

}
