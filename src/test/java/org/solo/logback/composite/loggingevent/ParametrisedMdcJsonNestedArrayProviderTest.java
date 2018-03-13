package org.solo.logback.composite.loggingevent;

import org.json.JSONException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

public class ParametrisedMdcJsonNestedArrayProviderTest extends AbstractParametrisedMdcJsonProviderTest {

    private final MdcJsonNestedArrayProvider provider;

    ParametrisedMdcJsonNestedArrayProviderTest() throws IOException {
        super();
        provider = new MdcJsonNestedArrayProvider();
        provider.addArrayFieldName("a.b.c");
        provider.addArrayFieldName("x.y");
        provider.addArrayFieldName("array");
    }

    private static Stream<Arguments> parameters() {
        return Stream.of(
                jsonForEmptyMDC("{}"),
                jsonForMDC("{\"a\":{\"b\":{\"c\":[\"one\",\"2\",\"trzy\"]},\"c\":{\"d\":{\"e\":\"value2\"}}},\"x\":{\"y\":[\"z\",\"e\",\"t\"]}}",
                        "a.b.c", "one,2,trzy",
                        "a.c.d.e", "value2",
                        "x.y", "z,e,t"),
                jsonForMDC("{\"array\":[\"one\",\"2\",\"trzy\"],\"notConfiguredArray\":\"a,r,r,a,y\"}",
                        "array", "one,2,trzy",
                        "notConfiguredArray", "a,r,r,a,y")
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
