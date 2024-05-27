package com.camelsoft.rayaserver.Models.Project;

import com.camelsoft.rayaserver.Models.File.File_model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Ads")
public class Ads  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(columnDefinition = "TEXT", name = "url")
    private String url;
    @Column(columnDefinition = "TEXT", name = "description")
    private String description;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "list_ads_id")
    private Set<File_model> attachments = new HashSet<>();
    @Column(name = "timestmp")
    private Date timestmp = new Date();

    public Ads() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<File_model> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<File_model> attachments) {
        this.attachments = attachments;
    }

    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }
}
