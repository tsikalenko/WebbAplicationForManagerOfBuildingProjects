package com.gmail.tsikalenko.nikita.repositories;

import com.gmail.tsikalenko.nikita.entities.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByUsername(String login);
    Manager findByPassword(String username);
}
