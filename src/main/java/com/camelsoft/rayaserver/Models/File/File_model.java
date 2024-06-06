package com.camelsoft.rayaserver.Models.File;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "file_project")
public class File_model implements Serializable {

    @Id
    @GeneratedValue(strategy =   GenerationType.SEQUENCE)
    @Column(name = "file_id"  )
    private Long id;
    @Column(name = "file_name")
    private String name;
    @Column(name = "file_path")
    private String url;
    @Column(name = "file_type")
    private String type;
   @Column(name = "file_description")
    private String description;
    @Column(name = "file_size")
    private Long size;
    @Column(name = "timestmp")
    private Date timestmp=new Date();
    @Column(name = "range")
    private Integer range;

    @Column(name = "share")
    private Boolean share;


    public File_model() {
    }

    public File_model(String name, String url, String type, long size) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.size = size;
        this.timestmp= new Date();
    }
    @PreRemove
    public void preRemove() {


    }


    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getShare() {
        return share;
    }

    public void setShare(Boolean share) {
        this.share = share;
    }

    public Date getTimestmp() {
        return timestmp;
    }


    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }
}
