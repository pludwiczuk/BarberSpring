package com.spring.barber.auth;

import java.util.ArrayList;
import java.util.List;

import com.spring.barber.user.Role;
import com.spring.barber.user.UserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.spring.barber.user.User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("Username not found");
		}

		Role role = user.getRole();
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role.toString()));

		return new User(user.getUsername(), user.getPassword(), authorities);
		
	}

}