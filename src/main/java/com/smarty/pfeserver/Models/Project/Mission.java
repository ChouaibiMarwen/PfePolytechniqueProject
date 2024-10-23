package com.smarty.pfeserver.Models.Project;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smarty.pfeserver.Enum.Project.MissionStatusEnum;
import com.smarty.pfeserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Mission")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "startdate")
    private Date startdate;
    @Column(name = "title")
    private String title;
    @Column(name = "address")
    private String address;
    @Column(name = "fileurl")
    private String fileurl;
    @Column(name = "enddate")
    private Date enddate;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "teamLead")
    private users teamLead;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "location")
    private Location location;
    @Column(name = "budget")
    private Double budget = 0.0;
    @Column(name = "status")
    private MissionStatusEnum status = MissionStatusEnum.PENDING;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "users_missions",
            joinColumns =
            @JoinColumn(name = "group_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "users_id", referencedColumnName = "user_id"))
    private Set<users> participants = new HashSet<>();
    @OneToMany(mappedBy = "mission", cascade = {CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();
    @OneToMany(mappedBy = "mission", cascade = {CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.EAGER)
    private Set<Task> tasks = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "mission", cascade = {CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.EAGER)
    private Set<BoostBudgetRequest> requestBoostBudgetRequest = new HashSet<>();
    @Column(name = "archive")
    private Boolean archive = false;
    @Column(name = "timestmp")
    private Date timestmp = new Date();

    public Mission() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public users getTeamLead() {
        return teamLead;
    }

    public void setTeamLead(users teamLead) {
        this.teamLead = teamLead;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Set<users> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<users> participants) {
        this.participants = participants;
    }

    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public MissionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MissionStatusEnum status) {
        this.status = status;
    }

    public void addParticipant(users user) {
        this.participants.add(user);
        user.getMissions().add(this);
    }

    public void removeParticipant(users user) {
        this.participants.remove(user);
        user.getMissions().remove(this);
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Set<BoostBudgetRequest> getRequestBoostBudgetRequest() {
        return requestBoostBudgetRequest;
    }

    public void setRequestBoostBudgetRequest(Set<BoostBudgetRequest> requestBoostBudgetRequest) {
        this.requestBoostBudgetRequest = requestBoostBudgetRequest;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }
}
