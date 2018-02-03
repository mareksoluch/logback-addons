package pl.marko.logback.composite.loggingevent;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.net.URL;

public class JsonLogginTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonLogginTest.class);

    @Test public void
    ahouldLogArrayValue(){
        URL resource = this.getClass().getResource("logback.xml");
        System.out.println(resource);
        MDC.put("arraKey", "one, two, tree");
        MDC.put("camel.one", "one");
        MDC.put("camel.two", "2");
        MDC.put("camel.tree", "trzy");

        LOGGER.debug("test log");
    }

}
