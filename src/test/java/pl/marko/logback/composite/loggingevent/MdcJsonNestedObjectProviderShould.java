package pl.marko.logback.composite.loggingevent;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

public class MdcJsonNestedObjectProviderShould {

    private MdcJsonNestedObjectProvider provider = new MdcJsonNestedObjectProvider();

    @Mock
    private JsonGenerator generator;

    @Mock
    private ILoggingEvent event;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }


    @Test public void
    writeSimpleNestedObject() throws IOException {
        // given
        mdcEntryInEvent("a.b", "value");
        InOrder inOrder = inOrder(generator);
        // when
        provider.writeTo(generator, event);
        // then
        inOrder.verify(generator).writeFieldName(eq("a"));
        inOrder.verify(generator).writeStartObject();
        inOrder.verify(generator).writeFieldName(eq("b"));
        inOrder.verify(generator).writeObject(eq("value"));
        inOrder.verify(generator).writeEndObject();
    }

    @Test public void
    writeNestedObject() throws IOException {
        // given
        Map<String, String> mdcProperties = ImmutableMap.of(
                "a.b", "value1",
                "a.c", "value2");
        willReturn(mdcProperties).given(event).getMDCPropertyMap();

        InOrder inOrder = inOrder(generator);
        // when
        provider.writeTo(generator, event);
        // then
        inOrder.verify(generator).writeFieldName(eq("a"));
        inOrder.verify(generator).writeStartObject();
        inOrder.verify(generator).writeFieldName(eq("b"));
        inOrder.verify(generator).writeObject(eq("value1"));
        inOrder.verify(generator).writeFieldName(eq("c"));
        inOrder.verify(generator).writeObject(eq("value2"));
        inOrder.verify(generator).writeEndObject();
    }

    @Test public void
    writeNestedObjectWithDifferentDepth() throws IOException {
        // given
        Map<String, String> mdcProperties = ImmutableMap.of(
                "a.b", "value1",
                "a.c", "value2",
                "a.d.e", "value3");
        willReturn(mdcProperties).given(event).getMDCPropertyMap();

        InOrder inOrder = inOrder(generator);
        // when
        provider.writeTo(generator, event);
        // then
        inOrder.verify(generator).writeFieldName(eq("a"));
        inOrder.verify(generator).writeStartObject();
        inOrder.verify(generator).writeFieldName(eq("b"));
        inOrder.verify(generator).writeObject(eq("value1"));
        inOrder.verify(generator).writeFieldName(eq("c"));
        inOrder.verify(generator).writeObject(eq("value2"));
        inOrder.verify(generator).writeFieldName(eq("d"));
        inOrder.verify(generator).writeStartObject();
        inOrder.verify(generator).writeFieldName(eq("e"));
        inOrder.verify(generator).writeObject(eq("value3"));
        inOrder.verify(generator, times(2)).writeEndObject();
    }

    private void mdcEntryInEvent(String key, String value) {
        willReturn(ImmutableMap.of(key, value)).given(event).getMDCPropertyMap();
    }

}
