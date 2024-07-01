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
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.SuppliersClassification;
import com.camelsoft.rayaserver.Models.User.UsersCategory;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Auth.RoleRepository;
import com.camelsoft.rayaserver.Repository.Project.InvoiceRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
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
import java.util.stream.Collectors;

@Repository
public class CriteriaService {
    private final EntityManager em;
    private final CriteriaBuilder criteriaBuilder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private InvoiceRepository invoicerepository;

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

    public PageImpl<users> UsersSearchCreatiriaRolesListSubsupplier(int page, int size, Boolean active, String name, RoleEnum role, Boolean verified, users manager, Boolean deleted) {
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
                if(thirdparty){
                    predicates.add(criteriaBuilder.isNotNull(invoiceRoot.get("thirdpartypoid")));
                }else{
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

  /*  public PageImpl<Invoice> findAllByStatusAndRole(int page, int size, InvoiceStatus status, List<RoleEnum> role) {
        try {
            // Prepare criteria builder and query
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Invoice> criteriaQuery = criteriaBuilder.createQuery(Invoice.class);
            Root<Invoice> invoiceRoot = criteriaQuery.from(Invoice.class);

            // Prepare list to hold predicates
            List<Predicate> predicates = new ArrayList<>();

            // Combine createdby and relatedto invoices
            List<Invoice> invoicesList = new ArrayList<>();
            invoicesList.addAll(invoicerepository.findAllByCreatedby_Role_RoleIn(role));
            predicates.add(invoiceRoot.in(invoicesList));

            // Apply status filter if present
            if (status != null) {
                predicates.add(criteriaBuilder.equal(invoiceRoot.get("status"), status));
            }


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
    }*/

    public PageImpl<Invoice> findAllByStatusAndRole(int page, int size, InvoiceStatus status, List<RoleEnum> role, Integer invoicenumber, Long poid, String suppliername, users assignedto) {
        try {
            // Prepare criteria builder and query
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Invoice> criteriaQuery = criteriaBuilder.createQuery(Invoice.class);
            Root<Invoice> invoiceRoot = criteriaQuery.from(Invoice.class);

            // Prepare list to hold predicates
            List<Predicate> predicates = new ArrayList<>();

            // Combine createdby and relatedto invoices
            List<Invoice> invoicesList = new ArrayList<>();
            invoicesList.addAll(invoicerepository.findAllByCreatedby_Role_RoleIn(role));
            predicates.add(invoiceRoot.in(invoicesList));

            // Apply status filter if present
            if (status != null) {
                predicates.add(criteriaBuilder.equal(invoiceRoot.get("status"), status));
            }

            // Apply invoicenumber filter if present
            if (invoicenumber != null) {
                predicates.add(criteriaBuilder.equal(invoiceRoot.get("invoicenumber"), invoicenumber));
            }

            // Apply suppliername filter if present
            if (suppliername != null && !suppliername.isEmpty()) {
                predicates.add(criteriaBuilder.like(invoiceRoot.get("suppliername"), "%" + suppliername + "%"));
            }

            // Apply poid filter if present
            if (poid != null) {
                Join<Invoice, PurshaseOrder> purchaseOrderJoin = invoiceRoot.join("purshaseorder", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(purchaseOrderJoin.get("id"), poid));
            }

            if (assignedto != null) {
                Join<Invoice, PurshaseOrder> purchaseOrderJoin = invoiceRoot.join("purshaseorder", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(purchaseOrderJoin.get("subadminassignedto"), assignedto));
            }


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
        long total = getTotalCountVehicles(predicates);

        return new DynamicResponse(resultList, pageable.getPageNumber(), total, (int) Math.ceil((double) total / size));
    }
    private long getTotalCountVehicles(List<Predicate> predicates) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<PurshaseOrder> countRoot = countQuery.from(PurshaseOrder.class);
        countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
        return em.createQuery(countQuery).getSingleResult();
    }


    public DynamicResponse findEventsByRoleOrCategoryAndArchiveIsFalse(int page, int size, Boolean archive, RoleEnum role, users user) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        Join<Event, UsersCategory> categoryJoin = eventRoot.join("categories", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(eventRoot.get("archive"), archive));

        // Check if role is in assignedto set
        predicates.add(cb.isMember(role, eventRoot.get("assignedto")));

        // Check if user belongs to any category of the event
        Subquery<UsersCategory> subquery = query.subquery(UsersCategory.class);
        Root<UsersCategory> subqueryRoot = subquery.from(UsersCategory.class);
        subquery.select(subqueryRoot)
                .where(cb.equal(subqueryRoot.get("category"), categoryJoin),
                        cb.equal(subqueryRoot.get("user"), user));
        predicates.add(cb.exists(subquery));

        query.select(eventRoot)
                .distinct(true)
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .orderBy(cb.desc(eventRoot.get("timestamp")));

        TypedQuery<Event> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);

        List<Event> resultList = typedQuery.getResultList();

        // Total count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Event> countRoot = countQuery.from(Event.class);
        countQuery.select(cb.countDistinct(countRoot))
                .where(cb.and(predicates.toArray(new Predicate[0])));
        Long total = em.createQuery(countQuery).getSingleResult();

        int totalPages = (int) Math.ceil((double) total / size);

        return new DynamicResponse(resultList, page, total, totalPages);
    }
}
