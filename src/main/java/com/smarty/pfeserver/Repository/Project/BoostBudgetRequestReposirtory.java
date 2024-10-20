package com.smarty.pfeserver.Repository.Project;

import com.smarty.pfeserver.Models.Project.BoostBudgetRequest;
import com.smarty.pfeserver.Models.User.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoostBudgetRequestReposirtory extends JpaRepository<BoostBudgetRequest, Long> {

    List<BoostBudgetRequest> findAllByCreatedbyOrderByTimestampDesc(users user);
}
