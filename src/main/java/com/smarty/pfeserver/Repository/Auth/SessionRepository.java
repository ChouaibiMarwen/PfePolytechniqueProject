package com.smarty.pfeserver.Repository.Auth;



import com.smarty.pfeserver.Models.Auth.SessionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<SessionModel, Long> {
    List<SessionModel> findAllByEmail(String id);
    List<SessionModel> findAllByUsername(String id);
    List<SessionModel> findAllByUserid(int id);
    void deleteById(int id);
    void deleteAllByUserid(int userid);
}
