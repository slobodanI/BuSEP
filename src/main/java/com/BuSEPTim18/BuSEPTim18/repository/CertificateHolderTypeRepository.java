package com.BuSEPTim18.BuSEPTim18.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BuSEPTim18.BuSEPTim18.model.CertificateHolderType;

public interface CertificateHolderTypeRepository extends JpaRepository<CertificateHolderType, Integer>{
	List<CertificateHolderType> findAll();
	
	CertificateHolderType findBySerialNumber(String serialNumber);
}
