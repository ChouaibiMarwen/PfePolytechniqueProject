package com.smarty.pfeserver.Models.User;

import com.smarty.pfeserver.Enum.Project.SoftSkillsEnum;
import com.smarty.pfeserver.Enum.Tools.Provider;
import com.smarty.pfeserver.Models.Auth.Privilege;
import com.smarty.pfeserver.Models.Auth.Role;
import com.smarty.pfeserver.Models.File.MediaModel;
import com.smarty.pfeserver.Models.Project.*;
import com.smarty.pfeserver.Models.Tools.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smarty.pfeserver.Models.Project.Mission;
import com.smarty.pfeserver.Models.Tools.Address;
import com.smarty.pfeserver.Models.Tools.BankInformation;
import com.smarty.pfeserver.Models.Tools.PersonalInformation;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.*;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "users_table",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "user_name"),
                @UniqueConstraint(columnNames = "phone_number"),
                @UniqueConstraint(columnNames = "email")
        })
public class users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "user_name")
    @Length(min = 5, message = "*Your user name must have at least 5 characters")
    @NotEmpty(message = "*Please provide a user name")
    private String username;
    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;
    @JsonIgnore
    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;
    @Transient
    private String name;
    @ElementCollection
    private Set<SoftSkillsEnum> softskills = new HashSet<>();
    @Column(name = "phone_number")
    private String phonenumber;
    @Column(name = "suspend_reason")
    private String suspendraison = "NO RAISON FOUND";
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profileimage_media")
    private MediaModel profileimage;
    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "personal_id", referencedColumnName = "personal_information_id")
    private PersonalInformation personalinformation;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "users_table_documents")
    private Set<MediaModel> documents = new HashSet<>();
    @Column(name = "provider")
    private Provider provider = Provider.local;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "users_table_attachmentchat_media")
    private Set<MediaModel> attachmentchat = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.EAGER)
    private Set<Address> addresses = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    @JsonIgnore
    private Set<Mission> missions = new HashSet<>();
    @ManyToMany(mappedBy = "taskparticipants")
    @JsonIgnore
    private Set<Task> tasks = new HashSet<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "manager_id")
    private users manager;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<BankInformation> bankinformations = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_privileges",
            joinColumns =
            @JoinColumn(name = "usar_id", referencedColumnName = "user_id"),
            inverseJoinColumns =
            @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges = new HashSet<>();
    @Column(name = "active")
    private Boolean active = true;
    @Column(name = "verified")
    private Boolean verified = false;
    @Column(name = "deleted")
    private Boolean deleted = false;
    @Column(name = "otp")
    private String lastotp;
    @Column(name = "timestmp")
    private Date timestmp = new Date();

    public users() {

    }

    public users(String username, String email, String password, String phonenumber, PersonalInformation personalinformation) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phonenumber = phonenumber;
        this.personalinformation = personalinformation;
    }

    public users(String username, String email, String password, String phonenumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phonenumber = phonenumber;
        this.timestmp = new Date();
    }


    @PostLoad
    private void afterload() {
        if (this.personalinformation != null) {
            this.name = this.personalinformation.getFirstnameen() + " " + this.personalinformation.getLastnameen();
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @Length(min = 5, message = "*Your user name must have at least 5 characters") @NotEmpty(message = "*Please provide a user name") String getUsername() {
        return username;
    }

    public void setUsername(@Length(min = 5, message = "*Your user name must have at least 5 characters") @NotEmpty(message = "*Please provide a user name") String username) {
        this.username = username;
    }

    public @Email(message = "*Please provide a valid Email") @NotEmpty(message = "*Please provide an email") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "*Please provide a valid Email") @NotEmpty(message = "*Please provide an email") String email) {
        this.email = email;
    }

    public @Length(min = 5, message = "*Your password must have at least 5 characters") @NotEmpty(message = "*Please provide your password") String getPassword() {
        return password;
    }

    public void setPassword(@Length(min = 5, message = "*Your password must have at least 5 characters") @NotEmpty(message = "*Please provide your password") String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSuspendraison() {
        return suspendraison;
    }

    public void setSuspendraison(String suspendraison) {
        this.suspendraison = suspendraison;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public MediaModel getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(MediaModel profileimage) {
        this.profileimage = profileimage;
    }

    public PersonalInformation getPersonalinformation() {
        return personalinformation;
    }

    public void setPersonalinformation(PersonalInformation personalinformation) {
        this.personalinformation = personalinformation;
    }

    public Set<MediaModel> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<MediaModel> documents) {
        this.documents = documents;
    }

    public Set<MediaModel> getAttachmentchat() {
        return attachmentchat;
    }

    public void setAttachmentchat(Set<MediaModel> attachmentchat) {
        this.attachmentchat = attachmentchat;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public users getManager() {
        return manager;
    }

    public void setManager(users manager) {
        this.manager = manager;
    }

    public Set<BankInformation> getBankinformations() {
        return bankinformations;
    }

    public void setBankinformations(Set<BankInformation> bankinformations) {
        this.bankinformations = bankinformations;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getLastotp() {
        return lastotp;
    }

    public void setLastotp(String lastotp) {
        this.lastotp = lastotp;
    }

    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Set<Mission> getMissions() {
        return missions;
    }

    public void setMissions(Set<Mission> missions) {
        this.missions = missions;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public void addMission(Mission mission) {
        this.missions.add(mission);
        mission.getParticipants().add(this);
    }

    public void removeMission(Mission mission) {
        this.missions.remove(mission);
        mission.getParticipants().remove(this);
    }



    public void addTask(Task task) {
        this.tasks.add(task);
        task.getTaskparticipants().add(this);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
        task.getTaskparticipants().remove(this);
    }

    public Set<SoftSkillsEnum> getSoftskills() {
        return softskills;
    }

    public void setSoftskills(Set<SoftSkillsEnum> softskills) {
        this.softskills = softskills;
    }
}
