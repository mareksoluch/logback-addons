package org.solo.logback.composite.loggingevent;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MdcJsonArrayProvider extends AbstractMdcJsonProvider {

    private List<String> arrayFieldNames = new ArrayList<>();
    private final ArrayValuePrinter arrayValuePrinter = new ArrayValuePrinter();
    private String arraySeparator = ",";

    @Override
    void writeProperties(JsonGenerator generator, Map<String, String> mdcProperties) throws IOException {
        for (Map.Entry<String, String> entry : mdcProperties.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null && value != null) {
                generator.writeFieldName(key);
                generator.writeObject(arrayValuePrinter.printValue(key, value, arrayFieldNames, arraySeparator));
            }
        }
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
