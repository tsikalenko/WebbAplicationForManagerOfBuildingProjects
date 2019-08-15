package com.gmail.tsikalenko.nikita;

import com.gmail.tsikalenko.nikita.entities.Manager;
import com.gmail.tsikalenko.nikita.entities.Role;
import com.gmail.tsikalenko.nikita.repositories.ManagerRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    ManagerRepository managerRepository;

    public UserDetails loadUserByUsername(@NonNull String login) throws UsernameNotFoundException {
        Manager manager = managerRepository.findByUsername(login).get();
        if (manager == null) {
            throw new UsernameNotFoundException(login);
        }
        List role = new ArrayList();
        role.add(Role.USER);
        manager.setAuthorities(role);
        manager.setAccountNonExpired(true);
        manager.setAccountNonLocked(true);
        manager.setCredentialsNonExpired(true);
        manager.setEnabled(true);
        return manager;
    }
}
