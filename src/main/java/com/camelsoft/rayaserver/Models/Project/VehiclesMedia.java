package com.camelsoft.rayaserver.Models.Project;

import com.camelsoft.rayaserver.Models.File.File_model;
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
    private File_model frontviewimage;
    @OneToOne(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE},orphanRemoval = true)
    @JoinColumn(name = "rearviewimage_file_id")
    private File_model rearviewimage;
    @OneToOne(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE},orphanRemoval = true)
    @JoinColumn(name = "interiorviewimage_file_id")
    private File_model interiorviewimage;
    @OneToOne(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE},orphanRemoval = true)
    @JoinColumn(name = "sideviewimageleft_file_id")
    private File_model sideviewimageleft;
    @OneToOne(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE},orphanRemoval = true)
    @JoinColumn(name = "sideviewimageright_file_id")
    private File_model sideviewimageright;
    @OneToMany(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE},orphanRemoval = true)
    @JoinColumn(name = "Vehicles_images_id")
    private Set<File_model> additionalviewimages = new HashSet<>();
    @Column(name = "timestamp")
    private Date timestamp;

    public VehiclesMedia() {
        this.timestamp=new Date();
    }

    public VehiclesMedia(File_model frontviewimage, File_model rearviewimage, File_model interiorviewimage, File_model sideviewimageleft, File_model sideviewimageright, Set<File_model> additionalviewimages) {
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

    public File_model getFrontviewimage() {
        return frontviewimage;
    }

    public void setFrontviewimage(File_model frontviewimage) {
        this.frontviewimage = frontviewimage;
    }

    public File_model getRearviewimage() {
        return rearviewimage;
    }

    public void setRearviewimage(File_model rearviewimage) {
        this.rearviewimage = rearviewimage;
    }

    public File_model getInteriorviewimage() {
        return interiorviewimage;
    }

    public void setInteriorviewimage(File_model interiorviewimage) {
        this.interiorviewimage = interiorviewimage;
    }

    public File_model getSideviewimageleft() {
        return sideviewimageleft;
    }

    public void setSideviewimageleft(File_model sideviewimageleft) {
        this.sideviewimageleft = sideviewimageleft;
    }

    public File_model getSideviewimageright() {
        return sideviewimageright;
    }

    public void setSideviewimageright(File_model sideviewimageright) {
        this.sideviewimageright = sideviewimageright;
    }


    public Set<File_model> getAdditionalviewimages() {
        return additionalviewimages;
    }

    public void setAdditionalviewimages(Set<File_model> additionalviewimages) {
        this.additionalviewimages = additionalviewimages;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
