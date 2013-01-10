package de.niles.rest;

import de.niles.MicroPost;
import de.niles.MicroPostRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.net.URL;
import java.util.List;

import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class MicroPostResourceTest {
    private static final String RESOURCE_PREFIX = RestConfig.class.getAnnotation(ApplicationPath.class).value().substring(1);


    @Deployment(name = "rest")
    public static WebArchive create() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(MicroPostRepository.class, AbstractResource.class, MicroPostResource.class, RestConfig.class, MicroPost.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml");

    }


    @EJB
    MicroPostRepository ejb;

    @Test
    @Ignore
    public void insetData() {
        ejb.add(new MicroPost("Niels", "Das ist ein Test"));
    }

    @Test
    @GET
    @Path("rest/microPost")
    @Consumes(MediaType.APPLICATION_XML)
    @RunAsClient
    @Ignore
    public void tryRestEasy(ClientResponse<List<MicroPost>> response) {
        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }

    @Test
    @RunAsClient
    public void findAll(@ArquillianResource URL deploymentUrl) throws Exception {
        ClientRequest request = new ClientRequest(deploymentUrl.toString() + RESOURCE_PREFIX + "/microPost");
        request.header("Accept", MediaType.APPLICATION_XML);
        //ClientResponse<List<MicroPost>> responseList= request.get((Class<List<MicroPost>>)(Class<?>)List.class);

        //responseList.getEntity()
        ClientResponse<String> responseObj = request.get(String.class);


        junit.framework.Assert.assertEquals(200, responseObj.getStatus());

        String response = responseObj.getEntity().replaceAll("<\\?xml.*\\?>", "").trim();
        assertThat(response, is("<microPosts></microPosts>"));
    }

    public static void main(String[] args) {
        System.out.println(List.class);
    }
}
