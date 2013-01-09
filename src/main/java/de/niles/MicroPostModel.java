package de.niles;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Model
@Stateful
public class MicroPostModel {
    @PersistenceContext
    private EntityManager entityManager;

    private MicroPost newMicroPost;
    private List<MicroPost> microPosts;

    @Produces
    @Named
    public MicroPost getNewPost() {
        return newMicroPost;
    }

    @PostConstruct
    public void initNew() {
        newMicroPost = new MicroPost();
    }

    @Produces
    @Named
    public List<MicroPost> getPosts() {
        if (microPosts == null) {
            microPosts = entityManager.createQuery("select posts from MicroPost posts order by posts.id desc ").getResultList();
        }
        return microPosts;
    }

    public void save() {
        System.out.println("saved");
        entityManager.persist(newMicroPost);
        initNew();
        microPosts = null;
    }
}
