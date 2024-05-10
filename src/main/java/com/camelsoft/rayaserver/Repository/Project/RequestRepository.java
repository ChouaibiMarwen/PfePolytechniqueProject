package com.camelsoft.rayaserver.Repository.Project;

import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Enum.Project.Request.RequestState;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.camelsoft.rayaserver.Models.Project.Request;
import com.camelsoft.rayaserver.Models.User.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Page<Request> findAllByStatusAndArchiveIsFalse(Pageable page, RequestState status);
    Page<Request> findAllByStatusAndCreatorrequest_RoleArchiveIsFalse(Pageable page, RequestState status, Role role);
    Page<Request> findAllByCreatorrequest_RoleArchiveIsFalse(Pageable page,Role role);
    Page<Request> findAllByArchiveIsFalse(Pageable page);


}
