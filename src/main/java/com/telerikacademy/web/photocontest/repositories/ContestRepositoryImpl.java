package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.repositories.contracts.ContestRepository;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// TODO Repo is disabled
//@Repository
@AllArgsConstructor
public class ContestRepositoryImpl {

    private final SessionFactory sessionFactory;
    public Iterable<Contest> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Contest> contestList = session.createQuery("from Contest", Contest.class);
            return contestList.getResultList();
        }
    }
}
