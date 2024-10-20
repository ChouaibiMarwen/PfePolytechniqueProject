package com.smarty.pfeserver.Repository.Tools;


import com.smarty.pfeserver.Enum.Project.Notification.MessageStatus;
import com.smarty.pfeserver.Models.Notification.Notification;

import com.smarty.pfeserver.Models.User.users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Page<Notification> findAllByReciver(Pageable pageable, users user);
    List<Notification> findAllByReciver(users user);
    List<Notification> findAllByReciverAndStatusNot(users user, MessageStatus status);
    Page<Notification> findAllByReciverAndStatus(Pageable pageable, users user, MessageStatus status);

}
