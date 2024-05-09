package com.camelsoft.rayaserver.Models.Chat;


import com.camelsoft.rayaserver.Models.File.File_model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ChatNotification")
public class ChatNotification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "profile_image")
    private String profileimage;
    @Column(name = "senderName")
    private String senderName;
    @Column(name = "message")
    private String message;
    @Column(name = "sender_id")
    private Long senderid;
    @Column(name = "chatId")
    private String chatId;

    @OneToMany
    private List<File_model> attachments;
    @Column(name = "timestmp")
    private Date timestmp;

    public ChatNotification() {
    }
}
