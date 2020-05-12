package com.BuSEPTim18.BuSEPTim18.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BuSEPTim18.BuSEPTim18.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername( String username );
}

