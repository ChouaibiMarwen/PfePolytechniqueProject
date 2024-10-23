package com.smarty.pfeserver.Models.Project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smarty.pfeserver.Enum.Project.MissionStatusEnum;
import com.smarty.pfeserver.Models.User.users;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Mission")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "createdby")
    private users createdby;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "task_mission")
    private Mission mission;
    @Column(name = "startdate")
    private Date startdate;
    @Column(name = "title")
    private String title;
    @Column(name = "status")
    private MissionStatusEnum status = MissionStatusEnum.PENDING;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "evolution")
    private Double evolutionPercentage = 0.0;
    @Column(name = "enddate")
    private Date enddate;
    @Column(name = "fileurl")
    private String fileurl;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "users_mtasks",
            joinColumns =
            @JoinColumn(name = "group_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "users_id", referencedColumnName = "user_id"))
    private Set<users> taskparticipants = new HashSet<>();
    @Column(name = "timestmp")
    private Date timestmp = new Date();

    public Task() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public users getCreatedby() {
        return createdby;
    }

    public void setCreatedby(users createdby) {
        this.createdby = createdby;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }

    public MissionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MissionStatusEnum status) {
        this.status = status;
    }

    public Double getEvolutionPercentage() {
        return evolutionPercentage;
    }

    public void setEvolutionPercentage(Double evolution) {
        this.evolutionPercentage = evolution;
    }

    public Set<users> getTaskparticipants() {
        return taskparticipants;
    }

    public void setTaskparticipants(Set<users> taskparticipants) {
        this.taskparticipants = taskparticipants;
    }

    public void addParticipants(users user) {
        this.taskparticipants.add(user);
        user.getTasks().add(this);
    }

    public void removeParticipants(users user)  {
        this.taskparticipants.remove(user);
        user.getTasks().remove(this);
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }
}
