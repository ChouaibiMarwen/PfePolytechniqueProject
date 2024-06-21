package com.camelsoft.rayaserver.Models.User;

import com.camelsoft.rayaserver.Enum.Tools.Provider;
import com.camelsoft.rayaserver.Models.Auth.Privilege;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.File.MediaModel;
import com.camelsoft.rayaserver.Models.Project.*;
import com.camelsoft.rayaserver.Models.Tools.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @Column(name = "phone_number")
    private String phonenumber;
    @Column(name = "provider")
    private Provider provider = Provider.local;
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
    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "configuration_user_id", referencedColumnName = "configuration_id")
    private UserConfiguration userconfiguration;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "users_table_documents")
    private Set<MediaModel> documents = new HashSet<>();
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "users_table_attachmentchat_media")
    private Set<MediaModel> attachmentchat = new HashSet<>();
    @OneToMany(mappedBy = "createdby", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Invoice> invoicescreated = new HashSet<>();
    @OneToMany(mappedBy = "confirmedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Invoice> invoicesconfirmed = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.LAZY)
    private Set<Address> addresses = new HashSet<>();
    @OneToMany(mappedBy = "relatedto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Invoice> invoicesrecived = new HashSet<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<RequestCorrespondence> requestcorrespendencessended = new HashSet<>();
    @OneToMany(mappedBy = "creatorrequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Request> requests = new HashSet<>();
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "manager_id")
    private users manager;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BankInformation> bankinformations = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id")
    private BillingAddress billingAddress;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_privileges",
            joinColumns =
            @JoinColumn(name = "usar_id", referencedColumnName = "user_id"),
            inverseJoinColumns =
            @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Supplier supplier;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<PurshaseOrder> purchaseOrders = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "role_department_id")
    private RoleDepartment roledepartment;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserAction> actions = new HashSet<>();

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

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_categories",
            joinColumns =
            @JoinColumn(name = "usar_id", referencedColumnName = "user_id"),
            inverseJoinColumns =
            @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private Set<UsersCategory> categories = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "usersevents", fetch = FetchType.EAGER)
    private Set<Event> events = new HashSet<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "classification_id")
    private SuppliersClassification supplierclassification;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "subadmin_classification_id")
    private SuppliersClassification subadminClassification;

    @JsonIgnore
    @OneToMany(mappedBy = "subadminassignedto", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<PurshaseOrder> poassigned = new HashSet<>();

    @Transient
    private String supplierCompanyName;

    @Transient
    private Long idsubadminclassification;

    @Transient
    private String namesubadminclassification;


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
        if(this.supplier != null) {
            this.supplier.setName(this.name);
            if(this.supplier.getName() != null) {
                this.supplierCompanyName = this.supplier.getName();
            }
            if(this.supplierclassification != null) {
                this.supplier.setClassificationname(this.supplierclassification.getName());
            }
        }else{
            this.supplierCompanyName = "";
        }

        if(subadminClassification != null){
            idsubadminclassification = subadminClassification.getId();
            namesubadminclassification = subadminClassification.getName();
        }
    }

    public void addAddress(Address address) {
        this.addresses.add(address);
    }

    public Set<Invoice> getInvoicesconfirmed() {
        return invoicesconfirmed;
    }

    public void setInvoicesconfirmed(Set<Invoice> invoicesconfirmed) {
        this.invoicesconfirmed = invoicesconfirmed;
    }

    public String getSupplierCompanyName() {
        return supplierCompanyName;
    }

    public void setSupplierCompanyName(String supplierCompanyName) {
        this.supplierCompanyName = supplierCompanyName;
    }

    public UserConfiguration getUserconfiguration() {
        return userconfiguration;
    }

    public void setUserconfiguration(UserConfiguration userconfiguration) {
        this.userconfiguration = userconfiguration;
    }

    @Override
    public int hashCode() {
        return 31; // Replace with any prime number
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
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

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
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

    public Set<UserAction> getActions() {
        return actions;
    }

    public void setActions(Set<UserAction> actions) {
        this.actions = actions;
    }

    public Set<MediaModel> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<MediaModel> documents) {
        this.documents = documents;
    }

    public Set<Invoice> getInvoicescreated() {
        return invoicescreated;
    }

    public void setInvoicescreated(Set<Invoice> invoicescreated) {
        this.invoicescreated = invoicescreated;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<Invoice> getInvoicesrecived() {
        return invoicesrecived;
    }

    public void setInvoicesrecived(Set<Invoice> invoicesrecived) {
        this.invoicesrecived = invoicesrecived;
    }

    public Set<RequestCorrespondence> getRequestcorrespendencessended() {
        return requestcorrespendencessended;
    }

    public void setRequestcorrespendencessended(Set<RequestCorrespondence> requestcorrespendencessended) {
        this.requestcorrespendencessended = requestcorrespendencessended;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public RoleDepartment getRoledepartment() {
        return roledepartment;
    }

    public void setRoledepartment(RoleDepartment roledepartment) {
        this.roledepartment = roledepartment;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    public Set<BankInformation> getBankinformations() {
        return bankinformations;
    }

    public void setBankinformations(Set<BankInformation> bankinformations) {
        this.bankinformations = bankinformations;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Set<PurshaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurshaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
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

    public Set<MediaModel> getAttachmentchat() {
        return attachmentchat;
    }

    public void setAttachmentchat(Set<MediaModel> attachmentchat) {
        this.attachmentchat = attachmentchat;
    }

    public users getManager() {
        return manager;
    }

    public void setManager(users manager) {
        this.manager = manager;
    }

    public Set<UsersCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<UsersCategory> categories) {
        this.categories = categories;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public SuppliersClassification getSupplierclassification() {
        return supplierclassification;
    }

    public void setSupplierclassification(SuppliersClassification supplierclassification) {
        this.supplierclassification = supplierclassification;
    }

    public Set<PurshaseOrder> getPoassigned() {
        return poassigned;
    }

    public void setPoassigned(Set<PurshaseOrder> poassigned) {
        this.poassigned = poassigned;
    }

    public void addUsersPrivilege(Privilege privilege) {
        privileges.add(privilege);
        privilege.getUser().add(this);
    }

    public SuppliersClassification getSubadminClassification() {
        return subadminClassification;
    }

    public void setSubadminClassification(SuppliersClassification subadminClassification) {
        this.subadminClassification = subadminClassification;
    }

    public Long getIdsubadminclassification() {
        return idsubadminclassification;
    }

    public void setIdsubadminclassification(Long idsubadminclassification) {
        this.idsubadminclassification = idsubadminclassification;
    }

    public String getNamesubadminclassification() {
        return namesubadminclassification;
    }

    public void setNamesubadminclassification(String namesubadminclassification) {
        this.namesubadminclassification = namesubadminclassification;
    }
}
