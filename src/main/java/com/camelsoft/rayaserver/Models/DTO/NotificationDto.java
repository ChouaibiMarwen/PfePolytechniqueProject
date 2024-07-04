package com.camelsoft.rayaserver.Models.DTO;

import com.camelsoft.rayaserver.Enum.Project.Notification.MessageStatus;
import com.camelsoft.rayaserver.Enum.Tools.Action;

import com.camelsoft.rayaserver.Models.Notification.Notification;
import com.camelsoft.rayaserver.Models.User.users;
import lombok.Data;
import java.util.Date;

import static com.camelsoft.rayaserver.Models.DTO.UserShortDto.mapToUserShortDTO;

@Data
public class NotificationDto {
    private Long id;
    private UserShortDto sender;
    private UserShortDto reciver;
    private MessageStatus status= MessageStatus.WAITING;
    private Action action=Action.IDLE;
    private Long actionId;
    private String subject ;
    private String content ;
    private Date timestmp;



    public static NotificationDto NotificationToDto(Notification notification)  {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(notification.getId());
        notificationDto.setSender(mapToUserShortDTO(notification.getSender()));
        notificationDto.setReciver(mapToUserShortDTO(notification.getReciver()));
        notificationDto.setStatus(notification.getStatus());
        notificationDto.setAction(notification.getAction());
        notificationDto.setActionId(notification.getActionId());
        notificationDto.setSubject(notification.getSubject());
        notificationDto.setContent(notification.getContent());
        notificationDto.setTimestmp(notification.getTimestmp());

        return notificationDto;

    }




}
