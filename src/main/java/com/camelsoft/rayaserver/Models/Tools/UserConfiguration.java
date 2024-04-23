package com.camelsoft.rayaserver.Models.Tools;

import com.camelsoft.rayaserver.Enum.Tools.Language;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "UserConfiguration")
public class UserConfiguration  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "configuration_id")
    private Long id;
    @Column(name = "notification_email")
    private Boolean notificationEmail = false;
    @Column(name = "notification_fcm")
    private Boolean notificationFcm = false;
    @Column(name = "language")
    private Language language = Language.ENGLISH;
    @Column(name = "timestmp")
    private Date timestmp = new Date();

    public UserConfiguration() {

    }
}
