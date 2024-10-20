package com.smarty.pfeserver.Services.criteria;

import com.smarty.pfeserver.Enum.Project.MissionStatusEnum;
import com.smarty.pfeserver.Enum.User.RoleEnum;
import com.smarty.pfeserver.Models.Auth.Role;
import com.smarty.pfeserver.Models.Project.Mission;
import com.smarty.pfeserver.Models.Project.Transaction;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Repository.Auth.RoleRepository;
import com.smarty.pfeserver.Tools.Exception.NotFoundException;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CriteriaService {
    private final EntityManager em;
    private final CriteriaBuilder criteriaBuilder;
    @Autowired
    private RoleRepository roleRepository;
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
    public List<Mission> findMissionsWithFilters(String title, Date startDate, Date endDate, users teamLead, MissionStatusEnum status, users participant) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Mission> cq = cb.createQuery(Mission.class);
        Root<Mission> mission = cq.from(Mission.class);

        List<Predicate> predicates = new ArrayList<>();

        // Title filter
        if (title != null && !title.isEmpty()) {
            predicates.add(cb.like(mission.get("title"), "%" + title + "%"));
        }

        // Start date filter
        if (startDate != null && endDate == null) {
            predicates.add(cb.greaterThanOrEqualTo(mission.get("startdate"), startDate));
        }

        // End date filter
        if (endDate != null && startDate == null) {
            predicates.add(cb.lessThanOrEqualTo(mission.get("startdate"), endDate));
        }

        // Between start and end date filter
        if (startDate != null && endDate != null) {
            predicates.add(cb.between(mission.get("startdate"), startDate, endDate));
        }

        // Team lead filter
        if (teamLead != null) {
            predicates.add(cb.equal(mission.get("teamLead"), teamLead));
        }

        // Status filter
        if (status != null) {
            predicates.add(cb.equal(mission.get("status"), status));
        }

        // Participant filter
        if (participant != null) {
            predicates.add(cb.isMember(participant, mission.get("participants")));
        }

        // Combine all predicates
        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Mission> query = em.createQuery(cq);
        return query.getResultList();
    }

    public List<Transaction> getFilteredTransactions(String missionTitle, Long missionId, Long technicianId, Date startDate, Date endDate) {
        // Step 1: Create a CriteriaQuery for Transaction
        CriteriaQuery<Transaction> criteriaQuery = criteriaBuilder.createQuery(Transaction.class);
        Root<Transaction> transactionRoot = criteriaQuery.from(Transaction.class);

        // Step 2: Initialize a list to hold the filtering conditions (Predicates)
        List<Predicate> predicates = new ArrayList<>();

        // Step 3: Joins and filtering logic

        // Join Transaction with Mission
        Join<Transaction, Mission> missionJoin = transactionRoot.join("mission", JoinType.INNER);

        // Filter by mission title (if provided)
        if (missionTitle != null && !missionTitle.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(missionJoin.get("title")), "%" + missionTitle.toLowerCase() + "%"));
        }

        // Filter by mission ID (if provided)
        if (missionId != null) {
            predicates.add(criteriaBuilder.equal(missionJoin.get("id"), missionId));
        }

        // Filter by createdBy user (technician) ID (if provided)
        if (technicianId != null) {
            Join<Transaction, users> createdByJoin = transactionRoot.join("createdby", JoinType.INNER);
            predicates.add(criteriaBuilder.equal(createdByJoin.get("id"), technicianId));
        }

        // Filter by start date (if provided)
        if (startDate != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(transactionRoot.get("timestamp"), startDate));
        }

        // Filter by end date (if provided)
        if (endDate != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(transactionRoot.get("timestamp"), endDate));
        }

        // Step 4: Apply the predicates (filtering conditions) to the query
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        // Step 5: Add ordering by timestamp in descending order
        criteriaQuery.orderBy(criteriaBuilder.desc(transactionRoot.get("timestamp")));

        // Step 6: Execute the query and return the results
        return em.createQuery(criteriaQuery).getResultList();
    }

}
