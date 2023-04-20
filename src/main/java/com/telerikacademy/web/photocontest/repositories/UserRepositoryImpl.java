package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.models.Ranking;
import com.telerikacademy.web.photocontest.models.Ranks;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("from User where isDeleted = false", User.class);
            return query.list();
        }
    }

    @Override
    public User getById(Long id) {
        try (Session session = sessionFactory.openSession()){
            User user = session.get(User.class, id);
            if (user == null || user.isDeleted()) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("from User where email = :email " +
                    "and isDeleted = false", User.class);
            query.setParameter("email", email);
            List<User> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "email", email);
            }
            return result.get(0);
        }
    }
    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("from User where username = :username" +
                    " and isDeleted = false", User.class);
            query.setParameter("username", username);
            List<User> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "username", username);
            }
            return result.get(0);
        }
    }

    @Override
    public List<User> getAllOrganizers() {
        try (Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("from User where isOrganizer = true " +
                    "and isDeleted = false", User.class);
            return query.list();
        }
    }

    @Override
    public List<User> getUsersWithJuryPermission() {
        try (Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("from User where rank.name = :master " +
                    "or rank.name = :wise " +
                    "and isDeleted = false", User.class);
            query.setParameter("master", Ranks.MASTER.toString());
            query.setParameter("wise", Ranks.WISE_AND_BENEVOLENT_PHOTO_DICTATOR.toString());
            return query.list();
        }
    }

    @Override
    public List<User> search(Optional<String> keyword) {
        String query = keyword.orElse("");
        try (Session session = sessionFactory.openSession()){
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> userRoot = criteriaQuery.from(User.class);
            Predicate firstName = criteriaBuilder.equal(userRoot.get("firstName"), query);
            Predicate lastName = criteriaBuilder.equal(userRoot.get("lastName"), query);
            Predicate username = criteriaBuilder.equal(userRoot.get("username"), query);
            Predicate search = criteriaBuilder.or(firstName, lastName, username);
            Predicate finalPredicate = criteriaBuilder.and(search,
                    criteriaBuilder.equal(userRoot.get("isDeleted"), false));
            criteriaQuery.where(finalPredicate);
            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<User> query = getUserCriteriaQuery(session);
            return getUsers(pageable, session, query);
        }

    }

    @Override
    public Page<User> findAllJunkies(Pageable pageable, String sortBy, String orderBy) {
        try (Session session = sessionFactory.openSession()){
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> userRoot = criteriaQuery.from(User.class);
            Join<User, Ranking> joinRank = userRoot.join("rank", JoinType.LEFT);
            Expression<String> rankExpression = joinRank.get("name");

            Order order = extractOrder(rankExpression, sortBy, orderBy, criteriaBuilder, userRoot);
            Predicate predicate = criteriaBuilder.and(criteriaBuilder.equal(userRoot.get("isOrganizer"), false),
                    criteriaBuilder.equal(userRoot.get("isDeleted"), false));

            criteriaQuery.where(predicate)
                    .orderBy(order);

            return getUsers(pageable, session, criteriaQuery);
        }
    }

    @NotNull
    private Page<User> getUsers(Pageable pageable, Session session, CriteriaQuery<User> criteriaQuery) {
        TypedQuery<User> typedQuery = session.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<User> users = typedQuery.getResultList();

        return new PageImpl<>(users, pageable, getAll().size());
    }

    private Order extractOrder(Expression<String> rankExpression, String sortBy, String orderBy,
                               CriteriaBuilder criteriaBuilder, Root<User> userRoot) {
        List<String> rankOrder = Arrays.asList("Junkie", "Enthusiast", "Master",
                "Wise and Benevolent Photo Dictator");
        return orderBy.equals("desc") ? criteriaBuilder.desc(extractSort(criteriaBuilder,
                sortBy, rankOrder, rankExpression, userRoot)) : criteriaBuilder.asc(extractSort(criteriaBuilder,
                sortBy, rankOrder, rankExpression, userRoot));
    }

    private Expression<?> extractSort(CriteriaBuilder criteriaBuilder, String sortBy, List<String> rankOrder,
                                      Expression<String> rankExpression, Root<User> userRoot) {
        return sortBy.equals("rank") ? criteriaBuilder.function("FIELD", String.class, rankExpression,
                criteriaBuilder.literal(rankOrder)) : userRoot.get("id");
    }

    private CriteriaQuery<User> getUserCriteriaQuery(Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(builder.equal(root.get("isDeleted"), false));
        return query;
    }




}
