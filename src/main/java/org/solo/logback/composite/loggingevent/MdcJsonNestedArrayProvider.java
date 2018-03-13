package org.solo.logback.composite.loggingevent;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MdcJsonNestedArrayProvider extends MdcJsonNestedObjectProvider {

    private String arraySeparator = ",";
    private List<String> arrayFieldNames = new ArrayList<>();
    private final ArrayValuePrinter arrayValuePrinter = new ArrayValuePrinter();

    public MdcJsonNestedArrayProvider() {
        super(true);
    }

    public MdcJsonNestedArrayProvider(boolean printNestedObjects) {
        super(printNestedObjects);
    }

    @Override
    protected void writeJsonValue(JsonGenerator generator, String jsonName, String jsonValue, String jsonPath) throws IOException {
        generator.writeObjectField(jsonName, arrayValuePrinter.printValue(jsonPath, jsonValue, arrayFieldNames, arraySeparator));
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
