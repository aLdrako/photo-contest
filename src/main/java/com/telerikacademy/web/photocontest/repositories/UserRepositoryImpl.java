package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.models.Ranks;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
            Query<User> query = session.createQuery("from User where email = :email", User.class);
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
            Query<User> query = session.createQuery("from User where username = :username", User.class);
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

}
