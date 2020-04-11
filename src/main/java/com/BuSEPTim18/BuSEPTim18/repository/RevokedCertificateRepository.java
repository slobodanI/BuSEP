package com.BuSEPTim18.BuSEPTim18.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.BuSEPTim18.BuSEPTim18.model.RevokedCertificate;

@Repository
public interface RevokedCertificateRepository extends JpaRepository<RevokedCertificate, Integer> {
	
	List<RevokedCertificate> findAll();
	
//	@Query("SELECT rc FROM revokedcertificate rc WHERE rc.serialnumber = ?1")
//	RevokedCertificate findBySerialNumber(String serialNumber);
	
	RevokedCertificate findBySerialNumber(String serialNumber);
	
	
	
	
}
