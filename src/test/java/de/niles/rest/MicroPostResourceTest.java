package de.niles.rest;

import de.niles.MicroPost;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.List;

@RunWith(Arquillian.class)
@Ignore
public class MicroPostResourceTest {
    private static final String RESOURCE_PREFIX = RestConfig.class.getAnnotation(ApplicationPath.class).value().substring(1);


    @Deployment(testable = false)
    public static WebArchive create() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(AbstractResource.class, MicroPostResource.class, RestConfig.class, MicroPost.class)
                .addAsResource("import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml");

    }

    @ArquillianResource
    URL deploymentUrl;

    @Test
    @GET
    @Path("rest/microPost")
    @Consumes(MediaType.APPLICATION_XML)
    public void shouldBeAbleToListAllCustomers(ClientResponse<List<MicroPost>> response) {
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        ClientRequest request = new ClientRequest(deploymentUrl.toString() + RESOURCE_PREFIX + "/microPost");

    }

    @Test
    public void testCreate() throws Exception {

    }

    @Test
    public void testRemove() throws Exception {

    }

    @Test
    public void testFind() throws Exception {

    }

    @Test
    public void testFindAll() throws Exception {

    }

    @Test
    public void testFindRange() throws Exception {

    }

    @Test
    public void testCountREST() throws Exception {

    }
}
