package pl.marko.logback.composite.loggingevent;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.google.common.collect.ImmutableMap;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.willReturn;

public class ParametrisedMdcJsonNestedObjectProviderTest {

    private final static JsonFactory FACTORY = new MappingJsonFactory().enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);

    private final MdcJsonNestedObjectProvider provider;

    private final StringWriter writer;
    private final JsonGenerator generator;

    @Mock
    private ILoggingEvent event;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    ParametrisedMdcJsonNestedObjectProviderTest() throws IOException {
        writer = new StringWriter();
        generator = FACTORY.createGenerator(writer);
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

    private static Arguments jsonForMDC(String json, String k1, String v1, String k2, String v2) {
        return Arguments.of(
                json,
                ImmutableMap.of(
                        k1, v1,
                        k2, v2
                ));
    }

    private static Arguments jsonForEmptyMDC(String json) {
        return Arguments.of(
                json,
                ImmutableMap.of());
    }

    private static Arguments jsonForMDC(String json, String k1, String v1, String k2, String v2, String k3, String v3) {
        return Arguments.of(
                json,
                ImmutableMap.of(
                        k1, v1,
                        k2, v2,
                        k3, v3
                ));
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void assertEquals(String expectedJSONString, Map<String, String> mdcProperties) throws IOException, JSONException {


        generator.writeStartObject();

        willReturn(mdcProperties).given(event).getMDCPropertyMap();

        // when
        provider.writeTo(generator, event);

        generator.writeEndObject();
        generator.flush();

        // then
        JSONAssert.assertEquals(expectedJSONString, writer.getBuffer().toString(), false);
    }

}
