package com.camelsoft.rayaserver.Models.Notification;




import com.camelsoft.rayaserver.Enum.Project.Notification.MessageStatus;
import com.camelsoft.rayaserver.Enum.Tools.Action;
import com.camelsoft.rayaserver.Models.Chat.ChatMessage;
import com.camelsoft.rayaserver.Models.User.users;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "notification_model")
@Data
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "notification_id")
    private Long id;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER,orphanRemoval = true)
    private users sender;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER,orphanRemoval = true)
    private users reciver;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private ChatMessage chat;

    @Column(name = "status")
    private MessageStatus status= MessageStatus.WAITING;
    @Column(name = "action")
    private Action action=Action.IDLE;
    @Column(name = "timestmp")
    private Date timestmp;

    public Notification() {
        this.timestmp=new Date();
    }

    public Notification(users sender, users reciver, ChatMessage chat, Action action) {
        this.sender = sender;
        this.reciver = reciver;
        this.chat = chat;
        this.action = action;
        this.timestmp=new Date();
    }


    public Map<String,String> toMap(){
        Map<String,String> data = new HashMap<>();
        data.put("id",this.id.toString());
        data.put("sender",this.sender.getId().toString());
        data.put("reciver",this.reciver.getId().toString());
        data.put("chat",this.chat!=null?this.chat.getChatId():"null");
        data.put("status",this.status.name());
        data.put("action", this.action.name());
        data.put("timestmp",this.timestmp.toString());
        return data;
    }
}
