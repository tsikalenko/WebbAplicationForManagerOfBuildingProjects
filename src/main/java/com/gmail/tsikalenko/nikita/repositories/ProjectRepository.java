package com.gmail.tsikalenko.nikita.repositories;

import com.gmail.tsikalenko.nikita.entities.Manager;
import com.gmail.tsikalenko.nikita.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    ArrayList<Project> findAllByManager(Manager manager);
    Optional<Project> findByNameAndManager(String name, Manager manager);
}
