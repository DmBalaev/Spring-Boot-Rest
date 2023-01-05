package com.dm.rest.persistance.repository;

import com.dm.rest.persistance.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findById(Long id);
    boolean existsByName(String name);
}
