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
        return entityManager.createNamedQuery("findAll", MicroPost.class).getResultList();
    }

    public void add(MicroPost newMicroPost) {
        entityManager.persist(newMicroPost);
    }

    public void clear() {
        entityManager.createNamedQuery("clear").executeUpdate();
    }
}
