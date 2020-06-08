package com.BuSEPTim18.BuSEPTim18.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.BuSEPTim18.BuSEPTim18.model.Authority;
import com.BuSEPTim18.BuSEPTim18.model.User;
import com.BuSEPTim18.BuSEPTim18.model.UserRequest;
import com.BuSEPTim18.BuSEPTim18.repository.UserRepository;


@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthorityService authService;

	
	public User findByUsername(String username) throws UsernameNotFoundException {
		User u = userRepository.findByUsername(username);
		return u;
	}

	public User findById(Long id) throws AccessDeniedException {
		User u = userRepository.findById(id).orElseGet(null);
		return u;
	}

	public List<User> findAll() throws AccessDeniedException {
		List<User> result = userRepository.findAll();
		return result;
	}

	
	public User save(UserRequest userRequest) {
		User u = new User();
		u.setUsername(userRequest.getUsername());
		u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		u.setFirstName(userRequest.getFirstname());
		u.setLastName(userRequest.getLastname());
		//u.setEmail(userRequest.getE);
		u.setEnabled(true);
		
		List<Authority> auth = authService.findByname("ROLE_USER");
		u.setAuthorities(auth);
		
		u = this.userRepository.save(u);
		return u;
	}

}
