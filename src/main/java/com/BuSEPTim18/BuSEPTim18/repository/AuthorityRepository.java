package com.BuSEPTim18.BuSEPTim18.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BuSEPTim18.BuSEPTim18.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	Authority findByName(String name);
}
