package pl.marko.logback.composite.loggingevent;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class MdcJsonArrayProvider extends AbstractMdcJsonProvider {

    private String arraySeparator = ",";
    private List<String> arrayFieldNames;

    @Override
    void writeProperties(JsonGenerator generator, Map<String, String> mdcProperties) throws IOException {
        for (Map.Entry<String, String> entry : mdcProperties.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                generator.writeFieldName(entry.getKey());
                generator.writeObject(convertToArrayIfPossible(entry.getKey(), entry.getValue()));
            }
        }
    }

    private Object convertToArrayIfPossible(String key, String value) {
        return arrayFieldNames.contains(key)
                ? valueToArray(value)
                : value;
    }

    private Collection<String> valueToArray(String value) {
        if (value != null && value.contains(arraySeparator)) {
            return asList(value.split(arraySeparator));
        }
        return singletonList(value);
    }


    public void addArrayFieldName(String fieldName) {
        if (this.arrayFieldNames == null)
            this.arrayFieldNames = new ArrayList<>();

        this.arrayFieldNames.add(fieldName);

    }

    public void setArrayFieldNames(List<String> arrayFieldNames) {
        this.arrayFieldNames = arrayFieldNames;
    }

    public void setArraySeparator(String arraySeparator) {
        this.arraySeparator = arraySeparator;
    }
}
