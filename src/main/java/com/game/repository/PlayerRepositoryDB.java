package com.game.repository;

import com.game.entity.Player;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import java.util.Properties;


@Repository(value = "db")
@Transactional
public class PlayerRepositoryDB implements IPlayerRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @PersistenceContext
    private EntityManager entityManager;

    private Properties properties;

    public PlayerRepositoryDB() {
        properties = new Properties();
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/rpg");
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        Query query = entityManager.createNativeQuery("SELECT * FROM player", Player.class);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public int getAllCount() {
        TypedQuery<Long> query = entityManager.createNamedQuery("Player.getAllCount", Long.class);
        return query.getSingleResult().intValue();
    }

    @Override
    public Player save(Player player) {
        entityManager.persist(player);
        return player;
    }

    @Override
    public Player update(Player player) {
        return entityManager.merge(player);
    }

    @Override
    public Optional<Player> findById(long id) {
        return Optional.ofNullable(entityManager.find(Player.class, id));
    }

    @Override
    public void delete(Player player) {
        entityManager.remove(player);
    }

    @PreDestroy
    public void beforeStop() {
        sessionFactory.close();
    }
}
