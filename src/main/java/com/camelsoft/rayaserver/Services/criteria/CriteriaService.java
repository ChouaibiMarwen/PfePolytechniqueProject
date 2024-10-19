package com.camelsoft.rayaserver.Services.criteria;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceRelated;
import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Enum.Project.MissionStatusEnum;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.Project.Mission;
import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Auth.RoleRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
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
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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


}
