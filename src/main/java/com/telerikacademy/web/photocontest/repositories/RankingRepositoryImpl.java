package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.models.Ranking;
import com.telerikacademy.web.photocontest.repositories.contracts.RankingRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RankingRepositoryImpl implements RankingRepository {
    private final SessionFactory sessionFactory;

    public RankingRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Ranking getByName(String name) {
        try (Session session = sessionFactory.openSession()){
            Query<Ranking> query = session.createQuery("from Ranking where name = :name", Ranking.class);
            query.setParameter("name", name);
            List<Ranking> list = query.list();
            if (list.size() == 0) {
                throw new EntityNotFoundException("Ranking", "name", name);
            }
            return list.get(0);
        }
    }

    @Override
    public void create(Ranking ranking) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(ranking);
            session.getTransaction().commit();
        }
    }
}
