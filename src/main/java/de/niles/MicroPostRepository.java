package de.niles;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class MicroPostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<MicroPost> findAll() {
        return entityManager.createQuery("select posts from MicroPost posts order by posts.id desc ").getResultList();
    }

    public void add(MicroPost newMicroPost) {
        entityManager.persist(newMicroPost);
    }

    public void clear() {
        entityManager.createQuery("delete from MicroPost").executeUpdate();
    }
}
