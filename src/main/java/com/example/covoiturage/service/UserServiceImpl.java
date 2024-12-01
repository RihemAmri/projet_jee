package com.example.covoiturage.service;
import com.example.covoiturage.entity.AppUser;
import com.example.covoiturage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;
    @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appuser = userRepository.findByEmail(email);
        System.out.println(appuser.toString());
   if(appuser != null) {
    var springUser = User.withUsername (appuser.getEmail())
            .password (appuser.getPassword())
            .roles (appuser.getRole())
            .build();
    return springUser;
}
   System.out.println("hani linaaaa");
        return null;
    }
}