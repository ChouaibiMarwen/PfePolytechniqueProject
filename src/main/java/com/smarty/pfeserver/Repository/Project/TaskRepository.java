package com.smarty.pfeserver.Repository.Project;

import com.smarty.pfeserver.Models.Project.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
