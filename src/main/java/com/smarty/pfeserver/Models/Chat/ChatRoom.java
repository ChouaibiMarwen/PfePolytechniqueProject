package com.smarty.pfeserver.Models.Chat;

import com.smarty.pfeserver.Models.User.users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ChatRoom")
public class ChatRoom implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "chatId")
    private String chatId;
    @Column(name = "last_message")
    private String lastmessage;

    @OneToOne( fetch = FetchType.EAGER)
    private users sender;

    @OneToOne( fetch = FetchType.EAGER)
    private users recipient;
    @Column(name = "timestmp")
    private Date timestmp = new Date();

}
