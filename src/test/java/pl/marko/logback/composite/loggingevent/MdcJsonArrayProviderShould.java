package pl.marko.logback.composite.loggingevent;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.collect.ImmutableMap;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

public class MdcJsonArrayProviderShould {

    private static final String SINGE_VALUE_TABLE = "singeValueTable";
    private static final String MULTI_VALUE_TABLE = "multiValueTable";
    private static final String NOT_CONFIGURED_PROPERTY = "notConfiguredProperty";
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private MdcJsonArrayProvider provider = new MdcJsonArrayProvider();

    @Mock
    private JsonGenerator generator;

    @Mock
    private ILoggingEvent event;

    @Test public void
    convertSingleValueFieldToArray() throws IOException {
        // given
        mdcEntryInEvent(SINGE_VALUE_TABLE, "a");
        // when
        provider.writeTo(generator, event);
        // then
        verify(generator).writeFieldName(eq(SINGE_VALUE_TABLE));
        verify(generator).writeObject(argThat(containsInAnyOrder("a")));
    }

    @Test public void
    convertMultiValueFieldToArray() throws IOException {
        // given
        mdcEntryInEvent(MULTI_VALUE_TABLE, "a,b,c,d");
        // when
        provider.writeTo(generator, event);
        // then
        verify(generator).writeFieldName(eq(MULTI_VALUE_TABLE));
        verify(generator).writeObject(argThat(containsInAnyOrder("a", "b", "c", "d")));
    }

    @Test public void
    notConvertToArrayIfFieldNotConfigured() throws IOException {
        // given
        willReturn(map(NOT_CONFIGURED_PROPERTY, "e,f,g,h")).given(event).getMDCPropertyMap();
        // when
        provider.writeTo(generator, event);
        // then
        verify(generator).writeFieldName(eq(NOT_CONFIGURED_PROPERTY));
        verify(generator).writeObject(eq("e,f,g,h"));
    }

    @Test public void
    notWriteAnyPropertyIfInExcludedProperties() throws IOException {
        // given
        provider.setExcludeMdcKeyNames(asList(NOT_CONFIGURED_PROPERTY));
        willReturn(map(NOT_CONFIGURED_PROPERTY, "e,f,g,h")).given(event).getMDCPropertyMap();
        // when
        provider.writeTo(generator, event);
        // then
        verifyNoMoreInteractions(generator);
    }

    @Test public void
    notWriteAnyPropertyIfNotInIncludedProperties() throws IOException {
        // given
        provider.setIncludeMdcKeyNames(asList("someOtherProperties"));
        willReturn(map(NOT_CONFIGURED_PROPERTY, "e,f,g,h")).given(event).getMDCPropertyMap();
        // when
        provider.writeTo(generator, event);
        // then
        verifyNoMoreInteractions(generator);
    }

    private Map<String, String> map(String key, String value) {
        return ImmutableMap.of(key, value);
    }

    private void mdcEntryInEvent(String key, String value) {
        provider.addArrayFieldName(key);
        willReturn(map(key, value)).given(event).getMDCPropertyMap();
    }

}