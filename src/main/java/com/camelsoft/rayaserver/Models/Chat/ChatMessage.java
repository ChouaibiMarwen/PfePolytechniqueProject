package com.camelsoft.rayaserver.Models.Chat;



import com.camelsoft.rayaserver.Enum.Project.Notification.MessageStatus;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.User.users;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @OneToOne(fetch = FetchType.EAGER)
    private users sender;
    @OneToOne(fetch = FetchType.EAGER)
    private users recipient;
    @Column(name = "senderName")
    private String senderName;
    @Column(name = "recipientName")
    private String recipientName;
    @Column(name = "content")
    private String content;
    @Column(name = "timestamp")
    private Date timestamp = new Date();
    @Column(name = "status")
    private MessageStatus status = MessageStatus.SENDING;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private List<File_model> attachments=new ArrayList<>();




}
