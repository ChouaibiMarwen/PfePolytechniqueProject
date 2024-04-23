package com.camelsoft.rayaserver.Models.Chat;



import com.camelsoft.rayaserver.Enum.Project.Notification.MessageStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
    @Column(name = "content")
    private String content;
    @Column(name = "timestamp")
    private Date timestamp;
    @Column(name = "status")
    private MessageStatus status = MessageStatus.SENDING;

    public ChatMessage() {
        this.timestamp = new Date();
    }

    public ChatMessage(Long senderId, Long recipientId, String senderName, String recipientName, String content) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.content = content;
        this.timestamp = new Date();
    }


}
