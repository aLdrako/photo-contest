package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

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
    public void delete(User user) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(user);
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
            Query<User> query = session.createQuery("from User where isOrganizer = true", User.class);
            return query.list();
        }
    }

}
