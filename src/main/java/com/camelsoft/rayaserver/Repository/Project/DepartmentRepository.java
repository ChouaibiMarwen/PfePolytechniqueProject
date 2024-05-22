package com.camelsoft.rayaserver.Repository.Project;
import com.camelsoft.rayaserver.Models.Project.Department;
import com.camelsoft.rayaserver.Models.Project.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findAllByNameContainingIgnoreCaseAndArchiveIsFalse(String name);
    List<Department> findByArchiveIsFalse();
}
