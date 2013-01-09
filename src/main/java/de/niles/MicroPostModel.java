package de.niles;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Model
public class MicroPostModel {
    @Inject
    MicroPostRepository microPostRepository;

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
            microPosts = microPostRepository.findAll();
        }
        return microPosts;
    }

    public void save() {
        System.out.println("saved");
        microPostRepository.add(newMicroPost);
        initNew();
        microPosts = null;
    }
}
