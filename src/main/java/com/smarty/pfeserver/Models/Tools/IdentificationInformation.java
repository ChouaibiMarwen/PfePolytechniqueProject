package com.smarty.pfeserver.Models.Tools;

import com.smarty.pfeserver.Enum.User.IndetificationType;
import com.smarty.pfeserver.Models.country.Root;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "IdentificationInformation")
public class IdentificationInformation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "type")
    private IndetificationType type = IndetificationType.NONE;
    @Column(name = "number")
    private String number;
    @Column(name = "issuedate")
    private Date issuedate=new Date();
    @Column(name = "expirydate")
    private Date expirydate=new Date();
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Root issuingcountry;
    @Column(name = "timestmp")
    private Date timestmp = new Date();

    public IdentificationInformation() {
    }
}
