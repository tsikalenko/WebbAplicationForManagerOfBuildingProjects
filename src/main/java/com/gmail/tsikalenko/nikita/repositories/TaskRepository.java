package com.gmail.tsikalenko.nikita.repositories;

import com.gmail.tsikalenko.nikita.entities.Project;
import com.gmail.tsikalenko.nikita.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    ArrayList<Task> findAllByProject(Project project);
}
