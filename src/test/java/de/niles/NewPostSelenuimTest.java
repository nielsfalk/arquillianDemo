package de.niles;

import com.thoughtworks.selenium.DefaultSelenium;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URL;

@RunWith(Arquillian.class)
public class NewPostSelenuimTest {
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(MicroPost.class.getPackage())
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
                .addAsWebResource(new File("src/main/webapp/index.xhtml"));
    }

    @Drone
    DefaultSelenium browser;

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void microPostShouldBePosted() {
        browser.open(deploymentUrl.toString().replaceFirst("/$", "") + "/");

        browser.type("id=newForm:author", "niels");
        browser.type("id=newForm:content", "das ist ein Test");
        browser.click("id=newForm:save");
        browser.waitForPageToLoad("15000");

        Assert.assertTrue("Micropost should exist",
                browser.isElementPresent("xpath=//td[contains(text(),'das ist ein Test')]"));
    }
}
