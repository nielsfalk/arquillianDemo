package de.niles.rest;

import de.niles.MicroPost;
import de.niles.MicroPostRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.net.URL;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;

@RunWith(Arquillian.class)
@RunAsClient
public class MicroPostResourceTest {
    private static final String RESOURCE_PREFIX = RestConfig.class.getAnnotation(ApplicationPath.class).value().substring(1);
    public static final MicroPost INITIAL_POST = new MicroPost("Niels", "Das ist ein Test");

    @Deployment
    public static WebArchive create() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(DataPump.class, MicroPostRepository.class, AbstractResource.class, MicroPostResource.class, RestConfig.class, MicroPost.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml");

    }

    @ArquillianResource
    URL deploymentUrl;

    @Test
    @GET
    @Path("/rest/microPost/1")
    @Consumes(APPLICATION_XML)
    @Ignore("doesn't work, but would be nice")
    public void findAllRestEasy(ClientResponse<MicroPost> response) {
        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }

    @Test
    public void findAll() {
        ClientResponse<List<MicroPost>> response = getClientResponse("/microPost", (Class<List<MicroPost>>) (Class<?>) List.class);
        assertThat(response.getEntity(new GenericType<List<MicroPost>>() {
        }), hasItem(INITIAL_POST));
    }

    @Test
    public void find() {
        ClientResponse<MicroPost> response = getClientResponse("/microPost/1", MicroPost.class);
        MicroPost entity = response.getEntity(new GenericType<MicroPost>() {
        });
        assertThat(entity, is(INITIAL_POST));
    }

    @Test
    public void findAsTextMediaTypeJSON() throws Exception {
        ClientRequest request = new ClientRequest(deploymentUrl.toString() + RESOURCE_PREFIX + "/microPost/1");
        request.header("Accept", APPLICATION_JSON);

        ClientResponse<String> responseObj = request.get(String.class);
        assertThat(responseObj.getStatus(), is(OK.getStatusCode()));

        String response = responseObj.getEntity().replaceAll("<\\?xml.*\\?>", "").trim();

        assertThat(response, is("{\"author\":\"Niels\",\"content\":\"Das ist ein Test\",\"id\":\"1\"}"));
    }


    private <T> ClientResponse<T> getClientResponse(String path, Class<T> returnType) {
        ClientRequest request = new ClientRequest(deploymentUrl.toString() + RESOURCE_PREFIX + path);
        request.header("Accept", APPLICATION_XML);
        try {
            ClientResponse<T> response = request.get(returnType);
            assertThat(response.getStatus(), is(OK.getStatusCode()));
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Singleton
    @Startup
    public static class DataPump {
        @EJB
        MicroPostRepository ejb;

        @PostConstruct
        public void insetData() {
            ejb.add(INITIAL_POST);
        }
    }
}
