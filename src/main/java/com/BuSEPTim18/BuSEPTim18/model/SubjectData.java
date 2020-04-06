package com.BuSEPTim18.BuSEPTim18.model;

import java.security.PublicKey;
import java.time.LocalDate;

import org.bouncycastle.asn1.x500.X500Name;

public class SubjectData {
	
	private PublicKey publicKey;
	private X500Name x500name;
	private LocalDate startDate;
	private LocalDate endDate;

	public SubjectData(PublicKey publicKey, X500Name x500name, LocalDate startDate, LocalDate endDate) {
		this.publicKey = publicKey;
		this.x500name = x500name;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public X500Name getX500name() {
		return x500name;
	}

	public void setX500name(X500Name x500name) {
		this.x500name = x500name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	
	
}
