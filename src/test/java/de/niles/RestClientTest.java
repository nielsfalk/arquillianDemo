package de.niles;


import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@RunWith(Arquillian.class)
@Ignore
public class RestClientTest {

    @Deployment(testable = false)
    public static WebArchive create() {
        return ShrinkWrap.create(WebArchive.class)
                //.addPackage(Customer.class.getPackage())
                //.addClasses(JaxRsActivator.class)
                //.addAsResource("import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml");

    }

    @Test
    @GET
    @Path("rest/microPost/1")
    @Consumes(MediaType.APPLICATION_XML)
    public void shouldBeAbleToListAllCustomers(ClientResponse<List<MicroPost>> response) {
        Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());


    }
}
