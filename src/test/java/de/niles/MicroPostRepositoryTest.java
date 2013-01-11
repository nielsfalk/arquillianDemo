package de.niles;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;

@RunWith(Arquillian.class)
public class MicroPostRepositoryTest {
    public static final MicroPost INITIAL_POST = new MicroPost("Franz", "Ich habe Hunger!");

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(MicroPostRepository.class, MicroPost.class)
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml");
    }

    @EJB
    MicroPostRepository microPostRepository;

    @Before
    public void initDB() {
        microPostRepository.clear();
        microPostRepository.add(INITIAL_POST);
    }

    @Test
    public void add() {
        MicroPost microPost = new MicroPost("Fritz", "Ich habe Hunger!");
        microPostRepository.add(microPost);
        assertThat(microPostRepository.findAll(), hasItem(microPost));
    }

    @Test
    public void findAll() {
        assertThat(microPostRepository.findAll(), hasItem(INITIAL_POST));
    }

    @Test
    public void clear() {
        microPostRepository.clear();
        assertThat(microPostRepository.findAll().isEmpty(), is(true));
    }

}
