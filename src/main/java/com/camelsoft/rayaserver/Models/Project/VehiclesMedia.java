package com.camelsoft.rayaserver.Models.Project;

import com.camelsoft.rayaserver.Models.File.MediaModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "VehiclesMedia")
public class VehiclesMedia implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE},orphanRemoval = true)
    @JoinColumn(name = "frontviewimage_file_id")
    private MediaModel frontviewimage;
    @OneToOne(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE},orphanRemoval = true)
    @JoinColumn(name = "rearviewimage_file_id")
    private MediaModel rearviewimage;
    @OneToOne(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE},orphanRemoval = true)
    @JoinColumn(name = "interiorviewimage_file_id")
    private MediaModel interiorviewimage;
    @OneToOne(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE},orphanRemoval = true)
    @JoinColumn(name = "sideviewimageleft_file_id")
    private MediaModel sideviewimageleft;
    @OneToOne(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE},orphanRemoval = true)
    @JoinColumn(name = "sideviewimageright_file_id")
    private MediaModel sideviewimageright;
    @OneToMany(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE},orphanRemoval = true)
    @JoinColumn(name = "Vehicles_images_id")
    private Set<MediaModel> additionalviewimages = new HashSet<>();
    @Column(name = "timestamp")
    private Date timestamp;

    public VehiclesMedia() {
        this.timestamp=new Date();
    }

    public VehiclesMedia(MediaModel frontviewimage, MediaModel rearviewimage, MediaModel interiorviewimage, MediaModel sideviewimageleft, MediaModel sideviewimageright, Set<MediaModel> additionalviewimages) {
        this.frontviewimage = frontviewimage;
        this.rearviewimage = rearviewimage;
        this.interiorviewimage = interiorviewimage;
        this.sideviewimageleft = sideviewimageleft;
        this.sideviewimageright = sideviewimageright;
        this.additionalviewimages = additionalviewimages;
        this.timestamp=new Date();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MediaModel getFrontviewimage() {
        return frontviewimage;
    }

    public void setFrontviewimage(MediaModel frontviewimage) {
        this.frontviewimage = frontviewimage;
    }

    public MediaModel getRearviewimage() {
        return rearviewimage;
    }

    public void setRearviewimage(MediaModel rearviewimage) {
        this.rearviewimage = rearviewimage;
    }

    public MediaModel getInteriorviewimage() {
        return interiorviewimage;
    }

    public void setInteriorviewimage(MediaModel interiorviewimage) {
        this.interiorviewimage = interiorviewimage;
    }

    public MediaModel getSideviewimageleft() {
        return sideviewimageleft;
    }

    public void setSideviewimageleft(MediaModel sideviewimageleft) {
        this.sideviewimageleft = sideviewimageleft;
    }

    public MediaModel getSideviewimageright() {
        return sideviewimageright;
    }

    public void setSideviewimageright(MediaModel sideviewimageright) {
        this.sideviewimageright = sideviewimageright;
    }


    public Set<MediaModel> getAdditionalviewimages() {
        return additionalviewimages;
    }

    public void setAdditionalviewimages(Set<MediaModel> additionalviewimages) {
        this.additionalviewimages = additionalviewimages;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
