package com.camelsoft.rayaserver.Request.chat;


import com.camelsoft.rayaserver.Models.File.MediaModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "ChatMessageRequest")
public class ChatMessageRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "senderId")
    private Long senderId;
    @Column(name = "recipientId")
    private Long recipientId;

    @Column(name = "content")
    private String content;
    @OneToMany
    private List<MediaModel> attachments=new ArrayList<>();

}
