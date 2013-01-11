package de.niles.rest;

import javax.persistence.EntityManager;
import java.util.List;

@SuppressWarnings("unchecked")
abstract class AbstractResource<T> {
    private final Class<T> entityClass;

    AbstractResource(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    void create(T entity) {
        getEntityManager().persist(entity);
    }

    void edit(T entity) {
        getEntityManager().merge(entity);
    }

    void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
