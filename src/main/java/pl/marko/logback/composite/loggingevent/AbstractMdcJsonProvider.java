package pl.marko.logback.composite.loggingevent;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.core.JsonGenerator;
import net.logstash.logback.composite.AbstractFieldJsonProvider;
import net.logstash.logback.composite.FieldNamesAware;
import net.logstash.logback.fieldnames.LogstashFieldNames;

import java.io.IOException;
import java.util.*;

abstract class AbstractMdcJsonProvider extends AbstractFieldJsonProvider<ILoggingEvent> implements FieldNamesAware<LogstashFieldNames> {

    protected List<String> includeMdcKeyNames = new ArrayList<>();
    protected List<String> excludeMdcKeyNames = new ArrayList<>();

    @Override
    public void start() {
        if (!this.includeMdcKeyNames.isEmpty() && !this.excludeMdcKeyNames.isEmpty()) {
            addError("Both includeMdcKeyNames and excludeMdcKeyNames are not empty.  Only one is allowed to be not empty.");
        }
        super.start();
    }

    @Override
    public void writeTo(JsonGenerator generator, ILoggingEvent event) throws IOException {
        logMDC(generator, event.getMDCPropertyMap());
    }

    private void logMDC(JsonGenerator generator, Map<String, String> mdcProperties) throws IOException {
        if (mdcProperties != null && !mdcProperties.isEmpty()) {
            if (getFieldName() != null) {
                generator.writeObjectFieldStart(getFieldName());
            }
            if (!getIncludeMdcKeyNames().isEmpty()) {
                mdcProperties = new HashMap<>(mdcProperties);
                mdcProperties.keySet().retainAll(getIncludeMdcKeyNames());
            }
            if (!getExcludeMdcKeyNames().isEmpty()) {
                mdcProperties = new HashMap<>(mdcProperties);
                mdcProperties.keySet().removeAll(getExcludeMdcKeyNames());
            }
            writeProperties(generator, mdcProperties);
            if (getFieldName() != null) {
                generator.writeEndObject();
            }
        }
    }

    abstract void writeProperties(JsonGenerator generator, Map<String, String> mdcProperties) throws IOException;


    @Override
    public void setFieldNames(LogstashFieldNames fieldNames) {
        setFieldName(fieldNames.getMdc());
    }

    public List<String> getIncludeMdcKeyNames() {
        return Collections.unmodifiableList(includeMdcKeyNames);
    }

    public void addIncludeMdcKeyName(String includedMdcKeyName) {
        this.includeMdcKeyNames.add(includedMdcKeyName);
    }

    public void setIncludeMdcKeyNames(List<String> includeMdcKeyNames) {
        this.includeMdcKeyNames = new ArrayList<>(includeMdcKeyNames);
    }

    public List<String> getExcludeMdcKeyNames() {
        return Collections.unmodifiableList(excludeMdcKeyNames);
    }

    public void addExcludeMdcKeyName(String excludedMdcKeyName) {
        this.excludeMdcKeyNames.add(excludedMdcKeyName);
    }

    public void setExcludeMdcKeyNames(List<String> excludeMdcKeyNames) {
        this.excludeMdcKeyNames = new ArrayList<>(excludeMdcKeyNames);
    }

}
