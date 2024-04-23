package com.camelsoft.rayaserver.Repository.Tools;


import com.camelsoft.rayaserver.Enum.Project.Notification.MessageStatus;
import com.camelsoft.rayaserver.Models.Notification.Notification;

import com.camelsoft.rayaserver.Models.User.users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Page<Notification> findAllByReciver(Pageable pageable, users user);
    Page<Notification> findAllByReciverAndStatus(Pageable pageable, users user, MessageStatus status);

}
