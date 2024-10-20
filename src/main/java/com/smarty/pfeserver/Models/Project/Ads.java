package com.smarty.pfeserver.Models.Project;

import com.smarty.pfeserver.Models.File.MediaModel;
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
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "list_ads_media")
    private Set<MediaModel> attachments = new HashSet<>();
    @Column(name = "add_to_slider")
    private Boolean addtoslider = true;
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

    public Set<MediaModel> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<MediaModel> attachments) {
        this.attachments = attachments;
    }

    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }

    public Boolean getAddtoslider() {
        return addtoslider;
    }

    public void setAddtoslider(Boolean addtoslider) {
        this.addtoslider = addtoslider;
    }
}
