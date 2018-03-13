package org.solo.logback.composite.loggingevent;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

public class ParametrisedMdcJsonNestedObjectProviderTest extends AbstractParametrisedMdcJsonProviderTest {

    private final MdcJsonNestedObjectProvider provider;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    ParametrisedMdcJsonNestedObjectProviderTest() throws IOException {
        provider = new MdcJsonNestedObjectProvider();
    }

    private static Stream<Arguments> parameters() {
        return Stream.of(
                jsonForEmptyMDC("{}"),
                jsonForMDC("{\"a\":{\"b\":\"value1\",\"c\":{\"d\":{\"e\":\"value2\"}},\"f\":{\"g\":\"value3\"}}}",
                        "a.b", "value1",
                        "a.c.d.e", "value2",
                        "a.f.g", "value3"),
                jsonForMDC("{\"a\":{\"b\":\"value1\",\"f\":{\"g\":\"value3\"}},\"c\":{\"d\":{\"e\":\"value2\"}}}",
                        "a.b", "value1",
                        "c.d.e", "value2",
                        "a.f.g", "value3"),
                jsonForMDC("{\"a\":{\"b\":\"value1\",\"c\":{\"a\":{\"e\":\"value2\"}}}}",
                        "a.b", "value1",
                        "a.c.a.e", "value2"),
                jsonForMDC("{\"a\":\"value1\",\"b\":\"value2\"}",
                        "a", "value1",
                        "b", "value2")
        );
    }

    @Override
    AbstractMdcJsonProvider getProvider() {
        return provider;
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void assertEquals(String expectedJSONString, Map<String, String> mdcProperties) throws IOException, JSONException {
        super.assertEquals(expectedJSONString, mdcProperties);
    }

}
