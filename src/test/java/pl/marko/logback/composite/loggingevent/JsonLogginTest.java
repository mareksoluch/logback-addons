package pl.marko.logback.composite.loggingevent;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class JsonLogginTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonLogginTest.class);

    @Test public void
    jsonProvidersExample() {
        MDC.put("arraKey1", "singleValue");
        MDC.put("arraKey2", "one, two, tree");
        MDC.put("arraKey3", null);
        MDC.put("root.one", "one");
        MDC.put("root.two", "2");
        MDC.put("root.tree", "trzy");

        MDC.put("a.b", "value1");
        MDC.put("a.c.d.e", "value2");
        MDC.put("a.f.g", "value3");

        LOGGER.debug("test log");
    }

}
