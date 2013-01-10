package de.niles;

import com.thoughtworks.selenium.DefaultSelenium;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.io.File;
import java.net.URL;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class ClientOrNotTest {
    private boolean inClientMode;

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(MicroPost.class.getPackage())
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
                .addAsWebResource(new File("src/main/webapp/index.xhtml"));
    }

    @EJB
    MicroPostRepository ejb;

    @Drone
    DefaultSelenium drone;

    @Before
    public void setUp() {
        inClientMode = ejb == null;
    }

    @Test
    public void noClientMode() {
        assertThat(inClientMode, is(false));
        assertThat(drone, is(nullValue()));
    }

    @Test
    @RunAsClient
    public void clientMode(@ArquillianResource URL deploymentUrl) {
        assertThat(inClientMode, is(true));
        assertThat(deploymentUrl, is(not(nullValue())));
        assertThat(drone, is(not(nullValue())));
    }

}
