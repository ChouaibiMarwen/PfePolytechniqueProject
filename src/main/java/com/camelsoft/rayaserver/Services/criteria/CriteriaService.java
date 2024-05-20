package com.camelsoft.rayaserver.Services.criteria;

import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Auth.RoleRepository;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CriteriaService {
    private final EntityManager em;
    private final CriteriaBuilder criteriaBuilder;
    @Autowired
    private RoleRepository roleRepository;

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
                finalPredicate = criteriaBuilder.and(finalPredicate,  criteriaBuilder.or(
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
                finalPredicate = criteriaBuilder.and(finalPredicate,  criteriaBuilder.or(
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

}
