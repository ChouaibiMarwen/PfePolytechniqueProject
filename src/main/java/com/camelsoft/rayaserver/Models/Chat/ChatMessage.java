package com.camelsoft.rayaserver.Models.Chat;


import com.camelsoft.rayaserver.Enum.Project.Notification.MessageStatus;
import com.camelsoft.rayaserver.Models.File.MediaModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ChatMessage")
@Data
public class ChatMessage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "chatId")
    private String chatId;
    @Column(name = "senderId")
    private Long senderId;
    @Column(name = "recipientId")
    private Long recipientId;
    @Column(name = "senderName")
    private String senderName;
    @Column(name = "recipientName")
    private String recipientName;
    @Column(name = "senderprofileimage")
    private String senderprofileimage;
    @Column(name = "recipientprofileimage")
    private String recipientprofileimage;
    @Column(columnDefinition = "TEXT",name = "content")
    private String content;
    @Column(name = "timestamp")
    private Date timestamp = new Date();
    @Column(name = "status")
    private MessageStatus status = MessageStatus.SENDING;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaModel> attachments = new ArrayList<>();


}
