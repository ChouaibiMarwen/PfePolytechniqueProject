package com.camelsoft.rayaserver.Services.criteria;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceRelated;
import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Enum.Project.Vehicles.AvailiabilityEnum;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.Project.PurshaseOrder;
import com.camelsoft.rayaserver.Models.Project.Vehicles;
import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.SuppliersClassification;
import com.camelsoft.rayaserver.Models.User.UsersCategory;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Auth.RoleRepository;
import com.camelsoft.rayaserver.Repository.Project.InvoiceRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import com.camelsoft.rayaserver.Tools.Util.FirstTimeInitializer;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository
public class CriteriaService {
    private final EntityManager em;
    private final CriteriaBuilder criteriaBuilder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private InvoiceRepository invoicerepository;
    private final Log logger = LogFactory.getLog(CriteriaService.class);

    public CriteriaService(EntityManager em) {

        this.em = em;
        this.criteriaBuilder = em.getCriteriaBuilder();
    }


    public PageImpl<users> UsersSearchCreatiria(int page, int size, Boolean active, Boolean deleted, String search, String Role) {
        try {
            Role userRole = roleRepository.findByRole(RoleEnum.valueOf(Role));

            CriteriaQuery<users> Q = criteriaBuilder.createQuery(users.class);
            Root<users> user = Q.from(users.class);
            Q.distinct(true);


            Predicate finalPredicate = criteriaBuilder.equal(user.get("role"), userRole);

            if (search != null) {
                Predicate namePredicate = criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(user.get("name")), "%" + search.toLowerCase() + "%"));
                namePredicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(user.get("email")), "%" + search.toLowerCase() + "%"), namePredicate);
                namePredicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(user.get("phonenumber")), "%" + search.toLowerCase() + "%"), namePredicate);
                namePredicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(user.get("timestmp").as(String.class)), "%" + search.toLowerCase() + "%"), namePredicate);
                finalPredicate = criteriaBuilder.and(namePredicate, finalPredicate);
            }
            if (active != null)
                finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(user.get("active"), active));
            if (deleted != null) {
                finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.or(
                        criteriaBuilder.equal(user.get("deleted"), deleted),
                        criteriaBuilder.isNull(user.get("deleted"))
                ));

            }
            Q.where(finalPredicate);
            Q.orderBy(criteriaBuilder.desc(user.get("timestmp")));
            TypedQuery<users> typedQuery = em.createQuery(Q);

            int usersCount = typedQuery.getResultList().size();
            typedQuery.setFirstResult(page * size);
            typedQuery.setMaxResults(size);
            Pageable pageable = PageRequest.of(page, size);

            return new PageImpl<>(typedQuery.getResultList(), pageable, usersCount);

        } catch (NoResultException ex) {
            throw new NotFoundException("No data found.");
        }
    }

    public PageImpl<users> UsersSearchCreatiriaRolesList(int page, int size, Boolean active, Boolean deleted, String search, List<String> roles) {
        try {
            List<Role> userRoles = roleRepository.findByRoleIn(roles.stream().map(RoleEnum::valueOf).collect(Collectors.toList()));

            CriteriaQuery<users> Q = criteriaBuilder.createQuery(users.class);
            Root<users> user = Q.from(users.class);
            Q.distinct(true);

            Predicate finalPredicate = user.get("role").in(userRoles);

            if (search != null) {
                Predicate namePredicate = criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(user.get("name")), "%" + search.toLowerCase() + "%"));
                namePredicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(user.get("email")), "%" + search.toLowerCase() + "%"), namePredicate);
                namePredicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(user.get("phonenumber")), "%" + search.toLowerCase() + "%"), namePredicate);
                namePredicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(user.get("timestmp").as(String.class)), "%" + search.toLowerCase() + "%"), namePredicate);
                finalPredicate = criteriaBuilder.and(namePredicate, finalPredicate);
            }
            if (active != null)
                finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(user.get("active"), active));
            if (deleted != null) {
                finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.or(
                        criteriaBuilder.equal(user.get("deleted"), deleted),
                        criteriaBuilder.isNull(user.get("deleted"))
                ));

            }
            Q.where(finalPredicate);
            Q.orderBy(criteriaBuilder.desc(user.get("timestmp")));
            TypedQuery<users> typedQuery = em.createQuery(Q);

            int usersCount = typedQuery.getResultList().size();
            typedQuery.setFirstResult(page * size);
            typedQuery.setMaxResults(size);
            Pageable pageable = PageRequest.of(page, size);

            return new PageImpl<>(typedQuery.getResultList(), pageable, usersCount);

        } catch (NoResultException ex) {
            throw new NotFoundException("No data found.");
        }
    }

    public PageImpl<users> UsersSearchCreatiriaRolesListsupplier(int page, int size, Boolean active, Boolean deleted, String search, List<String> roles, users manager) {
        try {
            List<Role> userRoles = roleRepository.findByRoleIn(roles.stream().map(RoleEnum::valueOf).collect(Collectors.toList()));

            CriteriaQuery<users> Q = criteriaBuilder.createQuery(users.class);
            Root<users> user = Q.from(users.class);
            Q.distinct(true);

            Predicate finalPredicate = user.get("role").in(userRoles);

            if (search != null) {
                Predicate namePredicate = criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(user.get("name")), "%" + search.toLowerCase() + "%"));
                namePredicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(user.get("email")), "%" + search.toLowerCase() + "%"), namePredicate);
                namePredicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(user.get("phonenumber")), "%" + search.toLowerCase() + "%"), namePredicate);
                namePredicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(user.get("timestmp").as(String.class)), "%" + search.toLowerCase() + "%"), namePredicate);
                finalPredicate = criteriaBuilder.and(namePredicate, finalPredicate);
            }
            if (active != null)
                finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(user.get("active"), active));
            finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(user.get("manager"), manager));
            if (deleted != null) {
                finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.or(
                        criteriaBuilder.equal(user.get("deleted"), deleted),
                        criteriaBuilder.isNull(user.get("deleted"))
                ));

            }
            Q.where(finalPredicate);
            Q.orderBy(criteriaBuilder.desc(user.get("timestmp")));
            TypedQuery<users> typedQuery = em.createQuery(Q);

            int usersCount = typedQuery.getResultList().size();
            typedQuery.setFirstResult(page * size);
            typedQuery.setMaxResults(size);
            Pageable pageable = PageRequest.of(page, size);

            return new PageImpl<>(typedQuery.getResultList(), pageable, usersCount);

        } catch (NoResultException ex) {
            throw new NotFoundException("No data found.");
        }
    }

   /* public PageImpl<users> UsersSearchCreatiriaRolesListSubsupplier(int page, int size, Boolean active, String name, RoleEnum role, Boolean verified, users manager, Boolean deleted) {
        try {
            Role userRoles = roleRepository.findByRole(role);

            CriteriaQuery<users> Q = criteriaBuilder.createQuery(users.class);
            Root<users> user = Q.from(users.class);
            Q.distinct(true);

            Predicate finalPredicate = user.get("role").in(userRoles);

            if (name != null) {
                Predicate namePredicate = criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(user.get("name")), "%" + name.toLowerCase() + "%"));
                finalPredicate = criteriaBuilder.and(namePredicate, finalPredicate);
            }
            if (active != null)
                finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(user.get("active"), active));

            if (verified != null)
                finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(user.get("verified"), verified));

            finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(user.get("manager"), manager));
            if (deleted != null) {
                finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.or(
                        criteriaBuilder.equal(user.get("deleted"), deleted),
                        criteriaBuilder.isNull(user.get("deleted"))
                ));

            }
            Q.where(finalPredicate);
            Q.orderBy(criteriaBuilder.desc(user.get("timestmp")));
            TypedQuery<users> typedQuery = em.createQuery(Q);

            int usersCount = typedQuery.getResultList().size();
            typedQuery.setFirstResult(page * size);
            typedQuery.setMaxResults(size);
            Pageable pageable = PageRequest.of(page, size);

            return new PageImpl<>(typedQuery.getResultList(), pageable, usersCount);

        } catch (NoResultException ex) {
            throw new NotFoundException("No data found.");
        }
    }*/

    public DynamicResponse filterAllUser(int page, int size, Boolean active, String name, RoleEnum role, Boolean verified) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Role userRole = roleRepository.findByRole(role);

            // Initialize criteria builder and query
            CriteriaQuery<users> query = criteriaBuilder.createQuery(users.class);
            Root<users> user = query.from(users.class);
            Join<users, PersonalInformation> personalInformationJoin = user.join("personalinformation"); // Assuming you have a PersonalInformation join

            // Create a list of predicates
            List<Predicate> predicates = new ArrayList<>();

            // Filter by role
            predicates.add(criteriaBuilder.equal(user.get("role"), userRole));

            // Filter by deleted (always false)
            predicates.add(criteriaBuilder.isFalse(user.get("deleted")));

            // Optional filters
            if (active != null) {
                predicates.add(criteriaBuilder.equal(user.get("active"), active));
            }

            if (verified != null) {
                predicates.add(criteriaBuilder.equal(user.get("verified"), verified));
            }

            // Search by name (partial match on both first name and last name)
            if (name != null && !name.isEmpty()) {
                String searchString = "%" + name.toLowerCase() + "%";

                // Case-insensitive match on first name, last name, and concatenated names
                Predicate namePredicate = criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(personalInformationJoin.get("firstnameen")), searchString),
                        criteriaBuilder.like(criteriaBuilder.lower(personalInformationJoin.get("lastnameen")), searchString),
                        criteriaBuilder.like(criteriaBuilder.lower(
                                criteriaBuilder.concat(
                                        criteriaBuilder.concat(personalInformationJoin.get("firstnameen"), " "),
                                        personalInformationJoin.get("lastnameen")
                                )
                        ), searchString)
                );

                // Combine the name predicate with the rest of the predicates
                predicates.add(namePredicate);
            }

            // Add predicates to the query
            query.where(predicates.toArray(new Predicate[0]));

            // Order by timestamp descending
            query.orderBy(criteriaBuilder.desc(user.get("timestmp")));

            // Execute the query
            TypedQuery<users> typedQuery = em.createQuery(query);
            typedQuery.setFirstResult(page * size);
            typedQuery.setMaxResults(size);

            // Fetch the result
            List<users> userList = typedQuery.getResultList();

            // Count total elements
            CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
            Root<users> countRoot = countQuery.from(users.class);
            countQuery.select(criteriaBuilder.count(countRoot)).where(predicates.toArray(new Predicate[0]));
            Long totalElements = em.createQuery(countQuery).getSingleResult();

            // Calculate total pages
            int totalPages = (int) Math.ceil((double) totalElements / size);

            return new DynamicResponse(userList, page, totalElements, totalPages);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException("No data found");
        }
    }



    public PageImpl<users> UsersSearchCreatiriaRolesListSubsupplier(int page, int size, Boolean active, String name, RoleEnum role, Boolean verified, users manager, Boolean deleted) {
        try {
            Role userRoles = roleRepository.findByRole(role);

            CriteriaQuery<users> Q = criteriaBuilder.createQuery(users.class);
            Root<users> user = Q.from(users.class);
            Q.distinct(true);

            // Join with PersonalInformation to access name fields
            Join<users, PersonalInformation> personalInformationJoin = user.join("personalinformation", JoinType.LEFT);

            Predicate finalPredicate = user.get("role").in(userRoles);

            // Search by name
            if (name != null && !name.isEmpty()) {
                Predicate namePredicate = criteriaBuilder.like(
                        criteriaBuilder.concat(
                                criteriaBuilder.concat(
                                        personalInformationJoin.get("firstnameen"), " "
                                ),
                                personalInformationJoin.get("lastnameen")
                        ),
                        "%" + name.toLowerCase() + "%"
                );
                finalPredicate = criteriaBuilder.and(namePredicate, finalPredicate);
            }

            if (active != null) {
                finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(user.get("active"), active));
            }

            if (verified != null) {
                finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(user.get("verified"), verified));
            }

            if (manager != null) {
                finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(user.get("manager"), manager));
            }

            if (deleted != null) {
                finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.or(
                        criteriaBuilder.equal(user.get("deleted"), deleted),
                        criteriaBuilder.isNull(user.get("deleted"))
                ));
            }

            Q.where(finalPredicate);
            Q.orderBy(criteriaBuilder.desc(user.get("timestmp")));
            TypedQuery<users> typedQuery = em.createQuery(Q);

            int usersCount = typedQuery.getResultList().size();
            typedQuery.setFirstResult(page * size);
            typedQuery.setMaxResults(size);
            Pageable pageable = PageRequest.of(page, size);

            return new PageImpl<>(typedQuery.getResultList(), pageable, usersCount);
        } catch (NoResultException ex) {
            throw new NotFoundException("No data found.");
        }
    }


    public PageImpl<Invoice> findAllByStatusAndRelatedAndUsers(int page, int size, InvoiceStatus status, InvoiceRelated related, users user, Boolean thirdparty, Integer invoicenumber) {
        try {
            // Prepare criteria builder and query
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Invoice> criteriaQuery = criteriaBuilder.createQuery(Invoice.class);
            Root<Invoice> invoiceRoot = criteriaQuery.from(Invoice.class);

            // Prepare list to hold predicates
            List<Predicate> predicates = new ArrayList<>();

            // Combine createdby and relatedto invoices
            List<Invoice> invoicesList = new ArrayList<>();
            invoicesList.addAll(invoicerepository.findAllByCreatedby(user));
            invoicesList.addAll(invoicerepository.findAllByRelatedto(user));
            predicates.add(invoiceRoot.in(invoicesList));

            // Apply status filter if present
            if (status != null) {
                predicates.add(criteriaBuilder.equal(invoiceRoot.get("status"), status));
            }
            if (thirdparty != null) {
                if (thirdparty) {
                    predicates.add(criteriaBuilder.isNotNull(invoiceRoot.get("thirdpartypoid")));
                } else {
                    predicates.add(criteriaBuilder.isNull(invoiceRoot.get("thirdpartypoid")));
                }
            }

            // Apply related filter if present
            if (related != null) {
                predicates.add(criteriaBuilder.equal(invoiceRoot.get("related"), related));
            }

            // Apply invoicenumber filter if present
            if (invoicenumber != null) {
                predicates.add(criteriaBuilder.equal(invoiceRoot.get("invoicenumber"), invoicenumber));
            }
            // Apply user filter
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(invoiceRoot.get("createdby"), user),
                    criteriaBuilder.equal(invoiceRoot.get("relatedto"), user)
            ));

            // Apply predicates to query
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

            // Order by timestamp descending
            criteriaQuery.orderBy(criteriaBuilder.desc(invoiceRoot.get("timestamp")));

            // Create query and set pagination
            TypedQuery<Invoice> typedQuery = em.createQuery(criteriaQuery);
            int totalRecords = typedQuery.getResultList().size();
            typedQuery.setFirstResult(page * size);
            typedQuery.setMaxResults(size);

            // Create pageable instance
            Pageable pageable = PageRequest.of(page, size);

            // Return paginated result
            return new PageImpl<>(typedQuery.getResultList(), pageable, totalRecords);
        } catch (NoResultException ex) {
            throw new NotFoundException("No data found.");
        }
    }



  /*  public PageImpl<Invoice> findAllByStatusAndRole(int page, int size, InvoiceStatus status, List<RoleEnum> role, Integer invoicenumber, Long poid, String suppliername, users assignedto) {
        try {
            logger.error("cri");
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Invoice> criteriaQuery = criteriaBuilder.createQuery(Invoice.class);
            Root<Invoice> invoiceRoot = criteriaQuery.from(Invoice.class);

            List<Predicate> predicates = new ArrayList<>();
            logger.error("searching by role" );
            List<Invoice> invoicesList = invoicerepository.findByRoleIn(role);
            logger.error("invcoice size: " + invoicesList.size() );
            predicates.add(invoiceRoot.in(invoicesList));

            if (status != null) {
                predicates.add(criteriaBuilder.equal(invoiceRoot.get("status"), status));
            }

            if (invoicenumber != null) {
                predicates.add(criteriaBuilder.equal(invoiceRoot.get("invoicenumber"), invoicenumber));
            }

            if (suppliername != null && !suppliername.isEmpty()) {
                predicates.add(criteriaBuilder.like(invoiceRoot.get("suppliername"), "%" + suppliername + "%"));
            }

            if (poid != null) {
                Join<Invoice, PurshaseOrder> purchaseOrderJoin = invoiceRoot.join("purshaseorder", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(purchaseOrderJoin.get("id"), poid));
                logger.error("join purshaseorder done");
            }

            if (assignedto != null) {
                Join<Invoice, PurshaseOrder> purchaseOrderJoin = invoiceRoot.join("purshaseorder", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(purchaseOrderJoin.get("subadminassignedto"), assignedto));
                logger.error("join subadminassignedto done");
            }

            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            criteriaQuery.orderBy(criteriaBuilder.desc(invoiceRoot.get("timestamp")));

            TypedQuery<Invoice> typedQuery = em.createQuery(criteriaQuery);

            // Count total records efficiently
            CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
            countQuery.select(criteriaBuilder.count(countQuery.from(Invoice.class)));
            countQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            int totalRecords = em.createQuery(countQuery).getSingleResult().intValue();

            typedQuery.setFirstResult(page * size);
            typedQuery.setMaxResults(size);

            Pageable pageable = PageRequest.of(page, size);
            List<Invoice> resultList = typedQuery.getResultList();
            logger.error("invcoice resultList size: " + resultList.size() );

            return new PageImpl<>(resultList, pageable, totalRecords);
        } catch (NoResultException ex) {
            throw new NotFoundException("No data found.");
        }
    }
*/

    public PageImpl<Invoice> findAllByStatusAndRole(int page, int size, InvoiceStatus status, List<RoleEnum> role, Integer invoicenumber, Long poid, String suppliername, Long suppliernumber, users assignedto) {
        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Invoice> criteriaQuery = criteriaBuilder.createQuery(Invoice.class);
            Root<Invoice> invoiceRoot = criteriaQuery.from(Invoice.class);
            List<Predicate> predicates = new ArrayList<>();

            // Join with PurshaseOrder to access its fields
            Join<Invoice, PurshaseOrder> purchaseOrderJoin = invoiceRoot.join("purshaseorder", JoinType.LEFT);
            // Join with Supplier to access suppliernumber
            Join<PurshaseOrder, Supplier> supplierJoin = purchaseOrderJoin.join("supplier", JoinType.LEFT);

            // Apply filters
            if (role != null && !role.isEmpty()) {
                predicates.add(invoiceRoot.get("role").in(role));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(invoiceRoot.get("status"), status));
            }

            if (invoicenumber != null) {
                predicates.add(criteriaBuilder.equal(invoiceRoot.get("invoicenumber"), invoicenumber));
            }

            if (suppliername != null && !suppliername.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(invoiceRoot.get("suppliername")),
                        "%" + suppliername.toLowerCase() + "%"
                ));
            }

            if (poid != null) {
                predicates.add(criteriaBuilder.equal(purchaseOrderJoin.get("id"), poid));
            }

            if (assignedto != null) {
                predicates.add(criteriaBuilder.equal(purchaseOrderJoin.get("subadminassignedto"), assignedto));
            }

            // Filter by suppliernumber from the Supplier entity
            if (suppliernumber != null) {
                predicates.add(criteriaBuilder.equal(supplierJoin.get("suppliernumber"), suppliernumber));
            }

            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            criteriaQuery.orderBy(criteriaBuilder.desc(invoiceRoot.get("timestamp")));

            TypedQuery<Invoice> typedQuery = em.createQuery(criteriaQuery);

            // Count total records efficiently
            CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
            Root<Invoice> countRoot = countQuery.from(Invoice.class);
            countQuery.select(criteriaBuilder.count(countRoot));

            // Reapply the same joins and predicates for the count query
            Join<Invoice, PurshaseOrder> countPurchaseOrderJoin = countRoot.join("purshaseorder", JoinType.LEFT);
            Join<PurshaseOrder, Supplier> countSupplierJoin = countPurchaseOrderJoin.join("supplier", JoinType.LEFT);
            List<Predicate> countPredicates = new ArrayList<>(predicates);
            countQuery.where(criteriaBuilder.and(countPredicates.toArray(new Predicate[0])));
            Long totalRecords = em.createQuery(countQuery).getSingleResult();

            typedQuery.setFirstResult(page * size);
            typedQuery.setMaxResults(size);

            Pageable pageable = PageRequest.of(page, size);
            List<Invoice> resultList = typedQuery.getResultList();

            return new PageImpl<>(resultList, pageable, totalRecords);
        } catch (NoResultException ex) {
            throw new NotFoundException("No data found.");
        }
    }

    public List<users> UsersSearchCreatiriaRolesListNotPaginated(Boolean active, Boolean deleted, String search, List<String> roles) {
        try {
            List<Role> userRoles = roleRepository.findByRoleIn(roles.stream().map(RoleEnum::valueOf).collect(Collectors.toList()));

            CriteriaQuery<users> Q = criteriaBuilder.createQuery(users.class);
            Root<users> user = Q.from(users.class);
            Q.distinct(true);

            Predicate finalPredicate = user.get("role").in(userRoles);

            if (search != null) {
                Predicate namePredicate = criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(user.get("name")), "%" + search.toLowerCase() + "%"));
                namePredicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(user.get("email")), "%" + search.toLowerCase() + "%"), namePredicate);
                namePredicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(user.get("phonenumber")), "%" + search.toLowerCase() + "%"), namePredicate);
                namePredicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(user.get("timestmp").as(String.class)), "%" + search.toLowerCase() + "%"), namePredicate);
                finalPredicate = criteriaBuilder.and(namePredicate, finalPredicate);
            }
            if (active != null)
                finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(user.get("active"), active));
            if (deleted != null) {
                finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.or(
                        criteriaBuilder.equal(user.get("deleted"), deleted),
                        criteriaBuilder.isNull(user.get("deleted"))
                ));
            }
            Q.where(finalPredicate);
            Q.orderBy(criteriaBuilder.desc(user.get("timestmp")));
            TypedQuery<users> typedQuery = em.createQuery(Q);

            return typedQuery.getResultList();

        } catch (NoResultException ex) {
            throw new NotFoundException("No data found.");
        }
    }

    public DynamicResponse FindAllPgSupplierAndcarmodelWithCriteria(int page, int size, Supplier supplier, String carmodel, String carmake, String carvin, AvailiabilityEnum availiability) {
        Pageable pageable = PageRequest.of(page, size);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Vehicles> query = builder.createQuery(Vehicles.class);
        Root<Vehicles> root = query.from(Vehicles.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("archive"), false));

        if (supplier != null) {
            predicates.add(builder.equal(root.get("supplier"), supplier));
        }

        if (carmodel != null) {
            predicates.add(builder.like(builder.lower(root.get("carmodel")), "%" + carmodel.toLowerCase() + "%"));
        }

        if (carmake != null) {
            predicates.add(builder.like(builder.lower(root.get("carmake")), "%" + carmake.toLowerCase() + "%"));
        }

        if (carvin != null) {
            predicates.add(builder.like(builder.lower(root.get("carvin")), "%" + carvin.toLowerCase() + "%"));
        }

        if (availiability != null) {
            predicates.add(builder.equal(root.get("availiability"), availiability));
        }

        query.where(builder.and(predicates.toArray(new Predicate[0])));
        query.orderBy(builder.desc(root.get("timestamp")));

        TypedQuery<Vehicles> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);

        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Vehicles> resultList = typedQuery.getResultList();
        long total = getTotalCountVehicles(predicates, builder, root);

        return new DynamicResponse(resultList, pageable.getPageNumber(), total, (int) Math.ceil((double) total / size));
    }

    private long getTotalCountVehicles(List<Predicate> predicates, CriteriaBuilder cb, Root<Vehicles> root) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Vehicles> countRoot = countQuery.from(Vehicles.class);
        countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
        return em.createQuery(countQuery).getSingleResult();
    }


    public DynamicResponse findEventsByRoleOrCategoryAndArchiveIsFalse(int page, int size, Boolean archive, RoleEnum role, users user) {
        Pageable pageable = PageRequest.of(page, size);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);

        query.distinct(true);
        root.fetch("categories", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("archive"), archive));

        if (role != null) {
            predicates.add(builder.isMember(role, root.get("assignedto")));
        }

        Subquery<Long> subquery = query.subquery(Long.class);
        Root<UsersCategory> categoryRoot = subquery.from(UsersCategory.class);
        Join<UsersCategory, users> usersJoin = categoryRoot.join("users");
        subquery.select(builder.literal(1L))
                .where(builder.and(
                        builder.equal(categoryRoot, root.join("categories")),
                        builder.equal(usersJoin, user)
                ));
        predicates.add(builder.exists(subquery));

        query.where(builder.and(predicates.toArray(new Predicate[0])));
        query.orderBy(builder.desc(root.get("timestamp")));

        TypedQuery<Event> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Event> resultList = typedQuery.getResultList();
        long total = getTotalCountEvents(predicates);

        return new DynamicResponse(resultList, page, total, (int) Math.ceil((double) total / size));
    }

    private long getTotalCountEvents(List<Predicate> predicates) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Event> countRoot = countQuery.from(Event.class);
        countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
        return em.createQuery(countQuery).getSingleResult();
    }
}
