package com.camelsoft.rayaserver.Repository.Tools;

import com.camelsoft.rayaserver.Models.Notification.AdminNotification;
import com.camelsoft.rayaserver.Models.User.users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminNotificationRepository extends JpaRepository<AdminNotification,Long> {
    Page<AdminNotification> findAllByReciverAndSenderOrderByTimestmpDesc(Pageable pageable, users reciver, users sender);
    Page<AdminNotification> findAllByReciverOrderByTimestmpDesc(Pageable pageable, users reciver);
}
