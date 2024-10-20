package com.smarty.pfeserver.Models.Notification;




import com.smarty.pfeserver.Enum.Project.Notification.MessageStatus;
import com.smarty.pfeserver.Enum.Tools.Action;
import com.smarty.pfeserver.Models.Chat.ChatMessage;
import com.smarty.pfeserver.Models.User.users;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "notification_model")
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

    @Column(name = "action_id")
    private Long actionId;
    @Column(name = "subject")
    private String subject ;
    @Column(name = "content")
    private String content ;

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


    public Notification(users sender, users reciver, Action action, String subject, String content,Long actionId) {
        this.actionId = actionId;
        this.sender = sender;
        this.reciver = reciver;
        this.action = action;
        this.subject = subject;
        this.content = content;
        this.timestmp = new Date();
    }



    public Map<String,String> toMap(){
        Map<String,String> data = new HashMap<>();
        data.put("id",this.id.toString());
        data.put("sender",this.sender.getId().toString());
        data.put("reciver",this.reciver.getId().toString());
        data.put("chat",this.chat!=null?this.chat.getChatId():"null");
        data.put("status",this.status.name());
        data.put("action", this.action.name());
        data.put("actionId", this.actionId.toString());
        data.put("subject",this.subject);
        data.put("content",this.content);
        data.put("timestmp",this.timestmp.toString());
        return data;
    }


    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public ChatMessage getChat() {
        return chat;
    }

    public void setChat(ChatMessage chat) {
        this.chat = chat;
    }

    public users getReciver() {
        return reciver;
    }

    public void setReciver(users reciver) {
        this.reciver = reciver;
    }

    public users getSender() {
        return sender;
    }

    public void setSender(users sender) {
        this.sender = sender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
