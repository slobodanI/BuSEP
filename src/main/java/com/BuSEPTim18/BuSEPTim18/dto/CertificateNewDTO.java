package com.BuSEPTim18.BuSEPTim18.dto;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CertificateNewDTO {
	
	@NotNull
	private String commonName;
	@NotNull
	private String givenname;
	@NotNull
	private String surname;
	@NotNull
	private String organization;
	@NotNull
	private String organizationalUnit;
	@NotNull
	@Size(min = 2, max = 2)
	private String countryCode;
	@NotNull
	@Email
	private String email;
	@NotNull
	
	public CertificateNewDTO() {
		// TODO Auto-generated constructor stub
	}
	
	private Date expirationDate;
	public CertificateNewDTO(@NotNull String commonName, @NotNull String givenname, @NotNull String surname,
			@NotNull String organization, @NotNull String organizationalUnit,
			@NotNull @Size(min = 2, max = 2) String countryCode, @NotNull @Email String email,
			@NotNull Date expirationDate) {
		super();
		this.commonName = commonName;
		this.givenname = givenname;
		this.surname = surname;
		this.organization = organization;
		this.organizationalUnit = organizationalUnit;
		this.countryCode = countryCode;
		this.email = email;
		this.expirationDate = expirationDate;
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
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
				  	
	
}
