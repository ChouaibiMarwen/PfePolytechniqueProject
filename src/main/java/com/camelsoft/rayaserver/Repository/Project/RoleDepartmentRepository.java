package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Models.Project.Department;
import com.camelsoft.rayaserver.Models.Project.RoleDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDepartmentRepository extends JpaRepository<RoleDepartment, Long> {

    List<RoleDepartment> findByArchiveIsFalse();
    List<RoleDepartment> findByArchiveIsFalseAndDepartment(Department department);

}
